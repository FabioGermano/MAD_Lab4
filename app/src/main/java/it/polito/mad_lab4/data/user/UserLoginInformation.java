package it.polito.mad_lab4.data.user;

import java.io.Serializable;

/**
 * Created by Eugenio on 09/05/2016.
 */
public class UserLoginInformation implements Serializable {
    private String username; //email
    private String password;

    public UserLoginInformation(){
        this.username = null;
        this.password = null;
    }

    public void setUsername(String arg){
        this.username = arg;
    }

    public String getUsername(){
        return this.username;
    }

    public void setPassword(String arg){
        this.password = arg;
    }

    public String getPassword(){
        return this.password;
    }
}
