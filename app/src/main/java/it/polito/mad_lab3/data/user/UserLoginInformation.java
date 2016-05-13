package it.polito.mad_lab3.data.user;

import java.io.Serializable;

/**
 * Created by Eugenio on 09/05/2016.
 */
public class UserLoginInformation implements Serializable {
    //private boolean login;
    private String username;
    private String password;

    public UserLoginInformation(){
        //this.login = false;
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
/*
    public boolean isLogin(){
        return this.login;
    }

    public void setLogin(boolean arg){
        this.login = arg;
    }*/
}
