package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.client.ClientPersonalInformation;

public class FirebaseGetRestaurantInfoManager implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();
    private DatabaseReference myRef;
    private boolean resultReturned = false;
    private String info;
    private String field;
    private String[] fields={"restaurantName", "address", "city", "logoThumbDownloadLink", "totRanking", "numReviews"};

    public void getRestaurantInfo(String restaurantId, String field) {

        this.field=field;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("/restaurants/"+restaurantId+"/"+this.field);
        myRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();
        if(field.equals(fields[4]) || field.equals(fields[5])){
            this.info= String.valueOf(dataSnapshot.getValue());
        }
        else
            this.info = (String) dataSnapshot.getValue();

        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        this.info = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public String getResult() {
        return this.info;
    }

    public void waitForResult() {
        lock.lock();
        if(!resultReturned) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
            }
            finally {
                lock.unlock();
            }
        }
    }
    public void terminate() {
        myRef.removeEventListener(this);
    }
}

