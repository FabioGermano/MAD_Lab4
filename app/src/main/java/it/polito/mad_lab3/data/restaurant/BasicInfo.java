package it.polito.mad_lab3.data.restaurant;

/**
 * Created by f.germano on 25/04/2016.
 */
public class BasicInfo {

    public BasicInfo(String address, String phone){
        this.address = address;
        this.phone = phone;
    }

    private String address;
    private String phone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
