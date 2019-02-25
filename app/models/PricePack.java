package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class PricePack extends Model implements Comparable<PricePack> {

    @Required
    @Enumerated(EnumType.STRING)
    public PricePackType type;

    @Required
    @MaxSize(3)
    public Integer blindBirdPrice;

    @Required
    @MaxSize(3)
    public Integer earlyBirdPrice;

    @Required
    @MaxSize(3)
    public Integer regularPrice;

    @Required
    @MaxSize(3)
    public Integer studentPrice;

    @Override
    public String toString() {
      return type.toString() 
      + " (" 
      + studentPrice + "€ - " 
      + blindBirdPrice + "€ - " 
      + earlyBirdPrice + "€ - " 
      + regularPrice + "€" 
      + ")";
    }

    public int compareTo(PricePack other) {
      return type.getOrder() - other.type.getOrder();
    }
}