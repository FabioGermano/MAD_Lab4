package it.polito.mad_lab4.newData.restaurant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by f.germano on 20/05/2016.
 */
public class Offer {

    private String offerName;
    private String offerId; //not mapped in firebase
    private float sumRank;
    private int numRanks;
    private float price;
    private String details;
    private ArrayList<Boolean> availableOn;
    private Boolean isTodayAvailable;

    public Offer(){
    }

    public Boolean getTodayAvailable() {
        return isTodayAvailable;
    }

    public void setTodayAvailable(Boolean todayAvailable) {
        isTodayAvailable = todayAvailable;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ArrayList<Boolean> getAvailableOn() {
        return availableOn;
    }

    public void setAvailableOn(ArrayList<Boolean> availableOn) {
        this.availableOn = availableOn;
    }
}
