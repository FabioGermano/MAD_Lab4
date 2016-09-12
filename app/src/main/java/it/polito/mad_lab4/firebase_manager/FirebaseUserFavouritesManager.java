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
public class /FirebaseUserFavouritesManager implements DatabaseReference.CompletionListener {

    public void removeLike(String userId, String restaurantId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("favourites/"+userId+"/"+restaurantId);
        myRef.setValue(null, this);

    }

    public void saveLike(String userId, String restaurantId){
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("like", true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("favourites/"+userId+"/"+restaurantId);
        myRef.setValue(value, this);

    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
    }
}
