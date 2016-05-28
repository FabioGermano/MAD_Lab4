package it.polito.mad_lab4.newData.restaurant;

import java.io.Serializable;

import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;

/**
 * Created by f.germano on 20/05/2016.
 */
public class Dish {
    private String dishName;
    private float sumRank;
    private String dishId; // not mapped in firebase
    private int numRanks;
    private float price;
    private Boolean isTodayAvailable;
    private String type; // primi, secondi...
    private String thumbDownloadLink;
    private String largeDownloadLink;

    public Dish(){
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public float getSumRank() {
        return sumRank;
    }

    public void setSumRank(float sumRank) {
        this.sumRank = sumRank;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
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

    public Boolean getTodayAvailable() {
        return isTodayAvailable;
    }

    public void setTodayAvailable(Boolean todayAvailable) {
        isTodayAvailable = todayAvailable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setThumbDownloadLink(String downloadLinkThumb) {
        this.thumbDownloadLink = downloadLinkThumb;
    }

    public void setLargeDownloadLink(String downloadLinkLarge) {
        this.largeDownloadLink = downloadLinkLarge;
    }

    public String getThumbDownloadLink() {
        return thumbDownloadLink;
    }

    public String getLargeDownloadLink() {
        return largeDownloadLink;
    }
}
