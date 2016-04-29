package it.polito.mad_lab3.data.user;

/**
 * Created by f.germano on 12/04/2016.
 */
public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String name;
    private String phone;

    public User(String name, String phone)
    {
        this.name = name;
        this.phone = phone;
    }
}
