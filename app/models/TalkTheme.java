package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class TalkTheme extends Model implements Comparable<TalkTheme>{
	
    @Required
	public String theme;
	
	@Required
	@Enumerated(EnumType.STRING)
	public TalkThemeColor color;

	@OneToMany(mappedBy = "theme")
	public List<Talk> talks = new ArrayList<Talk>();
	
	@Override
	public String toString(){
		return theme;
	}

	@Override
	public int compareTo(TalkTheme other) {
		return theme.compareTo(other.theme);
	}
}