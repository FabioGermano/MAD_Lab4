package it.polito.mad_lab3.data.restaurant;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by f.germano on 25/04/2016.
 */
public class RestaurantEntity {
    private ArrayList<Restaurant> restaurants;

    public RestaurantEntity(){
        this.restaurants = new ArrayList<Restaurant>();
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
