package it.polito.mad_lab4.firebase_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.client.ClientPersonalInformation;
import it.polito.mad_lab4.newData.restaurant.Dish;

/**
 * Created by Roby on 31/05/2016.
 */
public class FirebaseSaveClientInfoManager implements DatabaseReference.CompletionListener{

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private String downloadLinkAvatar;
    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;
    private Context context;

    public FirebaseSaveClientInfoManager(Context context){
        this.context = context;
    }


    public void saveClientInfo(final ClientPersonalInformation client, final String id, Bitmap avatar) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("clients/" + id);


        if (avatar == null)
            client.setAvatarDownloadLink(context.getResources().getString(R.string.default_avatar));
        else{
            //salvo bitmap su serve e setto link
        }

        myRef.setValue(client);


/*
        if(avatar != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://project-9122886501087922816.appspot.com");

            StorageReference thumbNameRef = storageRef.child(id + "_avatar.jpg");

            UploadTask uploadTask = thumbNameRef.putBytes(Helper.getBitmapAsByteArray(avatar));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadLinkAvatar = taskSnapshot.getDownloadUrl().toString();
                            myRef.setValue(client, ClientPersonalInformation.class);
                        }
                    });
        }
        else {
            client.setAvatarDownloadLink(null);
            myRef.setValue(client, ClientPersonalInformation.class);
        }*/





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
