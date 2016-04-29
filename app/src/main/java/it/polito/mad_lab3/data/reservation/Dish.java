package it.polito.mad_lab3.data.reservation;

/**
 * Created by Giovanna on 29/04/2016.
 */
public class Dish {
    private String dishName;
    private int quantity;
    private float price;
    private String type; // primi, secondi...


    private boolean isOffer;

    public Dish(String dishName, int quantity, float price, String type, boolean isOffer) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.isOffer=isOffer;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
