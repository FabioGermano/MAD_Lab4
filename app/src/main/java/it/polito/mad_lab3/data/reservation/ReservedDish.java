package it.polito.mad_lab3.data.reservation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by f.germano on 12/04/2016.
 */
public class ReservedDish implements Parcelable{
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

    protected ReservedDish(Parcel in) {
        name = in.readString();
        isOffer = in.readByte() != 0;
        quantity = in.readInt();
        price = in.readFloat();
    }

    public static final Creator<ReservedDish> CREATOR = new Creator<ReservedDish>() {
        @Override
        public ReservedDish createFromParcel(Parcel in) {
            return new ReservedDish(in);
        }

        @Override
        public ReservedDish[] newArray(int size) {
            return new ReservedDish[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeByte((byte) (isOffer ? 1 : 0));
        dest.writeInt(quantity);
        dest.writeFloat(price);
    }
}
