package it.polito.mad_lab3.bl;

import android.content.Context;

import java.util.ArrayList;

import it.polito.mad_lab3.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab3.dal.DB;
import it.polito.mad_lab3.dal.DBManager;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.RestaurantEntity;
import it.polito.mad_lab3.data.restaurant.UserPhoto;

/**
 * Created by f.germano on 26/04/2016.
 */
public class RestaurantBL {

    private static Context context = null;
    private static RestaurantEntity restaurantEntity = null;

    private static void init(Context _context){
        context = _context;
        restaurantEntity = DBManager.deserializeToEntity(context, DB.Restaurants, RestaurantEntity.class);
    }

    public static Restaurant getRestaurantById(Context _context, int id){

        if(context == null){
            init(_context);
        }

        for(Restaurant r : restaurantEntity.getRestaurants()){
            if(r.getRestaurantId() == id){
                return  r;
            }
        }

        return null;
    }

    public static ArrayList<Restaurant> getAllRestaurants(Context _context){

        if(context == null){
            init(_context);
        }

        return restaurantEntity.getRestaurants();
    }

    public static int getNewUserPhotoId(Restaurant restaurant)
    {
        return restaurant.getUserPhotos().size()+1;
    }

    public static void saveChanges(Context _context){
        DBManager.serializeEntity(context, DB.Restaurants, restaurantEntity);
    }

    public static void addNewLikeToUserPhoto(Restaurant restaurant, int userPhotoId){
        for(UserPhoto r : restaurant.getUserPhotos()){
            if(r.getId() == userPhotoId){
                r.setLikes(r.getLikes()+1);
            }
        }
    }
}
