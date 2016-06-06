package it.polito.mad_lab4.newData.other;

import java.io.Serializable;

/**
 * Created by Euge on 06/06/2016.
 */
public class RestaurantPosition implements Serializable {
    private String restaurantId;
    private Position position;

    public RestaurantPosition(){}


    public String getRestaurantId() { return restaurantId; }

    public void setRestaurantId(String Rid){
        this.restaurantId = Rid;
    }

    public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position position){
        this.position = position;
    }
}
