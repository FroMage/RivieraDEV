package models;

import javax.persistence.Entity;

import play.data.validation.Password;
import play.data.validation.Required;
import play.db.jpa.Model;
import util.BCrypt;

@Entity(name = "user_table")
public class User extends Model {

	@Required
	public String firstName, lastName, userName;
	// This is hashed with bCrypt
	@Required
	@Password
	public String password;
	
	public Boolean isBCrypt;

	@Override
	public void _save() {
	    // allows us to create users with plain text passwords in the admin CRUD pages
	    if(isBCrypt == null || !isBCrypt) {
	        password = BCrypt.hashpw(password, BCrypt.gensalt());
	        isBCrypt = true;
	    } else {
	        if(!password.startsWith("$2a$"))
	            throw new IllegalStateException("Password is not hashed with BCrypt");
	    }
	    super._save();
	}
	
	@Override
	public String toString() {
		return firstName+" "+lastName+" ("+userName+")";
	}

    public boolean checkPassword(String sentPassword) {
        if(isBCrypt != null && isBCrypt)
            return BCrypt.checkpw(sentPassword, password);
        return password.equals(sentPassword);
    }
}
