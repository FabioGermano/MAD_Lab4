package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.manager.Oggetto_menu;
import it.polito.mad_lab4.manager.RecyclerAdapter_offerte;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseOfferListManager implements ValueEventListener, ChildEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean resultReturned = false;
    private RecyclerAdapter_offerte adapter;
    private ArrayList<Offer> lista_offerte;
    private DatabaseReference myRef;

    public void setAdapter(RecyclerAdapter_offerte adapter){
        this.adapter = adapter;
    }

    public void startGetList(ArrayList<Offer> lista_offerte, String restaurantId){
        this.lista_offerte = lista_offerte;

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference refToOne = database.getReference("offers/" + restaurantId);
        refToOne.limitToFirst(1).addListenerForSingleValueEvent(this);

        myRef = database.getReference("offers/" + restaurantId);
        myRef.addChildEventListener(this);
    }

    public void waitForResult() {
        lock.lock();
        try {
            if (!resultReturned) {
                cv.await();
            }
        }
        catch(Exception e){
            Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() == null){
            lock.lock();
            this.cv.signal();
            lock.unlock();
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        lock.lock();
        this.cv.signal();
        Offer o = dataSnapshot.getValue(Offer.class);
        lista_offerte.add(o);
        this.adapter.notifyItemInserted(lista_offerte.size() - 1);
        lock.unlock();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Offer o = dataSnapshot.getValue(Offer.class);

        int position = findPositionOnList(lista_offerte, o.getOfferId());
        lista_offerte.remove(position);
        this.adapter.notifyItemRemoved(position);
        this.adapter.notifyItemRangeChanged(position, lista_offerte.size());
    }

    private int findPositionOnList(ArrayList<Offer> lista_offerte, String offerId) {
        int i = 0;
        for(Offer d : lista_offerte){
            if(d.getOfferId().equals(offerId)){
                return i;
            }
            i++;
        }

        return i;
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void detachListeners() {
        if(myRef != null){
            myRef.removeEventListener((ValueEventListener) this);
            myRef.removeEventListener((ChildEventListener) this);
        }
    }
}
