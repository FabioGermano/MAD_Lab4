package it.polito.mad_lab3.data.restaurant;

import java.io.Serializable;

/**
 * Created by Giovanna on 02/05/2016.
 */
public class Cover implements Serializable{

    private String largePath;
    private String thumbPath;

    public String getResPhoto() {
        return resPhoto;
    }

    public void setResPhoto(String resPhoto) {
        this.resPhoto = resPhoto;
    }

    private String resPhoto;

    public Cover(String largePath, String thumbPath) {
        this.largePath = largePath;
        this.thumbPath = thumbPath;
    }

    public String getLargePath() {
        return largePath;
    }

    public void setLargePath(String largePath) {
        this.largePath = largePath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
}
