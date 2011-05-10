package models;

import javax.persistence.Entity;

import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity(name = "user_table")
public class User extends Model {

	@Required
	public String firstName, lastName, userName;
	@Required
	@Password
	public String password;
	
	@Override
	public String toString() {
		return firstName+" "+lastName+" ("+userName+")";
	}
}
