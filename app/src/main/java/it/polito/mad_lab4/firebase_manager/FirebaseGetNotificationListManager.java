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

import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.user.Notification;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseGetNotificationListManager implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private ArrayList<Notification> notifications = new ArrayList<>();
    private boolean resultReturned = false;

    private Query query;

    public void getNotifications(final String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        query = database
                .getReference("userAlerts/" + userId);

        query.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() != null) {
            lock.lock();
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                this.notifications.add(d.getValue(Notification.class));
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

    public ArrayList<Notification> getResult() {
        return notifications;
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

    public void terminate() {
        query.removeEventListener(this);
    }
}
