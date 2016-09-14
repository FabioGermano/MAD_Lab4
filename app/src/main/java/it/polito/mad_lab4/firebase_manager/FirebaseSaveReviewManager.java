package it.polito.mad_lab4.firebase_manager;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.Review;
import it.polito.mad_lab4.newData.restaurant.ReviewFood;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveReviewManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();
    private boolean withDishes;
    private String restaurantId;
    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;
    private boolean timeout;

    public void saveReview(final String restaurantId,
                           final Review review , final boolean withDishes, final ArrayList<ReviewFood> reviewedFood) {
        this.withDishes=withDishes;
        this.restaurantId = restaurantId;

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                10000
        );

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reviews/" + restaurantId);
        String key = myRef.push().getKey();

        review.setReviewId(key);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        review.setDate(date);


        myRef = database.getReference("reviews/" + restaurantId + "/" + review.getReviewId());
        myRef.setValue(review, FirebaseSaveReviewManager.this);

        DatabaseReference upRef = database.getReference("restaurants/" + restaurantId);
        upRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if(currentData.getValue() != null) {
                    Restaurant restaurant= currentData.getValue(Restaurant.class);
                    restaurant.setNumReviews(restaurant.getNumReviews()+1);
                    restaurant.setTotRanking(restaurant.getTotRanking()+review.getRank());
                    currentData.setValue(restaurant);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if(!withDishes){
                    lock.lock();
                    firebaseReturnedResult = true;
                    databaseError = databaseError;
                    cv.signal();
                    lock.unlock();}
            }
        });

        if(withDishes){
            if(reviewedFood!=null && !reviewedFood.isEmpty()){
                    for(final ReviewFood r : reviewedFood) {

                        String path = null;
                        if (r.getFood() instanceof Offer) {
                            upRef = database.getReference("offers/" + restaurantId + "/" + r.getId());
                            upRef.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData currentData) {
                                    if (currentData.getValue() != null) {
                                        Offer offer = (Offer) currentData.getValue(Offer.class);
                                        offer.setNumRanks(offer.getNumRanks()+1);
                                        offer.setSumRank(offer.getSumRank()+r.getRating());
                                        currentData.setValue(offer);
                                    }
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    if(reviewedFood.indexOf(r)== reviewedFood.size()-1){
                                        lock.lock();
                                        firebaseReturnedResult = true;
                                        databaseError = databaseError;
                                        cv.signal();
                                        lock.unlock();}
                                    }

                            });

                        }
                        else if(r.getFood() instanceof Dish){
                            upRef = database.getReference("menu/" + restaurantId + "/" + r.getId());
                            upRef.runTransaction(new Transaction.Handler() {

                                @Override
                                public Transaction.Result doTransaction(MutableData currentData) {

                                    if (currentData.getValue() != null) {
                                        Dish dish = (Dish) currentData.getValue(Dish.class);
                                        dish.setNumRanks(dish.getNumRanks()+1);
                                        dish.setSumRank(dish.getSumRank()+r.getRating());
                                        currentData.setValue(dish);
                                    }
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                    if(reviewedFood.indexOf(r) == reviewedFood.size()-1){
                                        lock.lock();
                                        firebaseReturnedResult = true;
                                        databaseError = databaseError;
                                        cv.signal();
                                        lock.unlock();
                                    }

                                }
                            });
                        }
                    }

                }
        }

    }

    private void timeout() {
        lock.lock();
        timeout = true;
        this.cv.signal();
        lock.unlock();
    }


    @Override
    public void onComplete(final DatabaseError _databaseError, DatabaseReference databaseReference) {

    }

    /**
     * Returns false if errors occurred
     */
    public boolean waitForResult(){
        lock.lock();
        try {
        if(!firebaseReturnedResult || timeout)
                cv.await();
        } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }

        if (timeout)
            return false;

        return this.databaseError == null;
    }

    public void terminate() {

    }
}
