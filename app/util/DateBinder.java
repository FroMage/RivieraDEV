package util;

import play.Logger;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import models.Field;
import play.data.binding.AnnotationHelper;
import play.libs.I18N;
import play.mvc.Http.Request;

/**
 * Binder that support Date class.
 */
@Global
public class DateBinder implements TypeBinder<Date> {

    public static final String ISO8601 = "'ISO8601:'yyyy-MM-dd'T'HH:mm:ssZ";

    public Date bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) throws Exception {

    	Field field = getAnnotation(Field.class, annotations);
    	String time = null;
    	if(field != null){
    		time = Request.current().params.get(name+"_time");
    	}
    	Logger.debug("Invoked date binder for %s=%s time: %s", name, value, time);
    	
        Date date = AnnotationHelper.getDateAs(annotations, value);
        if (date != null) {
            return addTime(date, time);
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(I18N.getDateFormat());
            sdf.setLenient(false);
            return addTime(sdf.parse(value), time);
        } catch (ParseException e) {
             // Ignore
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(ISO8601);
            sdf.setLenient(false);
            return addTime(sdf.parse(value), time);
        } catch(Exception e) {
            throw new IllegalArgumentException("Cannot convert [" + value + "] to a Date: " + e.toString());
        }
        
    }

	private Date addTime(Date date, String time) {
		if(StringUtils.isEmpty(time))
			return date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            Date timeDate = sdf.parse(time);
            
            date.setHours(timeDate.getHours());
            date.setMinutes(timeDate.getMinutes());
            return date;
        } catch(Exception e) {
            throw new IllegalArgumentException("Cannot convert [" + time + "] to a Time: " + e.toString());
        }

	}

	private <T extends Annotation> T getAnnotation(Class<T> klass, Annotation[] annotations) {
		for(Annotation ann : annotations)
			if(klass.isAssignableFrom(ann.getClass()))
				return (T) ann;
		return null;
	}
}
