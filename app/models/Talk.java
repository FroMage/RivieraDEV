package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Talk extends Model {

	@ManyToOne
	public Slot slot;

	@Required
	public String title;
	
	@Required
	@Type(type="org.hibernate.type.StringClobType")
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
		return (track != null? track : "All tracks")+" "
				+(slot != null ? slot : "no slot")
				+": "+title+" ("+StringUtils.join(speakers, ", ")+")";
	}

	public static List<Track> findTracksPerDay(Date day) {
		return find("SELECT DISTINCT talk.track FROM Talk talk LEFT JOIN talk.slot AS slot WHERE date_trunc('day', slot.startDate) = ?", day).fetch();
	}
}
