package it.polito.mad_lab4.firebase_manager;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.restaurant.CoverImage;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.user.User;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveRestaurantProfileManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;
    private boolean timeout;

    public void saveRestaurant(final Restaurant restaurant,
                               final Bitmap logoThumb,
                               final Bitmap coversThumb[],
                               final Bitmap coversLarge[]) {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                15000
        );

        new Thread() {
            public void run() {
                PhotoLoader photoLoader = new PhotoLoader();

                if(logoThumb != null) {
                    photoLoader.loadPhoto(restaurant.getRestaurantId() + "_logo", logoThumb, null);
                    photoLoader.waitForResult();

                    restaurant.setLogoThumbDownloadLink(photoLoader.getDownloadLinkThumb());
                }

                for(int i = 0; i<coversThumb.length; i++){
                    if(coversThumb[i] != null) {
                        photoLoader = new PhotoLoader();
                        photoLoader.loadPhoto(restaurant.getRestaurantId() + "_cover_" + i, coversThumb[i], coversLarge[i]);
                        photoLoader.waitForResult();

                        restaurant.setLargeCoverByIndex(i, photoLoader.getDownloadLinkLarge());
                        restaurant.setThumbCoverByIndex(i, photoLoader.getDownloadLinkThumb());
                    }
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("restaurants/" + restaurant.getRestaurantId());
                myRef.setValue(restaurant, FirebaseSaveRestaurantProfileManager.this);

                User u = new User();
                u.setUserType("M");
                u.setName(restaurant.getRestaurantName());
                u.setAvatarDownloadLink(restaurant.getLogoThumbDownloadLink());

                myRef = database.getReference("users/" + restaurant.getRestaurantId());
                myRef.setValue(u, FirebaseSaveRestaurantProfileManager.this);
            }
        }.start();
    }

    private void timeout() {
        lock.lock();
        timeout = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onComplete(final DatabaseError _databaseError, DatabaseReference databaseReference) {
        lock.lock();
        firebaseReturnedResult = true;
        databaseError = _databaseError;
        cv.signal();
        lock.unlock();
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
