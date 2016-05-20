package it.polito.mad_lab4.data.restaurant;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by f.germano on 25/04/2016.
 */
public class UserPhoto implements Serializable{
    private String thumbPath;
    private String largePath;
    private int likes;
    private String description;
    private int id;

    public UserPhoto(String thumbPath, String largePath, int likes, String description, int id){
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.likes = likes;
        this.description = description;
        this.id = id;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getLargePath() {
        return largePath;
    }

    public void setLargePath(String largePath) {
        this.largePath = largePath;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
