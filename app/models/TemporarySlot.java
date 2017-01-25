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
public class TemporarySlot extends Model {
	
	@Field("startDate")
	@Required
	public Date startDate;
	
	@Field("endDate")
	@Required
	public Date endDate;
	
	@Field("labelEN")
	public String labelEN;
	@Field("labelFR")
	public String labelFR;
	
	@Override
	public String toString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append(dateFormat.format(startDate))
		      .append(" ")
		      .append(timeFormat.format(startDate))
			  .append(" - ")
			  .append(timeFormat.format(endDate));

		strbuf.append(" (" + labelEN + ")");	
		
		return strbuf.toString();
	}
	
	public static List<Slot> findPerDay(Date day){
		return find("date_trunc('day', startDate) = ? ORDER BY startDate", day).fetch();
	}
	
}