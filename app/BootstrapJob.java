import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import models.Slot;
import models.Speaker;
import models.Sponsor;
import play.Logger;
import play.Play;
import play.Play.Mode;
import play.db.jpa.Blob;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;


@OnApplicationStart
public class BootstrapJob extends Job {
    public void doJob() throws Exception {
    	if(Play.mode == Mode.DEV && Slot.count() == 0){
    		Logger.info("Loading test data");
    		Fixtures.load("test-data.yml");
    		updateSpeaker("Épardaud", "stef-epardaud.jpg");
    		updateSpeaker("Kloos", "egor-kloos.png");
    		updateSpeaker("Leroux", "nicolas-leroux.jpg");
    		updateSponsor("Lunatech Research", "lunatech.png");
    		updateSponsor("Amadeus", "amadeus.png");
    		updateSponsor("Avisto", "avisto.png");
    		updateSponsor("JBoss", "jboss.png");
    		updateSponsor("Stonesoft", "stonesoft.png");
    		updateSponsor("Oracle", "oracle.png");
    	}
    }

	private void updateSpeaker(String name, String file) throws FileNotFoundException {
		Speaker speaker = Speaker.find("byLastName", name).first();
		speaker.photo = new Blob();
		InputStream is = new FileInputStream("test-data-images/"+file);
		speaker.photo.set(is, file.endsWith("jpg") ? "image/jpg" : "image/png");
		speaker.save();
	}

	private void updateSponsor(String name, String file) throws FileNotFoundException {
		Sponsor sponsor = Sponsor.find("byCompany", name).first();
		sponsor.logo = new Blob();
		InputStream is = new FileInputStream("test-data-images/"+file);
		sponsor.logo.set(is, file.endsWith("jpg") ? "image/jpg" : "image/png");
		sponsor.save();
	}
}
