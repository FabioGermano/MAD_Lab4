package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

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
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveAvailabilityManager implements DatabaseReference.CompletionListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();
    private boolean isOk;

    private boolean resultReturned = false;

    private DatabaseReference myRef;

    public void saveAvailability(Oggetto_menu lista_menu, ArrayList<Offer> lista_offerte){
        Map<String, Object> mergedUpdates = new HashMap<String, Object>();

        for(Offer o : lista_offerte){
            mergedUpdates.put("offers/" + "-KIrgaSxr9VhHllAjqmp/" + o.getOfferId() + "/isTodayAvailable", o.getIsTodayAvailable());
        }

        for(int i = 0; i<4; i++){
            for(Dish d : lista_menu.getDishListByIndex(i)){
                mergedUpdates.put("menu/" + "-KIrgaSxr9VhHllAjqmp/" + d.getDishId() + "/isTodayAvailable", d.getIsTodayAvailable());
            }
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.updateChildren(mergedUpdates, this);
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
