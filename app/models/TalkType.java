package models;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;


import play.data.validation.Required;
import play.db.jpa.Model;
import play.i18n.Lang;

@Entity
public class TalkType extends Model implements Comparable<TalkType>{
	
    @Required
	public String typeEN;

    @Required
    public String typeFR;
    
	@OneToMany(mappedBy = "type")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString(){
        if (Lang.get().equals("en")) { // English
            return typeEN;
        }
		return typeFR;
	}

	@Override
	public int compareTo(TalkType other) {
		return typeEN.compareTo(other.typeEN);
	}

	public static List<TalkType> findUsedTypes() {
		return find("SELECT DISTINCT talk.type FROM Talk talk").fetch();
	}

}