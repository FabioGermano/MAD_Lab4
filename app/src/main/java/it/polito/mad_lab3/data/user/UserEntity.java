package it.polito.mad_lab3.data.user;

import java.util.ArrayList;

/**
 * Created by f.germano on 30/04/2016.
 */
public class UserEntity {
    public UserEntity() {
        this.users = new ArrayList<User>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public ArrayList<User> users;
}
