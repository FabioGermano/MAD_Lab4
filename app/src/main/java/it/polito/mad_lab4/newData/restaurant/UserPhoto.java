package it.polito.mad_lab4.newData.restaurant;

import java.io.Serializable;

/**
 * Created by f.germano on 25/04/2016.
 */
public class UserPhoto implements Serializable{
    private int likes;
    private String description;
    private String userPhotoId; // not mapped in firebase db, obteined by clling .getKey()

    public UserPhoto(){
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(String userPhotoId) {
        this.userPhotoId = userPhotoId;
    }
}
