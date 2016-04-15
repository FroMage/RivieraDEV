package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class News extends Model {

	@Field("publishedDate")
	@Required
	public Date publishedDate;

	@Required
	public String title;
	
	@Required
	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@MaxSize(10000)
	public String contents; 
	
	@Override
	public String toString() {
		return publishedDate
				+": "+title;
	}
	
	public static List<News> byDate(){
		return find("ORDER BY publishedDate DESC").fetch();
	}

	public static News latest() {
		return find("ORDER BY publishedDate DESC").first();
	}
}
