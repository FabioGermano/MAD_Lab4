package it.polito.mad_lab4.firebase_manager;

import android.util.Log;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import it.polito.mad_lab4.newData.restaurant.FavouritesRestaurantInfos;
import it.polito.mad_lab4.newData.restaurant.Review;

/**
 * Created by Giovanna on 06/06/2016.
 */
public class FirebaseGetFavouritesListManager  implements ValueEventListener {

    final Lock lock = new ReentrantLock();
    final Condition cv  = lock.newCondition();

    private ArrayList<String> favourites = new ArrayList<>();
    private ArrayList<FavouritesRestaurantInfos> infos = new ArrayList<>();

    private boolean resultReturned = false;

    private Query query;

    public void getFavourites(final String userId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("favourites").child(userId).addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        lock.lock();
        if(dataSnapshot.getValue() != null) {

            for (DataSnapshot d : dataSnapshot.getChildren()) {
                this.favourites.add(d.getKey());
            }
        }
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // TODO manage errors
        lock.lock();
        infos=null;
        resultReturned = true;
        this.cv.signal();
        lock.unlock();
    }

    public ArrayList<String> getResult() {
        return favourites;
    }

    public void waitForResult() {
        lock.lock();
        if(!resultReturned) {
            try {
                cv.await();
            } catch (InterruptedException e) {
                Log.e(e.getMessage(), e.getMessage());
            }
        }
    }

    public void terminate() {
        query.removeEventListener(this);
    }
}
