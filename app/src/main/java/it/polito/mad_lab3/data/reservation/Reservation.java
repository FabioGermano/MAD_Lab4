package it.polito.mad_lab3.data.reservation;

import java.util.ArrayList;

import it.polito.mad_lab3.data.user.User;

/**
 * Created by f.germano on 12/04/2016.
 */
public class Reservation {

    private int reservationId;
    private User user;
    private String date;
    private String time;
    private String status;
    private String places; // an integer, actually
    private ArrayList<ReservedDish> reservedDishes;
    private float totalIncome;
    private String noteByUser;
    private String noteByOwner;
    private boolean isExpired;
    private boolean isVerified;

    public Reservation()
    {
        reservedDishes = null;
    }

    public void initializeReservedDishes()
    {
        this.reservedDishes = new ArrayList<ReservedDish>();
    }

    public void addReservedDish(ReservedDish reservedDish)
    {
        reservedDishes.add(reservedDish);
    }

    public int getReservationId() {
        return reservationId;
    }

    public User getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

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

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getPlaces() {
        return places;
    }

    public ArrayList<ReservedDish> getReservedDishes() {
        return reservedDishes;
    }

    public ArrayList<ReservedDish> getReservedDishes(boolean onlyOffer) {
        ArrayList<ReservedDish> reservedDishes = new ArrayList<ReservedDish>();

        for(ReservedDish rd : this.reservedDishes){
            if(rd.isOffer() == onlyOffer){
                reservedDishes.add(rd);
            }
        }

        return reservedDishes;
    }

    public String getNoteByUser() {
        return noteByUser;
    }

    public String getNoteByOwner() {
        return noteByOwner;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setReservedDishes(ArrayList<ReservedDish> reservedDishes) {
        this.reservedDishes = reservedDishes;
    }

    public void setNoteByUser(String noteByUser) {
        this.noteByUser = noteByUser;
    }

    public void setNoteByOwner(String noteByOwner) {
        this.noteByOwner = noteByOwner;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public float getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(float totalIncome) {
        this.totalIncome = totalIncome;
    }
}
