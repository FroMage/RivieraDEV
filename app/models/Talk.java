package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.apache.commons.lang.StringUtils;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Talk extends Model {

	@Required
	@ManyToOne
	public Slot slot;

	@Required
	public String title;
	
	@Required
	@Lob
	@MaxSize(10000)
	public String description; 
	
	@ManyToOne
	public Track track;
	
	public boolean isBreak;
	
    @JoinTable(
            name="talk_speaker",
            joinColumns=@JoinColumn(name="talk_id"),
            inverseJoinColumns=@JoinColumn(name="speakers_id")
        )
	@ManyToMany
	public List<Speaker> speakers = new ArrayList<Speaker>();
	
	@Override
	public String toString() {
		return track+" "+slot+": "+title+" ("+StringUtils.join(speakers, ", ")+")";
	}
}
