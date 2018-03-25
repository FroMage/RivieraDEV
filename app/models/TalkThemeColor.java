package models;

public enum TalkThemeColor {
	Blue		("blue"), 
	Yellow		("yellow"),
	Purple		("purple"), 
	Green		("green"), 
	Orange		("orange"),
	Red			("red"),
	Pink		("pink"),
	Turquoise	("turquoise"),
	Brown		("brown"),
	Grey		("grey");

	private final String code;

	TalkThemeColor(String code) {
		this.code = code;
	}

	public String getCode() { return this.code; }
}
