package it.polito.mad_lab4.firebase_manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by f.germano on 29/05/2016.
 */
public class FirebaseRemoveOfferManager {
    public void removeOffer(final String RestaurantId, final String offerId){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("offers/" + RestaurantId + "/" + offerId);
        myRef.setValue(null);
    }
}
