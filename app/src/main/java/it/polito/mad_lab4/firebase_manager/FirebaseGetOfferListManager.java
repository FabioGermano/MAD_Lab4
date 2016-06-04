package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseGetOfferListManager implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private ArrayList<Offer> offers = new ArrayList<>();
    private boolean resultReturned = false;

    private Query query;

    public void getOffers(final String restaurantId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        query = database
                .getReference("offers/" + restaurantId);

        query.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null) {
            lock.lock();
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                this.offers.add(d.getValue(Offer.class));
            }
            resultReturned = true;
            this.cv.signal();
            lock.unlock();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // TODO manage errors
        lock.lock();
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public ArrayList<Offer> getResult() {
        return offers;
    }

    public void waitForResult() {
        lock.lock();
        if(!resultReturned) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
            }
        }
    }

    public void terminate() {
        query.removeEventListener(this);
    }
}
