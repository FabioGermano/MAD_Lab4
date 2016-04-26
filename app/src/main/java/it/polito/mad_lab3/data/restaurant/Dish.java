package it.polito.mad_lab3.data.restaurant;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Dish {
    private String dishName;
    private float avgRank;
    private int numRanks;
    private float price;
    private String thumbPath, largePath;
    private String type; // primi, secondi...

    public Dish(String dishName, float avgRank, int numRanks, float price, String thumbPath, String largePath, String type) {
        this.dishName = dishName;
        this.avgRank = avgRank;
        this.numRanks = numRanks;
        this.price = price;
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.type = type;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
