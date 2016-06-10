package it.polito.mad_lab4.firebase_manager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

        Map updateAvg = new HashMap();
        updateAvg.put("numDishesAndOffers", numPiatti);
        updateAvg.put("totDishesAndOffers", totPrezzo);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("menu/" + idRestaurant);
        myRef1.updateChildren(updateAvg, this);

    }

    @Override
    public void onComplete(DatabaseError _databaseError, DatabaseReference databaseReference) {
        lock.lock();
        firebaseReturnedResult = true;
        databaseError = _databaseError;
        cv.signal();
        lock.unlock();
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
