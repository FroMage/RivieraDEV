package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.hibernate.proxy.HibernateProxy;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import play.Logger;
import play.db.Model;
import play.db.Model.Factory;
import play.db.Model.Manager;
import play.db.Model.Property;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class YMLExporter extends Controller {

	public static void export(List<String> hql, List<String> exclude) throws Exception{
		Map<Object, SerialisedType> objects = new HashMap<Object, SerialisedType>();
		if(hql != null && !hql.isEmpty()){
			Stack<Model> towalk = new Stack<Model>();
			for(String oneHQL : hql){
				if(StringUtils.isEmpty(oneHQL))
					continue;
				List<Model> resultList = JPA.em().createQuery(oneHQL).getResultList();
				for(Model root : resultList){
					towalk.add(root);
				}
			}
			DirectedGraph<String,DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
			while(!towalk.isEmpty()){
				Model object = towalk.pop();
				Class klass = getClass(object);
				if(exclude.contains(klass.getName()))
					continue;
				Factory factory = Manager.factoryFor((Class<? extends Model>) klass);
				notFoundIfNull(factory, "Type is not model: "+klass);
				walk(object, factory, objects, towalk, graph, exclude);
			}
			List<SerialisedType> serialised = sort(objects.values(), graph);
			render(serialised, hql, exclude);
		}
		hql = new ArrayList<String>(5);
		exclude = new ArrayList<String>(5);
		render(hql, exclude);
	}

	// sort according to dependencies
	private static List<SerialisedType> sort(Collection<SerialisedType> values, DirectedGraph<String,DefaultEdge> graph) {
		CycleDetector<String, DefaultEdge> cycleDetector = new CycleDetector<String, DefaultEdge>(graph);
		if(cycleDetector.detectCycles())
			error("Cycle detected: could not sort data");
		TopologicalOrderIterator<String,DefaultEdge> orderIterator = new TopologicalOrderIterator<String, DefaultEdge>(graph);
		List<String> orderedTypes = new LinkedList<String>();
		while(orderIterator.hasNext())
			orderedTypes.add(orderIterator.next());

		// now find all the dependencies of each type
		List<SerialisedType> orderedSerialisedTypes = new ArrayList<YMLExporter.SerialisedType>();
		for(String type : orderedTypes){
			for(SerialisedType serialised : values){
				if(serialised.type.equals(type)){
					orderedSerialisedTypes.add(serialised);
				}
			}
		}
		return orderedSerialisedTypes;
	}

	private static void walk(Model root, Factory factory, Map<Object, SerialisedType> objects, Stack<Model> towalk, DirectedGraph<String,DefaultEdge> graph, List<String> exclude) throws IllegalArgumentException, IllegalAccessException {
		if(root instanceof HibernateProxy)
			root = (Model) ((HibernateProxy)root).writeReplace();
		SerialisedType serialised = new SerialisedType(root);
		objects.put(root, serialised);
		if(!graph.containsVertex(serialised.type))
			graph.addVertex(serialised.type);
		for(Property prop : factory.listProperties()){
			if(prop.name.startsWith("sync"))
				continue;
			Object val = prop.field.get(root);
			if(val == null)
				continue;
			// for some reason prop.isRelation is not set for non-owning relations, which is probably a bug
			boolean isMultiple = prop.isMultiple || 
			(Collection.class.isAssignableFrom(prop.type) 
					&& (prop.field.isAnnotationPresent(OneToMany.class) || prop.field.isAnnotationPresent(ManyToMany.class)));
			// do we list ids?
			if(isMultiple){
				// walk them later
				Collection<Model> c = (Collection) val;
				boolean manyToMany = prop.field.isAnnotationPresent(ManyToMany.class) && prop.isRelation;
				for(Model f : c){
					if(!objects.containsKey(f)){
						towalk.add(f);
						if(manyToMany && !exclude.contains(getClass(f).getName())){
							addDependency(graph, serialised.type, f);
							serialised.add(prop.name, makeKey(f));
						}
					}
				}
			}else if(prop.isRelation){
				// walk it later
				Model m = (Model)val;
				addDependency(graph, serialised.type, m);
				if(!objects.containsKey(m)){
					towalk.add(m);
				}
				// do not include excluded relations
				if(!exclude.contains(getClass(m).getName()))
					serialised.add(prop.name, makeKey(m));
			}else{
				serialised.add(prop.name, val);
			}
		}
	}

	private static void addDependency(DirectedGraph<String,DefaultEdge> graph, String from, Model m) {
		String to = getClass(m).getName();
		if(!graph.containsVertex(to))
			graph.addVertex(to);
		if(!graph.containsEdge(to, from)){
			Logger.info("%s depends from %s", from, to);
			graph.addEdge(to, from);
		}
	}

	public static class SerialisedType implements Comparable<SerialisedType> {
		public SerialisedType(Model m) {
			this.type = YMLExporter.getClass(m).getName();
			this.id = makeKey(m);
		}
		public void add(String name, Object val) {
			if(val instanceof Blob)
				return;
			else if(val instanceof String){
				val = "\""+((String)val).replaceAll("\n", "\\\\n").replaceAll("\"", "\\\"")+"\"";
			}
			else if(val instanceof Date){
				final GregorianCalendar calendar = new GregorianCalendar();
				calendar.setTime((Date)val);
				XMLGregorianCalendar xmlCalendar;
				try {
					xmlCalendar = DatatypeFactory.newInstance()
							.newXMLGregorianCalendar(calendar);
				} catch (DatatypeConfigurationException e) {
					throw new RuntimeException(e);
				}
				val = xmlCalendar.toXMLFormat();
			}
			properties.put(name, val);
		}
		public String type;
		public String id;
		public Map<String, Object> properties = new TreeMap<String, Object>();
		@Override
		public int compareTo(SerialisedType other) {
			return id.compareTo(other.id);
		}
	}

	public static String makeKey(Model m) {
		Object key = m._key();
		Class<?> c = key.getClass();
		if(c != String.class 
				&& !Number.class.isAssignableFrom(c))
			key = key.hashCode();
		return getClass(m).getSimpleName()+":"+key;
	}
	
	public static Class getClass(Model m){
		Class<?> klass = m.getClass();
		if(klass.getName().contains("$"))
			return klass.getSuperclass();
		return klass;
	}
}
