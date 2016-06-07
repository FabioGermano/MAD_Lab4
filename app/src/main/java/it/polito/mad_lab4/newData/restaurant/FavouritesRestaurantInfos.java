package it.polito.mad_lab4.newData.restaurant;

/**
 * Created by Giovanna on 06/06/2016.
 */
public class FavouritesRestaurantInfos {

    private String restaurantId;
    private String logoLink;
    private String name;
    private String city;
    private String address;
    private float totRanking;
    private float numRanking;

    public FavouritesRestaurantInfos() {
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public void setLogoLink(String logoLink) {
        this.logoLink = logoLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getTotRanking() {
        return totRanking;
    }

    public void setTotRanking(float totRanking) {
        this.totRanking = totRanking;
    }

    public float getNumRanking() {
        return numRanking;
    }

    public void setNumRanking(float numRanking) {
        this.numRanking = numRanking;
    }
}
