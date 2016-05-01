package it.polito.mad_lab3.data.user;

/**
 * Created by f.germano on 30/04/2016.
 */
public class UserPhotoLike {
    private int restaurantId;
    private int userPhotoId;

    public UserPhotoLike(int restaurantId, int userPhotoId) {
        this.restaurantId = restaurantId;
        this.userPhotoId = userPhotoId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(int userPhotoId) {
        this.userPhotoId = userPhotoId;
    }
}
