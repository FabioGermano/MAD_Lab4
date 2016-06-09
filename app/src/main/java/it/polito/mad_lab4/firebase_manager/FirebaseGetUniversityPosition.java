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

import it.polito.mad_lab4.newData.other.Position;

/**
 * Created by Euge on 10/06/2016.
 */
public class FirebaseGetUniversityPosition implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;

    private Position universityPosition = null;

    public void getUniversityPosition(String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("universities").child(id).child("position").addListenerForSingleValueEvent(this);

    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();
        universityPosition = dataSnapshot.getValue(Position.class);
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        universityPosition = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public Position getResult() {
        return universityPosition;
    }

    public void waitForResult() {
        lock.lock();
        if(!resultReturned) {
            try {
                System.out.println("try");
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
}
