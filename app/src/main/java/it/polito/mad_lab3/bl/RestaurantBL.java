package it.polito.mad_lab3.bl;

import android.content.Context;

import it.polito.mad_lab3.dal.DB;
import it.polito.mad_lab3.dal.DBManager;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.RestaurantEntity;

/**
 * Created by f.germano on 26/04/2016.
 */
public class RestaurantBL {

    private Context context;
    private RestaurantEntity restaurantEntity;

    public RestaurantBL(Context context){
        this.context = context;
        restaurantEntity = DBManager.deserializeToEntity(context, DB.Restaurants, RestaurantEntity.class);
    }

    public Restaurant getRestaurantById(int id){
        for(Restaurant r : restaurantEntity.getRestaurants()){
            if(r.getRestaurantId() == id){
                return  r;
            }
        }

        return null;
    }

    public int getNewUserPhotoId(Restaurant restaurant)
    {
        return restaurant.getUserPhotos().size()+1;
    }

    public void saveChanges(){
        DBManager.serializeEntity(context, DB.Restaurants, restaurantEntity);
    }
}
