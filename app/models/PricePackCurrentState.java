package models;

public class PricePackCurrentState {
    
    public PricePackType type;

    public Integer currentPrice;

    public Integer maxPrice;

    public Integer studentPrice;

    public PricePackCurrentState(PricePackType type, Integer currentPrice, Integer maxPrice, Integer studentPrice) {
        this.type = type;
        this.currentPrice = currentPrice;
        this.maxPrice = maxPrice;
        this.studentPrice = studentPrice;
    }

}