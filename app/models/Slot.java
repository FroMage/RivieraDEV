package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@SuppressWarnings("serial")
@Entity
public class Slot extends Model {
	
	@Field("startDate")
	@Required
	public Date startDate;
	
	@Field("endDate")
	@Required
	public Date endDate;
	
	@OneToMany(mappedBy = "slot")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		return dateFormat.format(startDate)+" "+timeFormat.format(startDate)+" - "+timeFormat.format(endDate);
	}
	
	public static List<Slot> findPerDay(Date day){
		return find("date_trunc('day', startDate) = ? ORDER BY startDate", day).fetch();
	}
	
	public Talk getTalkPerTrack(Track track){
		for(Talk talk : talks)
			if(talk.track == track)
				return talk;
		return null;
	}
}
