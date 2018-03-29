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
	public int position;

	public boolean isJUDCon;
	
	@OneToMany(mappedBy = "track")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString(){
		return "[" + position + "] " + title;
	}

	@Override
	public int compareTo(Track other) {
		//return position.compareTo(other.position);
		return position - other.position;
	}
}
