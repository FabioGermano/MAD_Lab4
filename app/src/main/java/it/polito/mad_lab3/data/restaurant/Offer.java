package it.polito.mad_lab3.data.restaurant;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Offer {

    private String offerName;
    private float avgRank;
    private int numRanks;
    private float price;
    private String thumbPath, largePath;
    private String details;

    public Offer(String offerName, float avgRank, int numRanks, float price, String thumbPath, String largePath, String details) {
        this.offerName = offerName;
        this.avgRank = avgRank;
        this.numRanks = numRanks;
        this.price = price;
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.details = details;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public float getAvgRank() {
        return avgRank;
    }

    public void setAvgRank(float avgRank) {
        this.avgRank = avgRank;
    }

    public int getNumRanks() {
        return numRanks;
    }

    public void setNumRanks(int numRanks) {
        this.numRanks = numRanks;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getLargePath() {
        return largePath;
    }

    public void setLargePath(String largePath) {
        this.largePath = largePath;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
