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
	
	// Only used for the "basic" schedule (displayed before knowing the exact program) 
	@Field("labelEN")
	public String labelEN;
	@Field("labelFR")
	public String labelFR;

	@OneToMany(mappedBy = "slot")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append(dateFormat.format(startDate)).append(" ")
		.append(timeFormat.format(startDate)).append(" - ").append(timeFormat.format(endDate));

		if (labelEN != null && labelEN.length() > 0) {
			strbuf.append(" (" + labelEN + ")");	
		
		} else {
			strbuf.append(" (");
			boolean first = true;
			for(Talk talk : talks){
				if(!first)
					strbuf.append(", ");
				first = false;
				strbuf.append(talk.track);
			}
			strbuf.append(")");
		}
		return strbuf.toString();
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
	
	public Talk getAllTracksEvent(){
		return talks.size() == 1 && talks.get(0).track == null ? talks.get(0) : null;
	}
}