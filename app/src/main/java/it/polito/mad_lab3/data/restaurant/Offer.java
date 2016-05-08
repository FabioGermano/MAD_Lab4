package it.polito.mad_lab3.data.restaurant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Offer implements Serializable{

    private String offerName;
    private float sumRank;
    private int numRanks;
    private float price;
    private String thumbPath, largePath;

    private ArrayList<Boolean> availableOn;

    public String getResPhoto() {
        return resPhoto;
    }

    public void setResPhoto(String resPhoto) {
        this.resPhoto = resPhoto;
    }

    private String resPhoto; // only in this prototic version of the app
    private String details;

    public Offer(String offerName, float sumRank, int numRanks, float price, String thumbPath, String largePath, String details) {
        this.offerName = offerName;
        this.sumRank = sumRank;
        this.numRanks = numRanks;
        this.price = price;
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.details = details;
        this.availableOn= new ArrayList<>();
        for(int i=0; i<7; i++)
            this.availableOn.add(false);
    }

    public ArrayList<Boolean> getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(ArrayList<Boolean> availableOn) {
        this.availableOn = availableOn;
    }


    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public float getAvgRank() {
        return sumRank/numRanks;
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
