package it.polito.mad_lab4.newData.user;

/**
 * Created by f.germano on 20/05/2016.
 */
public class User {
    private String name;
    private String userType;

    public User(){
    }

    public User(String name, String userType) {
        this.name = name;
        this.userType = userType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String type) {
        this.userType = type;
    }
}
