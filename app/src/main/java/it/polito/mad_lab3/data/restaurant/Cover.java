package it.polito.mad_lab3.data.restaurant;

/**
 * Created by Giovanna on 02/05/2016.
 */
public class Cover {

    private String largePath;
    private String thumbPath;

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
