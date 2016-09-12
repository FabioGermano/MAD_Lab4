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

/**
 * Created by Euge on 10/06/2016.
 */
public class FirebaseGetClientSingleInformation implements ValueEventListener {
    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private String result = null;
    private boolean resultReturned = false;
    private boolean timeout;
    private DatabaseReference mDatabase;

    public void getClientInfo(String id, String field) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mDatabase.child("clients").child(id).child(field).addListenerForSingleValueEvent(this);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                10000
        );
    }

    private void timeout() {
        lock.lock();
        timeout = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();

        result = dataSnapshot.getValue(String.class);

        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        result = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public String getResult() {
        return result;
    }

    public boolean waitForResult() {
        lock.lock();
        try {
            if(!resultReturned || timeout)
                cv.await();
        } catch (InterruptedException e) {
            System.out.println("Eccezione: "+ e.getMessage());
            Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }
        return timeout;
    }

    public void terminate() {
        mDatabase.removeEventListener(this);
    }
}
