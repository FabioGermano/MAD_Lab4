package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.client.ClientPersonalInformation;
import it.polito.mad_lab4.newData.other.University;

/**
 * Created by Roby on 31/05/2016.
 */
public class FirebaseGetClientInfoManager implements ValueEventListener {
    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;
    private boolean timeout;

    private ClientPersonalInformation client;
    private DatabaseReference mDatabase;

    public void getClientInfo(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        client = new ClientPersonalInformation();
        mDatabase.child("clients").child(id).addListenerForSingleValueEvent(this);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        timeout();
                    }
                },
                30000
        );
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();

        client = dataSnapshot.getValue(ClientPersonalInformation.class);

        resultReturned = true;
        this.cv.signal();
        lock.unlock();


    }

    private void timeout() {
        lock.lock();
        timeout = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        client = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public ClientPersonalInformation getResult() {
        return client;
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

