package it.polito.mad_lab4.newData.reservation;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.data.reservation.ReservedDish;
import it.polito.mad_lab4.data.user.User;

/**
 * Created by f.germano on 12/04/2016.
 */
public class Reservation {

    private String reservationId; // not mapped in firebase
    private String restaurantId;
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

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
