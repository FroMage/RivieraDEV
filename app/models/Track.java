package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Track extends Model implements Comparable<Track>{
	@Required
	public String title;
	
	@OneToMany(mappedBy = "track")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString(){
		return title;
	}

	@Override
	public int compareTo(Track other) {
		return title.compareTo(other.title);
	}
}
