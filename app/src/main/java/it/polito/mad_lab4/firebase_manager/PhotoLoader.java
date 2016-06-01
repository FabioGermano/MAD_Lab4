package it.polito.mad_lab4.firebase_manager;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.restaurant.Dish;

/**
 * Created by f.germano on 28/05/2016.
 */
public class PhotoLoader  {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private String downloadLinkThumb, downloadLinkLarge, downloadLinkAvatar;
    private boolean resultReturned = false;
    private DatabaseReference myRef;

    public void loadPhoto(final String id, final Bitmap thumb, final Bitmap large){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://project-9122886501087922816.appspot.com");
        StorageReference thumbNameRef = storageRef.child(id + "_thumb.jpg");

        UploadTask uploadTask = thumbNameRef.putBytes(Helper.getBitmapAsByteArray(thumb));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadLinkThumb = taskSnapshot.getDownloadUrl().toString();

                if(large !=  null) {
                    StorageReference largeNameRef = storageRef.child(id + "_large.jpg");
                    UploadTask uploadTask = largeNameRef.putBytes(Helper.getBitmapAsByteArray(large));
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadLinkLarge = taskSnapshot.getDownloadUrl().toString();

                            resultReturned = true;
                            lock.lock();
                            cv.signal();
                            lock.unlock();
                        }
                    });
                }
                else{
                    resultReturned = true;
                    lock.lock();
                    cv.signal();
                    lock.unlock();
                }
            }
        });
    }

    public void loadAvatar(final String id, final Bitmap avatar){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReferenceFromUrl("gs://project-9122886501087922816.appspot.com");
        StorageReference thumbNameRef = storageRef.child(id + "_avatar.jpg");

        UploadTask uploadTask = thumbNameRef.putBytes(Helper.getBitmapAsByteArray(avatar));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadLinkAvatar = taskSnapshot.getDownloadUrl().toString();
                resultReturned = true;
                lock.lock();
                cv.signal();
                lock.unlock();
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultReturned = true;
                lock.lock();
                cv.signal();
                lock.unlock();
            }
        });
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

    public String getDownloadLinkThumb() {
        return downloadLinkThumb;
    }

    public String getDownloadLinkLarge() {
        return downloadLinkLarge;
    }

    public String getDownloadLinkAvatar(){ return downloadLinkAvatar; }
}
