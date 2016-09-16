package it.polito.mad_lab4.firebase_manager;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveOfferManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private String restaurantId;
    private String downloadLinkThumb, downloadLinkLarge;
    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;
    private boolean timeout;

    public void saveOffer(final String restaurantId,
                         final boolean isNewOffer,
                         final Offer offer,
                         boolean isImageSetted,
                         final Bitmap thumb,
                         final Bitmap large) {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                10000
        );

        this.restaurantId = restaurantId;
        if(isNewOffer){
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("offers/" + restaurantId);
            String key = myRef.push().getKey();

            offer.setOfferId(key);
        }

        if(thumb != null && large != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://project-9122886501087922816.appspot.com");
            StorageReference thumbNameRef = storageRef.child(offer.getOfferId() + "_thumb.jpg");

            UploadTask uploadTask = thumbNameRef.putBytes(Helper.getBitmapAsByteArray(thumb));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadLinkThumb = taskSnapshot.getDownloadUrl().toString();

                    StorageReference largeNameRef = storageRef.child(offer.getOfferId() + "_large.jpg");
                    UploadTask uploadTask = largeNameRef.putBytes(Helper.getBitmapAsByteArray(large));
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadLinkLarge = taskSnapshot.getDownloadUrl().toString();

                            offer.setThumbDownloadLink(downloadLinkThumb);
                            offer.setLargeDownloadLink(downloadLinkLarge);
                            offer.setIsTodayAvailable(true);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("offers/" + restaurantId + "/" + offer.getOfferId());
                            myRef.setValue(offer, FirebaseSaveOfferManager.this);
                        }
                    });
                }
            });
        }
        else {

            offer.setIsTodayAvailable(true);

            if(!isImageSetted) {
                offer.setLargeDownloadLink(null);
                offer.setThumbDownloadLink(null);
            }

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("offers/" + restaurantId + "/" + offer.getOfferId());
            myRef.setValue(offer, FirebaseSaveOfferManager.this);
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
