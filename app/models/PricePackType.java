package models;

public enum PricePackType {
    DEEP_DIVE (1, "deepdive"),
    CONF      (2, "conference"),
    COMBI     (3, "combi");

    private final int order;
    private final String code;

    PricePackType(int order, String code) {
        this.order = order;
        this.code = code;
    }

    public int getOrder() { return this.order; }
    public String getCode() { return this.code; }
}
