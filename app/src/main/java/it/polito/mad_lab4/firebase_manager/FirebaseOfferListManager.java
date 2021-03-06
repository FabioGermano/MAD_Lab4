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

import it.polito.mad_lab4.manager.BlankOfferFragment;
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

    private boolean toReturn = false;
    private RecyclerAdapter_offerte adapter;
    private ArrayList<Offer> lista_offerte;
    private DatabaseReference myRef;
    private boolean timeout;
    private BlankOfferFragment[] fragments = new BlankOfferFragment[1];

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

    public boolean waitForResult() {
        lock.lock();
        try {
            if (!toReturn || timeout) {
                cv.await();
            }
        }
        catch(Exception e){
            Log.e(e.getMessage(), e.getMessage());
        }
        finally {
            lock.unlock();
        }

        return timeout;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue() == null){
            lock.lock();
            toReturn = true;
            this.cv.signal();
            lock.unlock();
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        lock.lock();
        this.cv.signal();
        toReturn = true;
        Offer o = dataSnapshot.getValue(Offer.class);
        lista_offerte.add(o);
        if(this.adapter != null) {
            this.adapter.notifyItemInserted(lista_offerte.size() - 1);
        }
        else if(fragments[0] != null){
            fragments[0].getAdapter().notifyItemInserted(lista_offerte.size() - 1);
        }
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
        if(this.adapter != null) {
            this.adapter.notifyItemRemoved(position);
            this.adapter.notifyItemRangeChanged(position, lista_offerte.size());
        }
        else if(fragments[0] != null){
            fragments[0].getAdapter().notifyItemRemoved(position);
            fragments[0].getAdapter().notifyItemRangeChanged(position, lista_offerte.size());
        }
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

    public void setFragments(BlankOfferFragment[] fragments) {
        this.fragments = fragments;
    }
}
