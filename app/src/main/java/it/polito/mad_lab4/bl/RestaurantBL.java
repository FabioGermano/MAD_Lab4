package it.polito.mad_lab4.bl;

import android.content.Context;

import java.util.ArrayList;
import java.util.Iterator;

import it.polito.mad_lab4.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.dal.DB;
import it.polito.mad_lab4.dal.DBManager;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.restaurant.RestaurantEntity;
import it.polito.mad_lab4.data.restaurant.Review;
import it.polito.mad_lab4.data.restaurant.ReviewFood;
import it.polito.mad_lab4.data.restaurant.UserPhoto;

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

    public static ArrayList<String> getAllDishName(Context context, int id){
        ArrayList<String> risultato = new ArrayList<>();
        Restaurant r = getRestaurantById(context, id);
        for(Dish d : r.getDishes()){
            risultato.add(d.getDishName());
        }
        return risultato;
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
    public static void updateDishesRating (Restaurant restaurant, ArrayList<ReviewFood> data, int section){
        Dish d;
        for(ReviewFood r : data)
        switch (section) {
            case 0:
                Offer o = restaurant.getOffers().get(r.getPosition());
                o.setNumRanks(o.getNumRanks()+1);
                o.setSumRank(o.getSumRank()+r.getRating());
                break;
            case 1:
                d = restaurant.getDishesOfCategory(DishType.MainCourses).get(r.getPosition());
                d.setNumRanks(d.getNumRanks()+1);
                d.setSumRank(d.getSumRank()+r.getRating());
                break;
            case 2:
                d = restaurant.getDishesOfCategory(DishType.SecondCourses).get(r.getPosition());
                d.setNumRanks(d.getNumRanks()+1);
                d.setSumRank(d.getSumRank()+r.getRating());
                break;
            case 3:
                d = restaurant.getDishesOfCategory(DishType.Dessert).get(r.getPosition());
                d.setNumRanks(d.getNumRanks()+1);
                d.setSumRank(d.getSumRank()+r.getRating());
                break;
            case 4:
                d = restaurant.getDishesOfCategory(DishType.Other).get(r.getPosition());
                d.setNumRanks(d.getNumRanks()+1);
                d.setSumRank(d.getSumRank()+r.getRating());
                break;
            default:
                break;
        }
    }

    public static void addReview(Restaurant restaurant, Review review){
        restaurant.getReviews().add(review);
        restaurant.setNumReviews(restaurant.getNumReviews()+1);
    }

    public static void removeLikeToUserPhoto(Restaurant restaurant, int userPhotoId){
        for(UserPhoto r : restaurant.getUserPhotos()){
            if(r.getId() == userPhotoId){
                r.setLikes(r.getLikes()-1);
            }
        }
    }
}
