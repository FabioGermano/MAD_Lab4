package it.polito.mad_lab3.bl;

import android.content.Context;

import java.util.Iterator;

import it.polito.mad_lab3.dal.DB;
import it.polito.mad_lab3.dal.DBManager;
import it.polito.mad_lab3.data.reservation.Reservation;
import it.polito.mad_lab3.data.reservation.ReservationType;
import it.polito.mad_lab3.data.reservation.ReservationTypeConverter;
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

    public static User findUserByUsernamePassword(Context _context, String username, String password){
        if(context == null){
            init(_context);
        }

        for(User u : userEntity.getUsers()){
            if(u.getUserLoginInfo().getUsername().equals(username) && u.getUserLoginInfo().getPassword().equals(password)){

                return u;
            }
        }

        return null;
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
    public static int getNewReservatioId(User user)
    {
        return user.getReservations().size()+1;
    }

    public static boolean checkUserPhotoLike(User user, int restaurantId, int userPhotoId){
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

    public static void removeUserPhotoLike(User user, int restaurantId, int userPhotoId){
        for (Iterator<UserPhotoLike> iterator = user.getUserPhotoLikes().iterator(); iterator.hasNext(); ) {
            UserPhotoLike userPhotoLike = iterator.next();
            if (userPhotoLike.getRestaurantId() == restaurantId && userPhotoLike.getUserPhotoId() == userPhotoId) {
                iterator.remove();
            }
        }
    }
    public static void addReservation(User user, Reservation reservation){
        user.getReservations().add(reservation);
    }

    public static void cancelReservation ( User user, int reservationId, boolean isPending){
        int index=-1;
        for(Reservation r: user.getReservations()) {
            if (r.getReservationId() == reservationId)
                index = user.getReservations().indexOf(r);
        }

        if(isPending){
                user.getReservations().remove(index);
            }
        else{
            user.getReservations().get(index).setStatus(ReservationTypeConverter.toString(ReservationType.DELETED));
        }

    }

    public static void saveChanges(Context _context){
        DBManager.serializeEntity(_context, DB.Users, userEntity);
    }

    public static int getCurrentUserId() {
        return 1;
    }
}
