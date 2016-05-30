package it.polito.mad_lab4.firebase_manager;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveDishManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private String restaurantId;
    private String downloadLinkThumb, downloadLinkLarge;
    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;

    public void saveDish(final String restaurantId,
                         final boolean isNewDish,
                         final Dish dish,
                         boolean isImageSetted,
                         final Bitmap thumb,
                         final Bitmap large) {

        this.restaurantId = restaurantId;
        if(isNewDish){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("menu/" + restaurantId);
            String key = myRef.push().getKey();

            dish.setDishId(key);
        }

        if(thumb != null && large != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://project-9122886501087922816.appspot.com");
            StorageReference thumbNameRef = storageRef.child(dish.getDishId() + "_thumb.jpg");

            UploadTask uploadTask = thumbNameRef.putBytes(Helper.getBitmapAsByteArray(thumb));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadLinkThumb = taskSnapshot.getDownloadUrl().toString();

                    StorageReference largeNameRef = storageRef.child(dish.getDishId() + "_large.jpg");
                    UploadTask uploadTask = largeNameRef.putBytes(Helper.getBitmapAsByteArray(large));
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadLinkLarge = taskSnapshot.getDownloadUrl().toString();

                            dish.setThumbDownloadLink(downloadLinkThumb);
                            dish.setLargeDownloadLink(downloadLinkLarge);
                            dish.setIsTodayAvailable(true);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("menu/" + restaurantId + "/" + dish.getDishId());
                            myRef.setValue(dish, FirebaseSaveDishManager.this);
                        }
                    });
                }
            });
        }
        else {

            if(!isImageSetted) {
                dish.setLargeDownloadLink(null);
                dish.setThumbDownloadLink(null);
            }

            dish.setIsTodayAvailable(true);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("menu/" + restaurantId + "/" + dish.getDishId());
            myRef.setValue(dish, FirebaseSaveDishManager.this);
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
