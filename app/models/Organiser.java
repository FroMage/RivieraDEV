package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Organiser extends Model {
	public String firstName;
	@Required
	public String lastName;
	public String title;
	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@Required
	@MaxSize(10000)
	public String biography;
	public String company;
	@URL
	public String companyURL;
	@URL
	public String blogURL;
	public String twitterAccount;
	
	public Blob photo;

	public boolean orga;
	public boolean cfp;
	
	@Override
	public String toString() {
		return firstName+" "+lastName;
	}

	public static List<Organiser> organisers() {
		//return find("orga = true ORDER BY firstname, lastname").fetch();
		return find("ORDER BY firstname, lastname").fetch();
	}

	public static List<Organiser> cfp() {
		return find("cfp = true ORDER BY firstname, lastname").fetch();
	}
}
