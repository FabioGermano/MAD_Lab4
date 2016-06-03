package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.user.User;

/**
 * Created by Euge on 03/06/2016.
 */
public class FirebaseGetUserInfoManager implements ValueEventListener {
    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;

    private User user;

    public void getClientInfo(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        user = new User();
        mDatabase.child("users").child(id).addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();

        user = dataSnapshot.getValue(User.class);

        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        user = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public void waitForResult() {
        lock.lock();
        if(!resultReturned) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                System.out.println("Eccezione: "+ e.getMessage());
                Log.e(e.getMessage(), e.getMessage());
            }
            finally {
                lock.unlock();
            }
        }
    }

    public User getUserInfo(){
        return this.user;
    }
}
