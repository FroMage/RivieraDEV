package models;

public enum Level {
	Beginner		("beginner"), 
	Intermediate    ("intermediate"),
	Advanced		("advanced");

	private final String code;

	Level(String code) {
		this.code = code;
	}

	public String getCode() { return this.code; }
}
