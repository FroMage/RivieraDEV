package models;

public enum PricePackPeriod {
    BLIND_BIRD  ("blindBird"),
    EARLY_BIRD  ("earlyBird"),
    REGULAR     ("regular");

    private final String code;

    PricePackPeriod(String code) {
        this.code = code;
    }

    public String getCode() { return this.code; }
}
