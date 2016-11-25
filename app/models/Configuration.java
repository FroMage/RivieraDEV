package models;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.Lob;

import play.db.jpa.Model;

@Entity
public class Configuration extends Model {

	@Enumerated(EnumType.STRING)
	public ConfigurationKey key;

	public String value;

	@Override
	public String toString() {
		return this.key + " : " + this.value;
	}
}
