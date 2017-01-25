package controllers;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Configuration;
import models.ConfigurationKey;
import models.News;
import models.Organiser;
import models.Slot;
import models.Speaker;
import models.Sponsor;
import models.SponsorShip;
import models.Talk;
import models.TemporarySlot;
import models.Track;
import play.i18n.Lang;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	private static void setup(){
		renderArgs.put("user", Security.connected());
	}
	
	public static void fr(String url){
		Lang.change("fr");
		redirect(url);
	}

	public static void en(String url){
		Lang.change("en");
		redirect(url);
	}

    public static void index() {
		String googleMapApiKey = getGoogleMapApiKey();

    	News latestNews = News.latest();

		SponsorsToDisplay sponsorsToDisplay = getSponsorsToDisplay();
		Map<SponsorShip, List<Sponsor>> sponsors = sponsorsToDisplay.getSponsors();
		List<Sponsor> sponsorsPreviousYears = sponsorsToDisplay.getSponsorsPreviousYears();
		List<Speaker> speakersPreviousYears = getSpeakersPreviousYears();				

		boolean lunchesAndPartySoldOut = sponsors.get(SponsorShip.Lunches) != null && sponsors.get(SponsorShip.Lunches).size() > 0
		                              && sponsors.get(SponsorShip.Party) != null && sponsors.get(SponsorShip.Party).size() > 0;

		render(googleMapApiKey, sponsors, lunchesAndPartySoldOut, sponsorsPreviousYears, speakersPreviousYears, latestNews);
    }

    public static void news() {
    	List<News> news = News.byDate();
    	render(news);
    }
    
    public static void about() {
    	organisers();
    }

    public static void access() {
        render();
    }

    public static void cfp() {
    	List<Organiser> orgas = Organiser.cfp();
        render(orgas);
    }

    public static void subscribe() {
        render();
    }
    
    public static void schedule(){
    	List<Date> days = TemporarySlot.find("select distinct date_trunc('day', startDate) from TemporarySlot ORDER BY date_trunc('day', startDate)").fetch();
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

    public static void orgaPhoto(Long id){
    	Organiser organiser = Organiser.findById(id);
    	notFoundIfNull(organiser);
    	response.contentType = organiser.photo.type();
    	renderBinary(organiser.photo.get());
    }

	public static void organisers() {
    	List<Organiser> orgas = Organiser.organisers();
		render(orgas);
	}

	public static void organiser(Long id) {
		Organiser orga = Organiser.findById(id);
    	notFoundIfNull(orga);
    	render(orga);
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

	/**
	 * Retourne l'API KEY sauvée en BD.
	 * En local, si la clé n'est pas définie alors la google map fonctionne quand même.
	 * MAIS en Prod/Staging, il FAUT une API Key sinon la carte ne fonctionne pas c'est certainement une restriction google.
	 * L'API KEY de Prod ne peut pas être utilisée en local, car nous l'avons restreinte pour ne fonctionner qu'avec les domaines *.rivieradev.fr
	 * et *.rivieradev.com afin de suivre les recommandations de sécurité décrites par Google.
	 * Pour générer une nouvelle API KEY : https://developers.google.com/maps/documentation/javascript/get-api-key?hl=Fr 
	 */
    private static String getGoogleMapApiKey(){
        Configuration config = Configuration.find("key",ConfigurationKey.GOOGLE_MAP_API_KEY).first();
		if(config != null){
			return config.value;
		}
		return "";
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