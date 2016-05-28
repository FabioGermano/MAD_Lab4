package it.polito.mad_lab4.data.user;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.data.reservation.Reservation;

/**
 * Created by f.germano on 12/04/2016.
 */
public class User implements Serializable{

    // tutto il necessario per visualizzare le informazioni nel menu a tendina
    // e per ricavare tutte le altre informazioni necessarie
    private String name;
    private String userType; // manager or client
    private UserLoginInformation userLoginInfo;

   /* public User(String name, String type, UserLoginInformation loginInfo)
    {
        this.name = name;
        this.userType = type;
        this.userLoginInfo = loginInfo;
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

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String type) {
        this.userType = type;
    }
    */


    /// da rimuovere ///
    private String phone;
    private String photo_path;
    private int userId;
    private ArrayList<UserPhotoLike> userPhotoLikes; //verranno poi prese dinamicamente dal server
    private ArrayList<Reservation> reservations; //verranno poi prese dinamicamente dal server




    public User(String name, String phone, int userId)
    {
        this.name = name;
        this.userLoginInfo = new UserLoginInformation();


        this.reservations = new ArrayList<Reservation>();
        this.userPhotoLikes = new ArrayList<UserPhotoLike>();
        this.photo_path = null;
        this.phone = phone;
        this.userId = userId;
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
