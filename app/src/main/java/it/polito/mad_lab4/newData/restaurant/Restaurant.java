package it.polito.mad_lab4.newData.restaurant;

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
    private ArrayList<String> timeTable; // eg item "11:30 - 15:30" ; string "CLOSED" if the restaurant is closed on that day; index -> day of week
    private String email;
    private String description;
    private boolean wifi, reservations, seatsOutside, parking, music, creditCard, bancomat;
    private boolean isTakeAway, isOnPlace;
    private int distance;
    private int numReviews, totRanking;
    private int numDishesAndOffers, totDishesAndOffers;
    private String restaurantName;
    private String restaurantId;

    public Restaurant(){

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

    public int getTotRanking() {
        return totRanking;
    }

    public void setTotRanking(int totRanking) {
        this.totRanking = totRanking;
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
}
