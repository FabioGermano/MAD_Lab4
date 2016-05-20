package it.polito.mad_lab4.newData.reservation;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by f.germano on 12/04/2016.
 */
public class ReservedDish {
    private String reservedDishId; // not mapped in firebase
    private String name;
    private boolean isOffer;
    private int quantity;
    private float price;

    public ReservedDish(){
    }

    public String getReservedDishId() {
        return reservedDishId;
    }

    public void setReservedDishId(String reservedDishId) {
        this.reservedDishId = reservedDishId;
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

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
