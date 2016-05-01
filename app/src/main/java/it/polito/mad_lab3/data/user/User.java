package it.polito.mad_lab3.data.user;

import java.util.ArrayList;

import it.polito.mad_lab3.data.reservation.Reservation;

/**
 * Created by f.germano on 12/04/2016.
 */
public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private ArrayList<Reservation> reservations;
    private String name;
    private String phone;
    private int userId;

    public ArrayList<UserPhotoLike> getUserPhotoLikes() {
        return userPhotoLikes;
    }

    public void setUserPhotoLikes(ArrayList<UserPhotoLike> userPhotoLikes) {
        this.userPhotoLikes = userPhotoLikes;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    private ArrayList<UserPhotoLike> userPhotoLikes;

    public User(String name, String phone, int userId)
    {
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.reservations = new ArrayList<Reservation>();
        this.userPhotoLikes = new ArrayList<UserPhotoLike>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }
}
