package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Euge on 10/06/2016.
 */
public class FirebaseUpdateAvgDishesAndOffers implements DatabaseReference.CompletionListener {
    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;

    public void updateAvgDishesAndOffers(String idRestaurant, float price, float oldPrice, boolean isNew){

        int numPiatti = -1;
        float totPrezzo = -1;

        FirebaseGetRestaurantInfoManager restaurantInfoManager;

        restaurantInfoManager = new FirebaseGetRestaurantInfoManager();
        restaurantInfoManager.getRestaurantInfo(idRestaurant, "totDishesAndOffers");
        restaurantInfoManager.waitForResult();
        totPrezzo = Float.parseFloat(restaurantInfoManager.getResult());

        if(isNew){
            restaurantInfoManager = new FirebaseGetRestaurantInfoManager();
            restaurantInfoManager.getRestaurantInfo(idRestaurant, "numDishesAndOffers");
            restaurantInfoManager.waitForResult();
            numPiatti = Integer.parseInt(restaurantInfoManager.getResult());
            if (numPiatti != -1)
                numPiatti++;

            if (totPrezzo != -1){
                totPrezzo = totPrezzo + price;
            }
        } else {
            //se è una modifica
            if (totPrezzo != -1) {
                if (oldPrice != price)
                    totPrezzo = totPrezzo - oldPrice + price;
            }
        }

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("menu/" + idRestaurant);
        myRef.setValue(dish, FirebaseSaveDishManager.this);*/
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }

    public boolean waitForResult(){
        lock.lock();
        if(!firebaseReturnedResult) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
            }finally {
                lock.unlock();
            }
        }

        return this.databaseError == null;
    }
}
