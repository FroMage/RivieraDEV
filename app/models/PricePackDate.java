package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class PricePackDate extends Model {

    @Required
    public Date blindBirdEndDate;

    @Required
    public Date earlyBirdEndDate;

    @Required
    public Date regularEndDate;

    @Override
    public String toString() {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
      StringBuffer strbuf = new StringBuffer();
		  strbuf.append(dateFormat.format(blindBirdEndDate))
		    .append(" - ")
		    .append(dateFormat.format(earlyBirdEndDate))
			  .append(" - ")
			  .append(dateFormat.format(regularEndDate));
      return strbuf.toString();
    }
}