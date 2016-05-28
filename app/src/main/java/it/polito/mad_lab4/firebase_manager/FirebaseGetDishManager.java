package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.restaurant.Dish;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseGetDishManager implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private Dish dish;
    private boolean resultReturned = false;

    public void getDish(final String restaurantId, final String dishId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("menu/" + restaurantId + "/" + dishId);
        myRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();
        this.dish = dataSnapshot.getValue(Dish.class);
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // TODO manage errors
        lock.lock();
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public Dish getResult() {
        return dish;
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
}
