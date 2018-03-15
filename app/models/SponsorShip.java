package models;

public enum SponsorShip {
	Platinum		("platinum"),
	Gold			("gold"), 
	Silver			("silver"), 
	Lunches			("lunches"), 
	Party			("party"),
	Partner			("partner"),
	PreviousYears	("previousYears");

	private final String code;

	SponsorShip(String code) {
		this.code = code;
	}

	public String getCode() { return this.code; }
}
