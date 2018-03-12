package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.i18n.Lang;

@Entity
public class Talk extends Model implements Comparable<Talk> {

	@ManyToOne
	public Slot slot;

	// At least one title must be filled
	public String titleEN;
	public String titleFR;
	
	// At least one description must be filled
	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@MaxSize(10000)
	public String descriptionEN;

	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@MaxSize(10000)
	public String descriptionFR; 
	
	@ManyToOne
	public Track track;
	
	@Required
	@Enumerated(EnumType.STRING)
	public BreakType isBreak;

	@Required
	@Enumerated(EnumType.STRING)
	public Language language;

	@Enumerated(EnumType.STRING)
	public Level level;

	@ManyToOne
	public TalkTheme theme;

	public String slidesUrl;
	
	// Permet de cacher ce talk dans la page qui liste les talks.
	// (Ex: 'Keynote des Orga', 'Accueil', etc...)
	public boolean isHiddenInTalksPage;

    @JoinTable(
            name="talk_speaker",
            joinColumns=@JoinColumn(name="talk_id"),
            inverseJoinColumns=@JoinColumn(name="speakers_id")
        )
	@ManyToMany
	public List<Speaker> speakers = new ArrayList<Speaker>();
	

	public String getTitle() {
		String displayedTitle = "";

		if (Lang.get().equals("en")) { // English
			if (titleEN != null && titleEN.length() > 0) {
				displayedTitle = titleEN;
			} 
			else if (titleFR != null && titleFR.length() > 0) {
				displayedTitle = titleFR;
			}
		}
		else { // French
			if (titleFR != null && titleFR.length() > 0) {
				displayedTitle = titleFR;
			}
			else if (titleEN != null && titleEN.length() > 0) {
				displayedTitle = titleEN;
			} 
		}
		return displayedTitle;
	}

	public String getDescription() {
		String displayedDescription = "";

		if (Lang.get().equals("en")) { // English
			if (descriptionEN != null && descriptionEN.length() > 0) {
				displayedDescription = descriptionEN;
			} 
			else if (descriptionFR != null && descriptionFR.length() > 0) {
				displayedDescription = descriptionFR;
			}
		}
		else { // French
			if (descriptionFR != null && descriptionFR.length() > 0) {
				displayedDescription = descriptionFR;
			}
			else if (descriptionEN != null && descriptionEN.length() > 0) {
				displayedDescription = descriptionEN;
			} 
		}
		return displayedDescription;
	}


	@Override
	public String toString() {
		return (slot != null ? slot : "no slot") 
		        + " "
		        + (track != null ? track : "All tracks")
				+ ": " + getTitle() 
				+ (speakers.size() > 0 ? " (" + StringUtils.join(speakers, ", ") + ")" : "");
	}

	public static List<Track> findTracksPerDay(Date day) {
		return find("SELECT DISTINCT talk.track FROM Talk talk LEFT JOIN talk.slot AS slot WHERE date_trunc('day', slot.startDate) = ?", day).fetch();
	}

	public int compareTo(Talk other){
		return this.getTitle().compareTo(other.getTitle());
	}

	public static List<Talk> findKeynotes() {
		return Talk.find("track IS NULL AND isBreak = '" + BreakType.NotABreak + "' AND isHiddenInTalksPage = false").fetch();
	}
}
