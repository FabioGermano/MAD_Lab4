package it.polito.mad_lab4.firebase_manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

/**
 * Created by f.germano on 29/05/2016.
 */
public class FirebaseRemoveDishManager {
    public void removeDish(final String RestaurantId, final String dishId){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("menu/" + RestaurantId + "/" + dishId);
        myRef.setValue(null);
    }
}
