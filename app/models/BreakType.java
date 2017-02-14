package models;

public enum BreakType {
	NotABreak		("notABreak"), 
	CofeeBreak		("cofeeBreak"),
	Breakfast		("breakfast"), 
	Lunch			("lunch"), 
	Party			("party");

	private final String code;

	BreakType(String code) {
		this.code = code;
	}

	public String getCode() { return this.code; }
}
