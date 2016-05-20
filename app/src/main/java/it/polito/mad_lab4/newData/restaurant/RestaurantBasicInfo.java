package it.polito.mad_lab4.newData.restaurant;

/**
 * Created by f.germano on 20/05/2016.
 */
public class RestaurantBasicInfo {
    private String restaurantId; // not mapped in firebase
    private String name;
    private Object position; //to be defined

    public RestaurantBasicInfo() {
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }
}
