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
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Review;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseGetReviewsListManager implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private ArrayList<Review> reviews = new ArrayList<>();
    private boolean resultReturned = false;

    private Query query;
    private boolean timeout;

    public void getReviews(final String restaurantId, Integer num){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        query = database
                .getReference("reviews/" + restaurantId);
        if(num != null){
            query = query.limitToFirst(num);
        }
        query.addListenerForSingleValueEvent(this);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                10000
        );
    }

    private void timeout() {
        lock.lock();
        timeout = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null) {
            lock.lock();
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                this.reviews.add(d.getValue(Review.class));
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

    public ArrayList<Review> getResult() {
        return reviews;
    }

    public boolean waitForResult() {
        lock.lock();
        try {
        if(!resultReturned || timeout)
                cv.await();
        } catch (InterruptedException e) {
            Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }
        return timeout;
    }

    public void terminate() {
        query.removeEventListener(this);
    }
}
