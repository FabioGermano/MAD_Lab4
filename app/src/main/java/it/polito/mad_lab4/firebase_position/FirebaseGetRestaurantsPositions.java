package it.polito.mad_lab4.firebase_position;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_offerteVicine;
import it.polito.mad_lab4.newData.other.Position;
import it.polito.mad_lab4.newData.other.RestaurantPosition;

/**
 * Created by Euge on 06/06/2016.
 */
public class FirebaseGetRestaurantsPositions implements ValueEventListener {
    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;
    private boolean timeout;
    private DatabaseReference mDatabase;
    private ArrayList<RestaurantPosition> listaPosizioniRistoranti = null;


    public FirebaseGetRestaurantsPositions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mDatabase.child("positions").addListenerForSingleValueEvent(this);

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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String idRistorante;
        Position p;
        RestaurantPosition obj;
        lock.lock();
        listaPosizioniRistoranti = new ArrayList<RestaurantPosition>();
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            idRistorante = postSnapshot.getKey();
            System.out.println(postSnapshot.toString());
            p = postSnapshot.getValue(Position.class);
            obj = new RestaurantPosition();
            obj.setRestaurantId(idRistorante);
            obj.setPosition(p);
            listaPosizioniRistoranti.add(obj);
        }

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
        listaPosizioniRistoranti = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public ArrayList<RestaurantPosition> getListaOfferte(){
        return this.listaPosizioniRistoranti;
    }

    public boolean waitForResult() {
        lock.lock();
        try {
            if(!resultReturned|| timeout)
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
        mDatabase.removeEventListener(this);
    }
}

