package controllers;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.News;
import models.Slot;
import models.Speaker;
import models.Sponsor;
import models.SponsorShip;
import models.Talk;
import models.Track;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	private static void setup(){
		renderArgs.put("user", Security.connected());
	}
	
    public static void index() {
    	News latestNews = News.latest();

		SponsorsToDisplay sponsorsToDisplay = getSponsorsToDisplay();
		Map<SponsorShip, List<Sponsor>> sponsors = sponsorsToDisplay.getSponsors();
		List<Sponsor> sponsorsPreviousYears = sponsorsToDisplay.getSponsorsPreviousYears();
		List<Speaker> speakersPreviousYears = getSpeakersPreviousYears();

		render(sponsors, sponsorsPreviousYears, speakersPreviousYears, latestNews);
    }

    public static void news() {
    	List<News> news = News.byDate();
    	render(news);
    }
    
    public static void about() {
        render();
    }

    public static void access() {
        render();
    }

    public static void cfp() {
        render();
    }

    public static void subscribe() {
        render();
    }
    
    public static void schedule(){
    	List<Date> days = Slot.find("select distinct date_trunc('day', startDate) from Slot ORDER BY date_trunc('day', startDate)").fetch();
    	List<Track> tracks = Track.findAll();
    	Map<Date,List<Track>> tracksPerDays = new HashMap<Date, List<Track>>();
    	for(Date day : days){
    		List<Track> tracksPerDay = Talk.findTracksPerDay(day);
    		Collections.sort(tracksPerDay);
    		tracksPerDays.put(day, tracksPerDay);
    	}
    	render(days, tracks, tracksPerDays);
    }

    public static void scheduleSuperSecret(){
    	schedule();
    }

    public static void talks(){
    	List<Talk> talks = Talk.find("ORDER BY title").fetch();
    	render(talks);
    }

    public static void speakers(){
		// TODO : Récupérer l'année en BD ou dans une config
		int newRivieraDevYear = getRivieraDevYear();
    	List<Speaker> speakers = Speaker.find("year = ? ORDER BY lastName, firstName", newRivieraDevYear).fetch();
		List<Speaker> speakersPreviousYears = getSpeakersPreviousYears();
    	render(speakers, speakersPreviousYears);
    }

    public static void speaker(Long id){
    	Speaker speaker= Speaker.findById(id);
    	notFoundIfNull(speaker);
    	render(speaker);
    }

    public static void talk(Long id){
    	Talk talk = Talk.findById(id);
    	notFoundIfNull(talk);
    	render(talk);
    }

    public static void sponsors() {
		SponsorsToDisplay sponsorsToDisplay = getSponsorsToDisplay();
		Map<SponsorShip, List<Sponsor>> sponsors = sponsorsToDisplay.getSponsors();
		List<Sponsor> sponsorsPreviousYears = sponsorsToDisplay.getSponsorsPreviousYears();
		
		render(sponsors, sponsorsPreviousYears);
    }
    
	public static void becomeSponsor() {
		render();
	}

    public static void speakerPhoto(Long id){
    	Speaker speaker = Speaker.findById(id);
    	notFoundIfNull(speaker);
    	response.contentType = speaker.photo.type();
    	renderBinary(speaker.photo.get());
    }

    public static void sponsorLogo(Long id){
    	Sponsor sponsor = Sponsor.findById(id);
    	notFoundIfNull(sponsor);
    	response.contentType = sponsor.logo.type();
    	renderBinary(sponsor.logo.get());
    }

	public static void orga() {
		render();
	}

	private static int getRivieraDevYear(){
		// TODO : Récupérer l'année en BD ou dans une config
		return 2017;
	}

	private static List<Speaker> getSpeakersPreviousYears(){
		return Speaker.find("year is null OR year < ? ORDER BY lastName, firstName",getRivieraDevYear()).fetch();
	}

	private static SponsorsToDisplay getSponsorsToDisplay() {
		boolean mustDisplaySponsorsPreviousYears = true;

		Map<SponsorShip, List<Sponsor>> sponsors = new TreeMap<SponsorShip, List<Sponsor>>();
		for(SponsorShip sponsorShip : SponsorShip.values()) {
			if (sponsorShip != SponsorShip.PreviousYears) {
				List<Sponsor> sponsorsBySponsorShip = Sponsor.find("level", sponsorShip).fetch();
				if (sponsorsBySponsorShip != null && sponsorsBySponsorShip.size() > 0) {
					mustDisplaySponsorsPreviousYears = false;
					Collections.sort(sponsorsBySponsorShip);
					sponsors.put(sponsorShip, sponsorsBySponsorShip);
				}
			}
		}

		List<Sponsor> sponsorsPreviousYears = null;
		if (mustDisplaySponsorsPreviousYears) {
			sponsorsPreviousYears = Sponsor.find("level", SponsorShip.PreviousYears).fetch();
			Collections.sort(sponsorsPreviousYears);
		}

		return new SponsorsToDisplay(sponsors, sponsorsPreviousYears);
	}


	private static class SponsorsToDisplay {
		private Map<SponsorShip, List<Sponsor>> sponsors;
		private List<Sponsor> sponsorsPreviousYears;

		public SponsorsToDisplay(Map<SponsorShip, List<Sponsor>> sponsors, List<Sponsor> sponsorsPreviousYears) {
			this.sponsors = sponsors;
			this.sponsorsPreviousYears = sponsorsPreviousYears;
		}

		public Map<SponsorShip, List<Sponsor>> getSponsors() { return this.sponsors; }
		public List<Sponsor> getSponsorsPreviousYears() { return this.sponsorsPreviousYears; }
	}

}