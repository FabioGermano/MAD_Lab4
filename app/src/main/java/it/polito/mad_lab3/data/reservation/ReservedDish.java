package it.polito.mad_lab3.data.reservation;

/**
 * Created by f.germano on 12/04/2016.
 */
public class ReservedDish {
    private String name;
    private boolean isOffer;
    private int quantity;
    private float price;

    public ReservedDish(String name, boolean isOffer, int quantity, float price) {
        this.name = name;
        this.isOffer = isOffer;
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setIsOffer(boolean isOffer) {
        this.isOffer = isOffer;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
