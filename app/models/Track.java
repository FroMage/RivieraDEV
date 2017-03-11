package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Track extends Model implements Comparable<Track>{
	@Required
	public String title;
	
	@Required
	@MaxSize(1)
	public Integer order;

	@OneToMany(mappedBy = "track")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString(){
		return title;
	}

	@Override
	public int compareTo(Track other) {
		return order.compareTo(other.order);
	}
}
