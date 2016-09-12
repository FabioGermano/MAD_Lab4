package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.manager.BlankMenuFragment;
import it.polito.mad_lab4.manager.Oggetto_menu;
import it.polito.mad_lab4.manager.RecyclerAdapter_menu;
import it.polito.mad_lab4.manager.RecyclerAdapter_offerte;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by f.germano on 28/05/2016.
 */
public class /FirebaseMenuListManager implements ValueEventListener, ChildEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private boolean toReturn = false;
    private BlankMenuFragment[] fragments = new BlankMenuFragment[4];
    private Oggetto_menu lista_menu;
    private DatabaseReference myRef;
    private boolean timeout;

    public void setFragments(BlankMenuFragment[] fragments){
        this.fragments = fragments;
    }

    public void startGetList(Oggetto_menu lista_menu, String restaurantId){
        this.lista_menu = lista_menu;

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference refToOne = database.getReference("menu/" + restaurantId);
        refToOne.limitToFirst(1).addListenerForSingleValueEvent(this);

        myRef = database.getReference("menu/" + restaurantId);
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

        Dish d = dataSnapshot.getValue(Dish.class);
        int index = DishTypeConverter.fromEnumToIndex(DishTypeConverter.fromStringToEnum(d.getType()));
        lista_menu.getDishListByIndex(index).add(d);
        if(fragments[index] != null) {
            fragments[index].getAdapter().notifyItemInserted(lista_menu.getDishListByIndex(index).size() - 1);
        }
        lock.unlock();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Dish d = dataSnapshot.getValue(Dish.class);

        int index = DishTypeConverter.fromEnumToIndex(DishTypeConverter.fromStringToEnum(d.getType()));
        int position = findPositionOnList(lista_menu.getDishListByIndex(index), d.getDishId());
        lista_menu.getDishListByIndex(index).remove(position);
        if(fragments[index] != null) {
            fragments[index].getAdapter().notifyItemRemoved(position);
            fragments[index].getAdapter().notifyItemRangeChanged(position, lista_menu.getDishListByIndex(index).size());
        }

    }

    private int findPositionOnList(ArrayList<Dish> list, String dishId){
        int i = 0;
        for(Dish d : list){
            if(d.getDishId().equals(dishId)){
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
