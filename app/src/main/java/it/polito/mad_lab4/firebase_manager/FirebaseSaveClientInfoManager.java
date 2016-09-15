package it.polito.mad_lab4.firebase_manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.user.User;

/**
 * Created by Roby on 31/05/2016.
 */
public class FirebaseSaveClientInfoManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;
    private Context context;
    private boolean timeout;

    public FirebaseSaveClientInfoManager(Context context){
        this.context = context;
    }


    public void saveClientInfo(final ClientPersonalInformation client, final String id, Bitmap avatar) {

        /*new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                30000
        );*/

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference clientsRef = database.getReference("clients/" + id);
        final DatabaseReference usersRef = database.getReference("users/"+id);
        final User u = new User(client.getName(), "C", client.getAvatarDownloadLink());

        if (avatar == null){
            if(client.getAvatarDownloadLink() == null || client.getAvatarDownloadLink().isEmpty()) {
                client.setAvatarDownloadLink("https://firebasestorage.googleapis.com/v0/b/project-9122886501087922816.appspot.com/o/default_avatar.png?alt=media&token=0de6dcbb-7572-47e9-bfba-f31b551b8779");
                u.setAvatarDownloadLink("https://firebasestorage.googleapis.com/v0/b/project-9122886501087922816.appspot.com/o/default_avatar.png?alt=media&token=0de6dcbb-7572-47e9-bfba-f31b551b8779");
            }
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
        }

        usersRef.setValue(u);
        clientsRef.setValue(client);

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
