package models;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class Speaker extends Model {
	public String firstName;
	@Required
	public String lastName;
	public String title;
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
	
	@ManyToMany(mappedBy = "speakers")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString() {
		return firstName+" "+lastName;
	}
}
