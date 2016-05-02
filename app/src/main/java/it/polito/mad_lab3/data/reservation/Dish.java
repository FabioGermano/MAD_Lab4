package it.polito.mad_lab3.data.reservation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Giovanna on 29/04/2016.
 */
public class Dish implements Parcelable {

    private String dishName;
    private int quantity;
    private float price;
    private String type;
    private boolean isOffer;

    public Dish(String dishName, int quantity, float price, String type, boolean isOffer) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.isOffer=isOffer;
    }

    protected Dish(Parcel in) {
        dishName = in.readString();
        quantity = in.readInt();
        price = in.readFloat();
        type = in.readString();
        isOffer = in.readByte() != 0;
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dishName);
        dest.writeInt(quantity);
        dest.writeFloat(price);
        dest.writeString(type);
        dest.writeByte((byte) (isOffer ? 1 : 0));
    }
}
