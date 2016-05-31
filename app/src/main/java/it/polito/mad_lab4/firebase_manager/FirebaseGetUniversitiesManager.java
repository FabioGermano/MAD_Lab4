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

import it.polito.mad_lab4.newData.other.University;
import it.polito.mad_lab4.newData.user.User;

/**
 * Created by Roby on 31/05/2016.
 */
public class FirebaseGetUniversitiesManager implements ValueEventListener{

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;

    private ArrayList<String> universities;

    public void getUniversities() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("universities").addListenerForSingleValueEvent(this);

        universities = new ArrayList<String>();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();
        University u;
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            u = postSnapshot.getValue(University.class);
            universities.add(u.getName());
        }
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public  ArrayList<String> getResult() {
        return universities;
    }

    public void waitForResult() {
        lock.lock();
        if(!resultReturned) {
            System.out.println("!resultReturned");
            try {
                System.out.println("try");
                cv.await();
            } catch (InterruptedException e) {
                System.out.println("Eccezione: "+ e.getMessage());
                Log.e(e.getMessage(), e.getMessage());
            }
        }
    }
}
