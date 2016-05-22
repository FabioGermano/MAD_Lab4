package it.polito.mad_lab4.data.restaurant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Offer implements Serializable{

    private String offerName;
    private int offerId;
    private float sumRank;
    private int numRanks;
    private float price;
    private String thumbPath, largePath;

    private ArrayList<Boolean> availableOn;
    private boolean isAvailable;

    public Offer(String offerName, int offerId, float sumRank, int numRanks, float price, String thumbPath, String largePath, ArrayList<Boolean> availableOn, String resPhoto, String details) {
        this.offerName = offerName;
        this.offerId = offerId;
        this.sumRank = sumRank;
        this.numRanks = numRanks;
        this.price = price;
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.availableOn = availableOn;
        this.resPhoto = resPhoto;
        this.details = details;
    }

    public Offer(){

    }

    public String getResPhoto() {
        return resPhoto;
    }

    public void setResPhoto(String resPhoto) {
        this.resPhoto = resPhoto;
    }

    private String resPhoto; // only in this prototic version of the app
    private String details;


    public ArrayList<Boolean> getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(ArrayList<Boolean> availableOn) {
        this.availableOn = availableOn;
    }

    public void setAvailableOn(boolean[] days) {
        if(availableOn == null){
            availableOn = new ArrayList<Boolean>();
        }
        this.availableOn.add(days[0]);
        this.availableOn.add(days[1]);
        this.availableOn.add(days[2]);
        this.availableOn.add(days[3]);
        this.availableOn.add(days[4]);
        this.availableOn.add(days[5]);
        this.availableOn.add(days[6]);
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


    public float getSumRank() {
        return sumRank;
    }

    public void setSumRank(float sumRank) {
        this.sumRank = sumRank;
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

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean checked) {
        this.isAvailable = checked;
    }
}
