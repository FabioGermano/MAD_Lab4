package it.polito.mad_lab4.data.user;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.data.reservation.Reservation;

/**
 * Created by f.germano on 12/04/2016.
 */
public class User implements Serializable{
    private ArrayList<Reservation> reservations; //verranno poi prese dinamicamente dal server
    private String name;
    private String phone;
    private String photo_path;
    private int userId;
    private ArrayList<UserPhotoLike> userPhotoLikes; //verranno poi prese dinamicamente dal server

    private UserLoginInformation userLoginInfo;

    public User(String name, String phone, int userId)
    {
        this.name = name;
        this.phone = phone;
        this.userId = userId;
        this.reservations = new ArrayList<Reservation>();
        this.userPhotoLikes = new ArrayList<UserPhotoLike>();
        this.userLoginInfo = new UserLoginInformation();
        this.photo_path = null;
    }

    public void setPhoto_path(String path){
        this.photo_path = path;
    }

    public String getPhoto_path(){
        return this.photo_path;
    }

    public void setUserLoginInfo(UserLoginInformation loginInfo){
        this.userLoginInfo = loginInfo;
    }

    public UserLoginInformation getUserLoginInfo(){
        return this.userLoginInfo;
    }

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }
}
