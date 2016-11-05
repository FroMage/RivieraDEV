package models;

public enum SponsorShip {
	Gold			("or", "Or"), 
	Silver			("argent", "Argent"), 
	Bronze			("bronze", "Bronze"), 
	Lanyard			("lanyard", "Tour de cou"), 
	Buffet			("buffet", "Buffet"), 
	Apero			("apero", "Apéritif"), 
	Breakfast		("breakfast", "Petit déjeuner"), 
	PreviousYears	("previousYears", "Années précédentes");

	private final String code;
    private final String title;

	SponsorShip(String code, String title) {
		this.code = code;
		this.title = title;
	}

	public String getCode() { return this.code; }
	public String getTitle() { return this.title; }
}
