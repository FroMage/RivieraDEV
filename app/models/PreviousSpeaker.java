package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Type;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class PreviousSpeaker extends Model {

	@Required
	public String firstName;

	@Required
	public String lastName;
	
	@Required
	public String company;
	
	public Blob photo;
	
	/* La dernière année à laquelle l'orateur a participé au RivieraDEV */
	@Required
	@MaxSize(4)
	public Integer year;
	
	@Override
	public String toString() {
		return firstName+" "+lastName;
	}
}
