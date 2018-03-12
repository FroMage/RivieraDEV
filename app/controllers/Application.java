package controllers;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Configuration;
import models.ConfigurationKey;
import models.Language;
import models.Level;
import models.News;
import models.Organiser;
import models.PreviousSpeaker;
import models.Slot;
import models.Speaker;
import models.Sponsor;
import models.SponsorShip;
import models.Talk;
import models.TalkTheme;
import models.TalkType;
import models.TemporarySlot;
import models.Track;
import play.i18n.Lang;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

	@Before
	private static void setup(){
		renderArgs.put("user", Security.connected());
		renderArgs.put("promotedPage", getPromotedPage());
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
		List<Speaker> speakersPreviousYears = PreviousSpeaker.find("ORDER BY lastName, firstName").fetch();
		List<Speaker> speakersStar = Speaker.find("star = true ORDER BY lastName, firstName").fetch();

		boolean lunchesAndPartySoldOut = sponsors.get(SponsorShip.Lunches) != null && sponsors.get(SponsorShip.Lunches).size() > 0
		                              && sponsors.get(SponsorShip.Party) != null && sponsors.get(SponsorShip.Party).size() > 0;

		boolean displayPreviousSpeakers = !displayNewSpeakers();

		render(googleMapApiKey, displayPreviousSpeakers, sponsors, lunchesAndPartySoldOut, sponsorsPreviousYears, speakersPreviousYears, speakersStar, latestNews);
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
		List<TalkTheme> themes = TalkTheme.findUsedThemes();
		Collections.sort(tracks);
		Level[] levels = Level.values();
		Language[] languages = Language.values();
    	Map<Date,List<Track>> tracksPerDays = new HashMap<Date, List<Track>>();
    	for(Date day : days){
    		List<Track> tracksPerDay = Talk.findTracksPerDay(day);
    		Collections.sort(tracksPerDay);
    		tracksPerDays.put(day, tracksPerDay);
    	}

		boolean displayFullSchedule = displayFullSchedule();
		boolean displayNewSpeakers = displayNewSpeakers();

    	render(displayFullSchedule, displayNewSpeakers, days, tracks, languages, tracksPerDays, themes, levels);
    }

    public static void scheduleSuperSecret(){
    	List<Date> days = Slot.find("select distinct date_trunc('day', startDate) from Slot ORDER BY date_trunc('day', startDate)").fetch();
    	List<Track> tracks = Track.findAll();
		List<TalkTheme> themes = TalkTheme.findUsedThemes();
		List<TalkType> types = TalkType.findUsedTypes();
		Collections.sort(types);
		Level[] levels = Level.values();
    	Map<Date,List<Track>> tracksPerDays = new HashMap<Date, List<Track>>();
    	for(Date day : days){
    		List<Track> tracksPerDay = Talk.findTracksPerDay(day);
    		Collections.sort(tracksPerDay);
    		tracksPerDays.put(day, tracksPerDay);
		}
		Language[] languages = Language.values();
    	render(days, tracks, tracksPerDays, themes, types, levels, languages);
    }

    public static void talks(){
		List<TalkTheme> themes = TalkTheme.findUsedThemes();
		Level[] levels = Level.values();
		List<TalkType> types = TalkType.findUsedTypes();
		Collections.sort(types);
    	List<Talk> talks = Talk.find("isHiddenInTalksPage = false").fetch();
		Collections.sort(talks);
		boolean displayFullSchedule = displayFullSchedule();
		Language[] languages = Language.values();
    	render(themes, levels, talks, types, languages, displayFullSchedule);
    }

    public static void speakers(){
    	List<Speaker> speakers = Speaker.find("ORDER BY lastName, firstName").fetch();
		List<Speaker> speakersPreviousYears = PreviousSpeaker.find("ORDER BY lastName, firstName").fetch();
		boolean displayPreviousSpeakers = !displayNewSpeakers();

    	render(speakers, speakersPreviousYears, displayPreviousSpeakers);
    }

    public static void speaker(Long id){
    	Speaker speaker= Speaker.findById(id);
    	notFoundIfNull(speaker);
    	render(speaker);
    }

    public static void talk(Long id){
    	Talk talk = Talk.findById(id);
    	notFoundIfNull(talk);
		boolean displayFullSchedule = displayFullSchedule();
    	render(talk, displayFullSchedule);
    }

    public static void judcon(){
    	List<Speaker> speakers = Speaker.find("isJUDConSpeaker = true ORDER BY lastName, firstName").fetch();
    	render(speakers);
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

    public static void previousSpeakerPhoto(Long id){
    	PreviousSpeaker speaker = PreviousSpeaker.findById(id);
    	notFoundIfNull(speaker);
    	if(!speaker.photo.exists())
    		redirect("/public/images/unicorn-horn-lashes.svg");
    	response.contentType = speaker.photo.type();
    	renderBinary(speaker.photo.get());
    }

    public static void speakerPhoto(Long id){
    	Speaker speaker = Speaker.findById(id);
    	notFoundIfNull(speaker);
    	if(!speaker.photo.exists())
    		redirect("/public/images/unicorn-horn-lashes.svg");
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
    	if(!organiser.photo.exists())
    		redirect("/public/images/unicorn-horn-lashes.svg");
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

	/**
	 * Retourne true si le programme complet doit être affiché, faux sinon.
	 */
    private static boolean displayFullSchedule(){
        Configuration config = Configuration.find("key",ConfigurationKey.DISPLAY_FULL_SCHEDULE).first();
		return config != null && config.value.equals("true") ;
    }

	/**
	 * Retourne true si les speakers de la nouvelle édition doivent être affichés
	 * (utile avant que le programme définitif ne soit connu)
	 */
    private static boolean displayNewSpeakers(){
        Configuration config = Configuration.find("key",ConfigurationKey.DISPLAY_NEW_SPEAKERS).first();
		return config != null && config.value.equals("true") ;
    }

	/**
	 * Retourne le nom de la page à mettre en avant sur le site.
	 * 'CFP'      : La page du CFP
	 * 'TICKETS'  : La page d'achat de tickets
	 * 'SPONSORS' : La page pour devenir un sponsor
	 */
    private static String getPromotedPage(){
        Configuration config = Configuration.find("key",ConfigurationKey.PROMOTED_PAGE).first();
		return config != null ? config.value : "";
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

	public static void live(){
    	List<Track> tracks = Track.findAll();
		Collections.sort(tracks);
		render(tracks);
	}
	
	public static void liveTrack(String track){
    	List<Track> tracks = Track.findAll();
		Collections.sort(tracks);
    	List<Talk> keynotes = Talk.findKeynotes();
		render(tracks, track, keynotes);
	}
}