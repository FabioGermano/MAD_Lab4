package it.polito.mad_lab3.bl;

import android.content.Context;

import it.polito.mad_lab3.dal.DB;
import it.polito.mad_lab3.dal.DBManager;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.data.user.UserEntity;
import it.polito.mad_lab3.data.user.UserPhotoLike;

/**
 * Created by f.germano on 30/04/2016.
 */
public class UserBL {
    private static Context context = null;
    private static UserEntity userEntity = null;

    private static void init(Context _context){
        context = _context;
        userEntity = DBManager.deserializeToEntity(context, DB.Users, UserEntity.class);
    }

    public static User getUserById(Context _context, int id){

        if(context == null){
            init(_context);
        }

        for(User u : userEntity.getUsers()){
            if(u.getUserId() == id){
                return  u;
            }
        }

        return null;
    }

    public static boolean checkUserPhotoLike(Context _context, User user, int restaurantId, int userPhotoId){

        if(context == null){
            init(_context);
        }

        for(UserPhotoLike upl : user.getUserPhotoLikes() ){
            if(upl.getRestaurantId() == restaurantId && upl.getUserPhotoId() == userPhotoId){
                return true;
            }
        }

        return false;
    }

    public static void addUserPhotoLike(User user, int restaurantId, int userPhotoId){
        user.getUserPhotoLikes().add(new UserPhotoLike(restaurantId, userPhotoId));
    }

    public static void saveChanges(Context _context){
        DBManager.serializeEntity(_context, DB.Users, userEntity);
    }
}
