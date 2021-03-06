package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.reservation.Reservation;


/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseGetReservationsManager implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private ArrayList<Reservation> reservations = new ArrayList<>();
    private boolean resultReturned = false;
    private boolean timeout;

    private Query query;

    public void getReservations(String userId, String restaurantId, String date){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        query = database.getReference("reservations/");
        if(userId != null) {
            query = query.orderByChild("userId")
                    .equalTo(userId);
        }
        else
        {
            query = query.orderByChild("restaurantIdAndDate")
                    .equalTo(restaurantId + " " + date);

        }
        query.addListenerForSingleValueEvent(this);

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
        if(dataSnapshot.getValue() != null) {
            lock.lock();
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                this.reservations.add(d.getValue(Reservation.class));
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

    public ArrayList<Reservation> getResult() {
        return reservations;
    }

    public boolean waitForResult() {
        lock.lock();
        try {
            if(!resultReturned || timeout)
                cv.await();
        } catch (InterruptedException e) {
            Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }
        return timeout;
    }

    public void terminate() {
        query.removeEventListener(this);
    }
}
