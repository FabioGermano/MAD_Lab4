package it.polito.mad_lab4.data.restaurant;

import java.io.Serializable;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Dish implements Serializable {
    private String dishName;
    private float sumRank;
    private int dishId;
    private int numRanks;
    private float price;
    private String thumbPath, largePath;
    private String type; // primi, secondi...
    private String resPhoto; // only in this prototic version of the app

    public Dish(String dishName, int dishId,float sumRank, int numRanks, float price, String thumbPath, String largePath, String type) {
        this.dishName = dishName;
        this.sumRank = sumRank;
        this.dishId= dishId;
        this.numRanks = numRanks;
        this.price = price;
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.type = type;
    }

    public String getResPhoto() {
        return this.resPhoto;
    }

    public void setResPhoto(String resPhoto) {
        this.resPhoto = resPhoto;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DishType getEnumType(){
        return DishTypeConverter.fromStringToEnum(this.type);
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public float getSumRank() {
        return sumRank;
    }

    public void setSumRank(float sumRank) {
        this.sumRank = sumRank;
    }
}
