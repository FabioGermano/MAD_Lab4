package it.polito.mad_lab3.data.restaurant;

import java.util.ArrayList;

/**
 * Created by f.germano on 25/04/2016.
 */
public class BasicInfo {


    private String address;
    private String phone;
    private ArrayList<String> timeTable; // string "CLOSED" if the restaurant is closed on that day; index -> day of week
    private String email;
    private String description;
    private String logoThumb;
    private ArrayList<Cover> covers;


    public BasicInfo(String address, String phone,String email, String description, String logoThumb,  ArrayList<String> timeTable){
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.logoThumb = logoThumb;
        this.timeTable=timeTable;
        this.covers = new ArrayList<Cover>();

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
}
