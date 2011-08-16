package models;

import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Sponsor extends Model {
	@Required
	public String company;
	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@Required
	@MaxSize(10000)
	public String about;
	@URL
	public String companyURL;
	public String twitterAccount;
	
	public Blob logo;
	
	@Override
	public String toString() {
		return company;
	}
}
