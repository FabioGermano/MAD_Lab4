package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.manager.Oggetto_menu;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservedDish;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveReservationManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();
    private boolean isOk;

    private boolean resultReturned = false;

    private DatabaseReference myRef, myRef1;

    public void saveReservation(Reservation reservation, ArrayList<ReservedDish> reservedDish){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("reservations/");
        String key = myRef.push().getKey();
        reservation.setReservationId(key);

        myRef = database.getReference("reservations/"+ key);
        myRef.setValue(reservation, this);
    }

    public boolean waitForResult() {
        lock.lock();
        if(!resultReturned) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
            }
        }
        return isOk;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            lock.lock();
            resultReturned = true;
            isOk = (databaseError == null);
            this.cv.signal();
            lock.unlock();
    }
}
