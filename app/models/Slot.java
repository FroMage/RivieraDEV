package models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@SuppressWarnings("serial")
@Entity
public class Slot extends Model {

    @Field("startDate")
    @Required
    public Date startDate;

    @Field("endDate")
    @Required
    public Date endDate;

    @OneToMany(mappedBy = "slot")
    public List<Talk> talks = new ArrayList<Talk>();

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        StringBuffer strbuf = new StringBuffer();

        strbuf.append(dateFormat.format(startDate)).append(" [").append(timeFormat.format(startDate)).append(" - ")
                .append(timeFormat.format(endDate)).append("]");
        /*
         * strbuf.append(" ("); boolean first = true; for(Talk talk : talks){ if(!first)
         * { strbuf.append(", "); } first = false; if(talk.track != null) {
         * strbuf.append(talk.track); } } strbuf.append(")");
         */
        return strbuf.toString();
    }

    public static List<Slot> findPerDay(Date day) {
        List<Slot> slots = find("date_trunc('day', startDate) = ? ORDER BY startDate, endDate", day).fetch();
        // En cas de slots déjà contenu dans 1 slots, ne renvoyer que le plus grand ?
        return slots;
    }

    public static List<Slot> findMultiPerDay(Date day) {
        List<Slot> slots = find("date_trunc('day', startDate) = ? ORDER BY startDate, endDate", day).fetch();
        Map<Date, Slot> multiSlots = new HashMap();
        for (Slot slot : slots) {
            Slot existingSlot = multiSlots.get(slot.startDate);
            if (existingSlot != null) {
                // Compare each EndDate
                if (existingSlot.endDate.compareTo(slot.endDate) < 0) {
                    multiSlots.put(slot.startDate, slot);
                }
            } else {
                multiSlots.put(slot.startDate, slot);
            }
        }
        // En cas de slots déjà contenu dans 1 slots, ne renvoyer que le plus grand ?
        List<Slot> result = new ArrayList(multiSlots.values());

        // Sorting
        Collections.sort(result, new Comparator<Slot>() {
            @Override
            public int compare(Slot slotA, Slot slotB) {
                int compare = slotA.startDate.compareTo(slotB.startDate);
                if (compare == 0) {
                    return slotA.endDate.compareTo(slotB.endDate);
                }
                return compare;
            }
        });

        return result;
    }

    public Talk getTalkPerTrack(Track track) {
        for (Talk talk : talks) {
            if (talk.track == track) {
                return talk;
            }
        }
        return null;
    }

    public List<Talk> getTalksPerTrack(Track track) {
        // Récupérer tous les talks pour la track qui contient dans ce Slot et les slots
        // contenus
        List<Slot> slots = find("? <= startDate AND endDate <= ?", this.startDate, this.endDate).fetch();

        List<Talk> talks = new ArrayList<Talk>();
        for (Slot slot : slots) {
            for (Talk talk : slot.talks) {
                if (talk.track == track) {
                    talks.add(talk);
                }
            }
        }
        return talks;
    }

    public Talk getAllTracksEvent() {
        return talks.size() == 1 && talks.get(0).track == null ? talks.get(0) : null;
    }
}