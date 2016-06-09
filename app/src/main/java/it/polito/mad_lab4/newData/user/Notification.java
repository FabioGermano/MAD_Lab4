package it.polito.mad_lab4.newData.user;

/**
 * Created by Giovanna on 29/05/2016.
 */
public class Notification {

    private String restaurantId;
    private boolean isNewOffer; // reservation or offer
    private String offerId;
    private boolean accepted;
    private String message;
    private String restaurantName;

    public Notification(){

    }
    public Notification(String restaurantId, boolean isNewOffer, String message) {
        this.restaurantId = restaurantId;
        this.isNewOffer = isNewOffer;
        this.message = message;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public boolean isNewOffer() {
        return isNewOffer;
    }


    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }


    public void setNewOffer(boolean newOffer) {
        isNewOffer = newOffer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

}
