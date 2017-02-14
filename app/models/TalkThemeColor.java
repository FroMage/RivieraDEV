package models;

public enum TalkThemeColor {
	Blue	("blue"), 
	Yellow  ("yellow"),
	Purple	("purple"), 
	Green   ("green"), 
	Orange  ("orange"),
	Red		("red");

	private final String code;

	TalkThemeColor(String code) {
		this.code = code;
	}

	public String getCode() { return this.code; }
}
