package it.polito.mad_lab4.newData.reservation;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.newData.reservation.ReservedDish;
import it.polito.mad_lab4.data.user.User;

/**
 * Created by f.germano on 12/04/2016.
 */
public class Reservation {

    private String reservationId; // not mapped in firebase
    private String restaurantId;
    private String restaurantIdAndDate;
    private String userId;
    private String date;
    private String time;
    private String status;
    private String places; // an integer, actually
    private float totalIncome;
    private String noteByUser;
    private String noteByOwner;
    private boolean isExpired;
    private boolean isVerified;
    private ArrayList<ReservedDish> reservedDishes;
    private String address;
    private String restaurantName;
    private String userName;
    private String phone;
    private String avatarDownloadLink;

    public Reservation(){
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getNoteByUser() {
        return noteByUser;
    }

    public void setNoteByUser(String noteByUser) {
        this.noteByUser = noteByUser;
    }

    public String getNoteByOwner() {
        return noteByOwner;
    }

    public void setNoteByOwner(String noteByOwner) {
        this.noteByOwner = noteByOwner;
    }

    public boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        isVerified = verified;
    }

    public void setReservedDishes(ArrayList<ReservedDish> reservedDishes) {
        this.reservedDishes = reservedDishes;
    }

    public ArrayList<ReservedDish> getReservedDishes() {
        return reservedDishes;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantIdAndDate() {
        return restaurantIdAndDate;
    }

    public void setRestaurantIdAndDate(String restaurantIdAndDate) {
        this.restaurantIdAndDate = restaurantIdAndDate;
    }

    public ArrayList<ReservedDish> getReservedDishesByType(boolean onlyOffer) {
        ArrayList<ReservedDish> reservedDishes = new ArrayList<ReservedDish>();

        for(ReservedDish rd : this.reservedDishes){
            if(rd.getIsOffer() == onlyOffer){
                reservedDishes.add(rd);
            }
        }

        return reservedDishes;
    }

    @Exclude
    public String getType(){
        if(places!=null && (reservedDishes == null || reservedDishes.size()==0)){
            return "Table";
        }
        else if (places== null && reservedDishes.size()>0){
            return "Take-away";
        }
        else {
            return "Table with orders";
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAvatarDownloadLink(String avatarDownloadLink) {
        this.avatarDownloadLink = avatarDownloadLink;
    }

    public String getAvatarDownloadLink() {
        return avatarDownloadLink;
    }

    public String getPhone() {
        return phone;
    }
}
