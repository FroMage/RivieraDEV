package controllers;

import java.util.Date;
import java.util.List;

import models.Slot;
import models.Speaker;
import models.Sponsor;
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
        render();
    }

    public static void about() {
        render();
    }
    
    public static void schedule(){
    	List<Date> days = Slot.find("select distinct date_trunc('day', startDate) from Slot ORDER BY date_trunc('day', startDate)").fetch();
    	List<Track> tracks = Track.findAll();
    	render(days, tracks);
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
    	render(speaker);
    }

    public static void talk(Long id){
    	Talk talk = Talk.findById(id);
    	render(talk);
    }

    public static void sponsors(){
    	List<Sponsor> sponsors = Sponsor.find("ORDER BY company").fetch();
    	render(sponsors);
    }
    
    public static void speakerPhoto(Long id){
    	Speaker speaker = Speaker.findById(id);
    	response.contentType = speaker.photo.type();
    	renderBinary(speaker.photo.get());
    }

    public static void sponsorLogo(Long id){
    	Sponsor sponsor = Sponsor.findById(id);
    	response.contentType = sponsor.logo.type();
    	renderBinary(sponsor.logo.get());
    }
}