package controllers;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    	List<Sponsor> sponsors = Sponsor.findAll();
		List<Sponsor> sponsorsGold = Sponsor.find("level", SponsorShip.Gold).fetch();
		List<Sponsor> sponsorsSilver = Sponsor.find("level", SponsorShip.Silver).fetch();
		List<Sponsor> sponsorsBronze = Sponsor.find("level", SponsorShip.Bronze).fetch();
    	Collections.sort(sponsors);
		Collections.sort(sponsorsGold);
        render(sponsors, sponsorsGold, sponsorsSilver, sponsorsBronze, latestNews);
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
    	List<Speaker> speakers = Speaker.find("ORDER BY lastName, firstName").fetch();
    	render(speakers);
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

    public static void sponsors(){
    	List<Sponsor> sponsors = Sponsor.find("ORDER BY company").fetch();
    	render(sponsors);
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

}