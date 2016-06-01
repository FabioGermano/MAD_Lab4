package it.polito.mad_lab4.firebase_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.client.ClientPersonalInformation;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.user.User;

/**
 * Created by Roby on 31/05/2016.
 */
public class FirebaseSaveClientInfoManager implements DatabaseReference.CompletionListener{

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;
    private Context context;

    public FirebaseSaveClientInfoManager(Context context){
        this.context = context;
    }


    public void saveClientInfo(final ClientPersonalInformation client, final String id, Bitmap avatar) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference clientsRef = database.getReference("clients/" + id);
        final DatabaseReference usersRef = database.getReference("users/"+id);

        final User u = new User(client.getName(), "C", client.getAvatarDownloadLink());

        if (avatar == null){
            clientsRef.setValue(client);
            usersRef.setValue(u);
        }
        else{
            //salvo bitmap su serve e setto link
            PhotoLoader photoLoader = new PhotoLoader();
            photoLoader.loadAvatar(id, avatar);
            photoLoader.waitForResult();

            if (photoLoader.getDownloadLinkAvatar() != null) {
                client.setAvatarDownloadLink(photoLoader.getDownloadLinkAvatar());
                u.setAvatarDownloadLink(photoLoader.getDownloadLinkAvatar());
            }
            clientsRef.setValue(client);
            usersRef.setValue(u);
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
