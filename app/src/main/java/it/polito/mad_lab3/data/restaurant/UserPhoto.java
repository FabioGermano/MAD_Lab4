package it.polito.mad_lab3.data.restaurant;

/**
 * Created by f.germano on 25/04/2016.
 */
public class UserPhoto {
    private String thumbPath;
    private String largePath;
    private int likes;

    public UserPhoto(String thumbPath, String largePath, int likes){
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.likes = likes;
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
}
