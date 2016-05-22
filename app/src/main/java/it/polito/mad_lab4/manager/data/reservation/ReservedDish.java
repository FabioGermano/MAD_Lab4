package it.polito.mad_lab4.manager.data.reservation;

/**
 * Created by f.germano on 12/04/2016.
 */
public class ReservedDish {
    private String name;
    private boolean isOffer;
    private int quantity;

    public ReservedDish(String name, boolean isOffer, int quantity) {
        this.name = name;
        this.isOffer = isOffer;
        this.quantity = quantity;
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
}
