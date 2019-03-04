package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import models.BreakType;
import models.Language;
import models.Level;
import models.Speaker;
import models.Talk;
import models.TalkTheme;
import models.TalkThemeColor;
import models.TalkType;
import play.Logger;
import play.db.jpa.Blob;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;
import play.mvc.With;
import util.ImageUtil;
import util.JavaExtensions;

@With(Secure.class)
public class Admin extends Controller {
    
    public static class ProgramUpload {
        public String name;
        public List<JsonCategory> categories;
        public List<JsonFormat> formats;
        public List<JsonTalk> talks;
        public List<JsonSpeaker> speakers;
    }
    
    public static class JsonCategory {
        public String id;
        public String name;
    }

    public static class JsonFormat {
        public String id;
        public String name;
        public String description;
    }

    public static class JsonTalk {
        public String title;
        public String state;
        public String level;
        @SerializedName("abstract")
        public String abstractField;
        public String categories;
        public String formats;
        public String[] speakers;
        public String comments;
        public String references;
        public double rating;
        public int loves;
        public int hates;
    }

    public static class JsonSpeaker {
        public String uid;
        public String displayName;
        public String bio;
        public String companie;
        public String photoURL;
        public String twitter;
        public String github;
        public String city;
        public String language;
        public String email;
        public String phone;
    }

    public static class StringOrListDeserializer implements JsonDeserializer<String[]> {

        @Override
        public String[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json instanceof JsonArray) {
                return new Gson().fromJson(json, String[].class);
            }
            String child = context.deserialize(json, String.class);
            return new String[] { child };
        }
    }

    public static void uploadProgramForm() {
        render();
    }
    
    public static void uploadProgram(File program) throws FileNotFoundException, IOException {
        flash("message", "Uploaded stuff");
        
        try(FileReader reader = new FileReader(program)){
            GsonBuilder builder = new GsonBuilder().registerTypeAdapter(String[].class, new StringOrListDeserializer());
            ProgramUpload json = builder.create().fromJson(reader, ProgramUpload.class);
            
            Map<String,Speaker> jsonSpeakerIds = new HashMap<>();
            Map<String,TalkTheme> jsonCategoryIds = new HashMap<>();
            Map<String,TalkType> jsonFormatIds = new HashMap<>();

            int color = 0;
            TalkThemeColor[] colors = TalkThemeColor.values();

            Logger.info("Importing %s categories", json.categories.size());
            for (JsonCategory jsonCategory : json.categories) {
                Logger.info(" Category %s", jsonCategory.name);

                TalkTheme talkTheme = TalkTheme.find("importId", jsonCategory.id).first();
                if(talkTheme == null) {
                    talkTheme = new TalkTheme();
                    talkTheme.theme = jsonCategory.name;
                    talkTheme.color = colors[color % colors.length];
                    talkTheme.importId = jsonCategory.id;
                    talkTheme.save();
                }
                
                jsonCategoryIds.put(jsonCategory.id, talkTheme);
                color++;
            }

            Logger.info("Importing %s formats", json.formats.size());
            for (JsonFormat jsonFormat : json.formats) {
                Logger.info(" Format %s", jsonFormat.name);
                TalkType talkType = TalkType.find("importId", jsonFormat.id).first();
                if(talkType == null) {
                    talkType = new TalkType();
                    talkType.typeEN = jsonFormat.name;
                    talkType.importId = jsonFormat.id;
                    talkType.save();
                }
                
                jsonFormatIds.put(jsonFormat.id, talkType);
            }

            Logger.info("Importing %s speakers", json.speakers.size());
            for (JsonSpeaker jsonSpeaker : json.speakers) {
                Logger.info(" Speaker %s", jsonSpeaker.displayName);
                Speaker speaker = Speaker.find("importId", jsonSpeaker.uid).first();
                if(speaker == null) {

                    speaker = new Speaker();
                    speaker.biography = jsonSpeaker.bio;
                    speaker.company = jsonSpeaker.companie;
                    speaker.email = jsonSpeaker.email;
                    speaker.importId = jsonSpeaker.uid;
                    if(jsonSpeaker.displayName == null) {
                        speaker.firstName = "Anonymous";
                        speaker.lastName = "Coward";
                    } else {
                        int firstSpace = jsonSpeaker.displayName.indexOf(' ');
                        if(firstSpace != -1) {
                            speaker.firstName = jsonSpeaker.displayName.substring(0, firstSpace);
                            speaker.lastName = jsonSpeaker.displayName.substring(firstSpace+1);
                        } else {
                            speaker.lastName = jsonSpeaker.displayName;
                        }
                    }
                    if(jsonSpeaker.twitter != null) {
                        if(jsonSpeaker.twitter.startsWith("@"))
                            speaker.twitterAccount = jsonSpeaker.twitter.substring(1);
                        else if(jsonSpeaker.twitter.startsWith("https://twitter.com"))
                            speaker.twitterAccount = jsonSpeaker.twitter.substring(19);
                        else
                            speaker.twitterAccount = jsonSpeaker.twitter;
                    }
                    speaker.phone = jsonSpeaker.phone;

                    // FIXME: add language, github?
                    HttpResponse photo = WS.url(jsonSpeaker.photoURL).get();
                    if(photo.getStatus() == HttpURLConnection.HTTP_OK){
                        try(InputStream is = photo.getStream()){
                            BufferedImage image = ImageIO.read(is);
                            BufferedImage scaledImage = ImageUtil.scaleImage(image, 400);
                            File tmpFile = File.createTempFile("thumbnail", ".jpg");
                            try {
                                ImageUtil.writeImage(scaledImage, new FileImageOutputStream(tmpFile));
                                speaker.photo = new Blob();
                                // this closes the IS
                                speaker.photo.set(new FileInputStream(tmpFile), "image/jpeg");
                            }finally {
                                tmpFile.delete();
                            }
                        }
                    }
                    speaker.save();
                }                
                jsonSpeakerIds.put(jsonSpeaker.uid, speaker);
            }
            
            Logger.info("Importing %s talks", json.talks.size());
            for (JsonTalk jsonTalk : json.talks) {
                Logger.info(" Talk %s", jsonTalk.title);
                Talk talk = Talk.find("importId", jsonTalk.title).first();
                if(talk == null) {
                    talk = new Talk();
                    talk.descriptionEN = jsonTalk.abstractField;
                    // bullshit
                    talk.importId = jsonTalk.title;
                    if(jsonTalk.level == null)
                        talk.level = Level.Beginner;
                    else
                        talk.level = Level.valueOf(JavaExtensions.capFirst(jsonTalk.level));
                    talk.titleEN = jsonTalk.title;
                    for (String jsonSpeakerId : jsonTalk.speakers) {
                        talk.speakers.add(jsonSpeakerIds.get(jsonSpeakerId));
                    }
                    talk.isBreak = BreakType.NotABreak;
                    // FIXME: state?
                    talk.type = jsonFormatIds.get(jsonTalk.formats);
                    talk.theme = jsonCategoryIds.get(jsonTalk.categories);
                    // FIXME:
                    talk.language = Language.FR;
                }
                
                talk.save();
            }
        }
        
        uploadProgramForm();
    }
    

}
