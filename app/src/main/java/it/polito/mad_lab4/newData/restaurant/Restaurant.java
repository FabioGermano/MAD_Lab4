package it.polito.mad_lab4.newData.restaurant;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

import it.polito.mad_lab4.data.restaurant.BasicInfo;
import it.polito.mad_lab4.data.restaurant.Cover;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.restaurant.Review;
import it.polito.mad_lab4.data.restaurant.UserPhoto;

/**
 * Created by f.germano on 25/04/2016.
 */
public class Restaurant {

    private String address;
    private String city;
    private String phone;
    private String logoThumbDownloadLink;

    // BLEEEE
    private String cover1_thumbDownloadLink, cover1_largeDownloadLink;
    private String cover2_thumbDownloadLink, cover2_largeDownloadLink;
    private String cover3_thumbDownloadLink, cover3_largeDownloadLink;
    private String cover4_thumbDownloadLink, cover4_largeDownloadLink;
    // FINE BLEEEE

    private ArrayList<String> timeTable; // eg item "11:30 - 15:30" ; string "CLOSED" if the restaurant is closed on that day; index -> day of week
    private String email;
    private String description;
    private boolean wifi, reservations, seatsOutside, parking, music, creditCard, bancomat;
    private boolean isTakeAway, isOnPlace;
    private int distance;
    private int numReviews;
    private float totRanking;
    private int numDishesAndOffers, totDishesAndOffers;
    private String restaurantName;
    private String restaurantId;

    public Restaurant(){

    }

    @Exclude
    public float getRanking(){
        if(numReviews == 0){
            return 0;
        }
        return totRanking/numReviews;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(ArrayList<String> timeTable) {
        this.timeTable = timeTable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isReservations() {
        return reservations;
    }

    public void setReservations(boolean reservations) {
        this.reservations = reservations;
    }

    public boolean isSeatsOutside() {
        return seatsOutside;
    }

    public void setSeatsOutside(boolean seatsOutside) {
        this.seatsOutside = seatsOutside;
    }

    public boolean isParking() {
        return parking;
    }

    public void setParking(boolean parking) {
        this.parking = parking;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isCreditCard() {
        return creditCard;
    }

    public void setCreditCard(boolean creditCard) {
        this.creditCard = creditCard;
    }

    public boolean isBancomat() {
        return bancomat;
    }

    public void setBancomat(boolean bancomat) {
        this.bancomat = bancomat;
    }

    public boolean isTakeAway() {
        return isTakeAway;
    }

    public void setTakeAway(boolean takeAway) {
        isTakeAway = takeAway;
    }

    public boolean isOnPlace() {
        return isOnPlace;
    }

    public void setOnPlace(boolean onPlace) {
        isOnPlace = onPlace;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public float getTotRanking() {
        return totRanking;
    }

    public int getNumDishesAndOffers() {
        return numDishesAndOffers;
    }

    public void setNumDishesAndOffers(int numDishesAndOffers) {
        this.numDishesAndOffers = numDishesAndOffers;
    }

    public int getTotDishesAndOffers() {
        return totDishesAndOffers;
    }

    public void setTotDishesAndOffers(int totDishesAndOffers) {
        this.totDishesAndOffers = totDishesAndOffers;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setTotRanking(float totRanking) {
        this.totRanking = totRanking;
    }

    public String getLogoThumbDownloadLink() {
        return logoThumbDownloadLink;
    }

    public void setLogoThumbDownloadLink(String logoThumbDownloadLink) {
        this.logoThumbDownloadLink = logoThumbDownloadLink;
    }

    public String getCover4_largeDownloadLink() {
        return cover4_largeDownloadLink;
    }

    public void setCover4_largeDownloadLink(String cover4_largeDownloadLink) {
        this.cover4_largeDownloadLink = cover4_largeDownloadLink;
    }

    public String getCover4_thumbDownloadLink() {
        return cover4_thumbDownloadLink;
    }

    public void setCover4_thumbDownloadLink(String cover4_thumbDownloadLink) {
        this.cover4_thumbDownloadLink = cover4_thumbDownloadLink;
    }

    public String getCover1_thumbDownloadLink() {
        return cover1_thumbDownloadLink;
    }

    public void setCover1_thumbDownloadLink(String cover1_thumbDownloadLink) {
        this.cover1_thumbDownloadLink = cover1_thumbDownloadLink;
    }

    public String getCover1_largeDownloadLink() {
        return cover1_largeDownloadLink;
    }

    public void setCover1_largeDownloadLink(String cover1_largeDownloadLink) {
        this.cover1_largeDownloadLink = cover1_largeDownloadLink;
    }

    public String getCover2_thumbDownloadLink() {
        return cover2_thumbDownloadLink;
    }

    public void setCover2_thumbDownloadLink(String cover2_thumbDownloadLink) {
        this.cover2_thumbDownloadLink = cover2_thumbDownloadLink;
    }

    public String getCover2_largeDownloadLink() {
        return cover2_largeDownloadLink;
    }

    public void setCover2_largeDownloadLink(String cover2_largeDownloadLink) {
        this.cover2_largeDownloadLink = cover2_largeDownloadLink;
    }

    public String getCover3_thumbDownloadLink() {
        return cover3_thumbDownloadLink;
    }

    public void setCover3_thumbDownloadLink(String cover3_thumbDownloadLink) {
        this.cover3_thumbDownloadLink = cover3_thumbDownloadLink;
    }

    public String getCover3_largeDownloadLink() {
        return cover3_largeDownloadLink;
    }

    public void setCover3_largeDownloadLink(String cover3_largeDownloadLink) {
        this.cover3_largeDownloadLink = cover3_largeDownloadLink;
    }

    public void setLargeCoverByIndex(int i, String downloadLinkLarge) {
        switch (i){
            case 0:
                this.cover1_largeDownloadLink = downloadLinkLarge;
                break;
            case 1:
                this.cover2_largeDownloadLink = downloadLinkLarge;
                break;
            case 2:
                this.cover3_largeDownloadLink = downloadLinkLarge;
                break;
            case 3:
                this.cover4_largeDownloadLink = downloadLinkLarge;
                break;
        }
    }

    public void setThumbCoverByIndex(int i, String downloadLinkThumb) {
        switch (i){
            case 0:
                this.cover1_thumbDownloadLink = downloadLinkThumb;
                break;
            case 1:
                this.cover2_thumbDownloadLink = downloadLinkThumb;
                break;
            case 2:
                this.cover3_thumbDownloadLink = downloadLinkThumb;
                break;
            case 3:
                this.cover4_thumbDownloadLink = downloadLinkThumb;
                break;
        }
    }
}
