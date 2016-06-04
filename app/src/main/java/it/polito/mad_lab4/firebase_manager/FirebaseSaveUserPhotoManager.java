package it.polito.mad_lab4.firebase_manager;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.UserPhoto;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveUserPhotoManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private String restaurantId;
    private String downloadLinkThumb, downloadLinkLarge;
    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;

    public void saveUserPhoto(final String restaurantId,
                              final String description,
                              final Bitmap thumb,
                              final Bitmap large) {

        final UserPhoto userPhoto = new UserPhoto();

        this.restaurantId = restaurantId;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("userphotos/" + restaurantId);
        String key = myRef.push().getKey();

        userPhoto.setUserPhotoId(key);

        if(thumb != null && large != null) {

            new Thread() {
                public void run() {
                    PhotoLoader photoLoader = new PhotoLoader();
                    photoLoader.loadPhoto(userPhoto.getUserPhotoId(), thumb, large);
                    photoLoader.waitForResult();

                    userPhoto.setThumbDownloadLink(photoLoader.getDownloadLinkThumb());
                    userPhoto.setLargeDownloadLink(photoLoader.getDownloadLinkLarge());
                    userPhoto.setDescription(description);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("userphotos/" + restaurantId + "/" + userPhoto.getUserPhotoId());
                    myRef.setValue(userPhoto, FirebaseSaveUserPhotoManager.this);
                }
            }.start();
        }
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
        if(!firebaseReturnedResult) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
            }
        }

        return this.databaseError == null;
    }

    public void terminate() {

    }
}
