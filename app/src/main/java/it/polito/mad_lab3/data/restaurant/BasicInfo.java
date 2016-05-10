package it.polito.mad_lab3.data.restaurant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by f.germano on 25/04/2016.
 */
public class BasicInfo implements Serializable{


    private String address;
    private String city;
    private String phone;
    private ArrayList<String> timeTable; // eg item "11:30 - 15:30" ; string "CLOSED" if the restaurant is closed on that day; index -> day of week
    private String email;
    private String description;
    private String logoThumb;
    private ArrayList<Cover> covers;
    private boolean wifi, reservations, seatsOutside, parking, music, creditCard, bancomat;
    private ArrayList<String> typesOfServices;
    private int distance;


    public BasicInfo(String address,String city, String phone,String email, String description, String logoThumb,  ArrayList<String> timeTable,ArrayList<String> typesOfServices){
        this.address = address;
        this.city=city;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.logoThumb = logoThumb;
        this.timeTable=timeTable;
        this.covers = new ArrayList<Cover>();
        this.wifi = false;
        this.reservations = false;
        this.seatsOutside = false;
        this.parking = false;
        this.music = false;
        this.creditCard = false;
        this.bancomat = false;
        this.typesOfServices=typesOfServices;
        this.distance = -1;
    }


    public ArrayList<String> getTypesOfServices() {
        return typesOfServices;
    }

    public void setTypesOfServices(ArrayList<String> typesOfServices) {
        this.typesOfServices = typesOfServices;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public ArrayList<Cover> getCovers() {
        return covers;
    }

    public void setCovers(ArrayList<Cover> covers) {
        this.covers = covers;
    }

    public String getLogoThumb() {
        return logoThumb;
    }

    public void setLogoThumb(String logoThumb) {
        this.logoThumb = logoThumb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getDistance(){ return distance; }
}
