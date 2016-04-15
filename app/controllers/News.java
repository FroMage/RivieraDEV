package controllers;

import play.mvc.With;
import controllers.CRUD.For;

@For(models.News.class)
@With(Secure.class)
public class News extends CRUD {

}
