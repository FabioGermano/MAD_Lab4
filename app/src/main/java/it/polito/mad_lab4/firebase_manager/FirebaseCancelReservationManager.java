package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.data.reservation.ReservationType;
import it.polito.mad_lab4.data.reservation.ReservationTypeConverter;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservedDish;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseCancelReservationManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();
    private boolean isOk;

    private boolean resultReturned = false;

    private DatabaseReference myRef;

    public void cancelReservation(Reservation reservation){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.PENDING))) {
            myRef = database.getReference("reservations/" + reservation.getReservationId());
            myRef.setValue(null);
        }
        else{
            myRef = database.getReference("reservations/" + reservation.getReservationId() + "/status");
            myRef.setValue(ReservationTypeConverter.toString(ReservationType.DELETED));
        }


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
