package controllers;

import java.util.List;

import models.Sponsor;
import models.Talk;
import play.mvc.Controller;

public class APIV1 extends Controller {
	public static void talks(){
		List<Talk> talks = Talk.all().fetch();
		render("/APIV1/talks.json", talks);
	}

	public static void sponsors(){
		List<Sponsor> sponsors = Sponsor.all().fetch();
		render("/APIV1/sponsors.json", sponsors);
	}

	public static void general(){
		render("/APIV1/general.json");
	}
}
