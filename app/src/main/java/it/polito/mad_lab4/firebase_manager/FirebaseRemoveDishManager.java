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
 * Created by f.germano on 29/05/2016.
 */
public class FirebaseRemoveDishManager {
    public void removeDish(final String RestaurantId, final String dishId,  final float price){

        int numPiatti = -1;
        float totPrezzo = -1;
        boolean failed = false;

        FirebaseGetRestaurantInfoManager restaurantInfoManager;
        restaurantInfoManager = new FirebaseGetRestaurantInfoManager();
        restaurantInfoManager.getRestaurantInfo(RestaurantId, "totDishesAndOffers");
        restaurantInfoManager.waitForResult();

        if (restaurantInfoManager.getResult() != null && !restaurantInfoManager.getResult().equals("null"))
            totPrezzo = Float.parseFloat(restaurantInfoManager.getResult());
        else
            failed = true;

        restaurantInfoManager = new FirebaseGetRestaurantInfoManager();
        restaurantInfoManager.getRestaurantInfo(RestaurantId, "numDishesAndOffers");
        restaurantInfoManager.waitForResult();

        if (restaurantInfoManager.getResult() != null && !restaurantInfoManager.getResult().equals("null"))
            numPiatti = Integer.parseInt(restaurantInfoManager.getResult());
        else
            failed = true;


        if (!failed) {
            numPiatti--;
            totPrezzo = totPrezzo - price;
            Map updateAvg = new HashMap();

            updateAvg.put("numDishesAndOffers", numPiatti);
            updateAvg.put("totDishesAndOffers", Math.round(totPrezzo));

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("menu/" + RestaurantId + "/" + dishId);
            myRef.setValue(null);

            DatabaseReference myRef1 = database.getReference("restaurants/" + RestaurantId);
            myRef1.updateChildren(updateAvg);
        }














    }
}
