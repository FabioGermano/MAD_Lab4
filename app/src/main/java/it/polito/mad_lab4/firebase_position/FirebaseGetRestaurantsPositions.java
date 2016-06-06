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

/**
 * Created by Euge on 06/06/2016.
 */
public class FirebaseGetRestaurantsPositions implements ValueEventListener {
    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;

    private ArrayList<Oggetto_offerteVicine> listaOfferte = null;


    public FirebaseGetRestaurantsPositions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("positions").addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String idRistorante;
        Position p;
        Oggetto_offerteVicine obj;
        lock.lock();
        listaOfferte = new ArrayList<Oggetto_offerteVicine>();
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            idRistorante = postSnapshot.getKey();
            System.out.println(postSnapshot.toString());
            p = postSnapshot.getValue(Position.class);
            obj = new Oggetto_offerteVicine(null, idRistorante);
            obj.setRestaurantPosition(p);
            listaOfferte.add(obj);
        }

        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        lock.lock();
        listaOfferte = null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public ArrayList<Oggetto_offerteVicine> getListaOfferte(){
        return this.listaOfferte;
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
}

