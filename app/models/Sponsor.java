package models;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import play.Logger;
import play.data.binding.ParamNode;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Blob;
import play.db.jpa.GenericModel;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class Sponsor extends Model implements Comparable<Sponsor> {
	@Required
	public String company;
	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@Required
	@MaxSize(10000)
	public String about;
	@Type(type="org.hibernate.type.StringClobType")
	@Lob
	@Required
	@MaxSize(10000)
	public String aboutEN;
	@URL
	public String companyURL;
	public String twitterAccount;

	@Enumerated(EnumType.STRING)
	public SponsorShip level;
	
	public Blob logo;
	
	// set on save/create
	public Integer width, height;
	
	@Override
	public void _save() {
		updateImageSizes();
		super._save();
	}
	
	private void updateImageSizes() {
		if(logo != null){
			try {
				BufferedImage image = ImageIO.read(logo.getFile());
				this.width = image.getWidth();
				this.height = image.getHeight();
			} catch (IOException e) {
				Logger.error(e, "Failed to read image");
			}
		}
	}

	@Override
	public String toString() {
		return company;
	}
	
	@Override
	public int compareTo(Sponsor other) {
		int ret = level.compareTo(other.level);
		if(ret != 0)
			return ret;
		return company.compareTo(other.company);
	}
	
	public int getWidth(int surface, int maxSide){
		if(width == null)
			return 0;
		int biggerSide = Math.max(width, height);
		float sizeFactor = (float)maxSide / biggerSide;
		float area = width * height * sizeFactor * sizeFactor;
		float areaFactor = Math.min(1, (float)surface / area);
		return (int)Math.floor(width * sizeFactor * areaFactor);
	}

	public int getHeight(int surface, int maxSide){
		if(height == null)
			return 0;
		int biggerSide = Math.max(width, height);
		float sizeFactor = (float)maxSide / biggerSide;
		float area = width * height * sizeFactor * sizeFactor;
		float areaFactor = Math.min(1, (float)surface / area);
		return (int)Math.floor(height * sizeFactor * areaFactor);
	}
}
