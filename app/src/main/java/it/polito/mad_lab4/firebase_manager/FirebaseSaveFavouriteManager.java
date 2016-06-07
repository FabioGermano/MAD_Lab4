package it.polito.mad_lab4.firebase_manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by f.germano on 28/05/2016.
 */
public class FirebaseSaveFavouriteManager implements DatabaseReference.CompletionListener {

    private boolean firebaseReturnedResult = false;
    private DatabaseError databaseError;

    public void removeFavourite(String userId, String restaurantId){

    }

    public void saveFavourite(String userId, String restaurantId){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("favourites/"+userId);
        String key = myRef.push().getKey();
        myRef = database.getReference("favourites/"+userId+"/"+key);
        myRef.setValue(restaurantId);

    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }
}
