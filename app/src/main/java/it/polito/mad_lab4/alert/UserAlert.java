package it.polito.mad_lab4.alert;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.data.reservation.ReservationType;
import it.polito.mad_lab4.data.reservation.ReservationTypeConverter;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.user.Notification;

/**
 * Created by f.germano on 30/04/2016.
 */
public class UserAlert {

    private static Context context;
    public static boolean isInitialited = false;
    private static SharedPreferences prefs;
    private static TextView labelAlert;
    public static int count = 0;
    public static String userId;

    static private MediaPlayer mp;


    public static void init(Context _context, String _userId, TextView _labelAlert){
        labelAlert = _labelAlert;




        if(context == null){
            context = _context;
            prefs = context.getSharedPreferences(_userId, 0);
            mp = MediaPlayer.create(context, R.raw.notification_sound);
        }

        if(prefs != null){
            count = prefs.getInt("alertCount", 0);
        }

        if(labelAlert != null) {
            labelAlert.setText(String.valueOf(count));
        }

        if(isInitialited){
            return;
        }

        isInitialited = true;
        userId = _userId;

        manageOffers();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("reservations/");
        query = query.orderByChild("userId").equalTo(userId);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Reservation reservation = dataSnapshot.getValue(Reservation.class);

                if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.DELETED)) ||
                        reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.PENDING))){
                    return;
                }

                if(reservation.getNotified()){
                    return;
                }

                if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.REJECTED))){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    mp.start();
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == false){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    mp.start();
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == true){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    mp.start();
                    save(reservation);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Reservation reservation = dataSnapshot.getValue(Reservation.class);

                if(reservation.getNotified()){
                    return;
                }

                if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.REJECTED))){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    mp.start();
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == false){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    mp.start();
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == true){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    mp.start();
                    save(reservation);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void manageOffers() {
        final Map<String,Boolean> offersAlert = loadMap();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        mDatabase.child("favourites").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {

                    for (final DataSnapshot d : dataSnapshot.getChildren()) {
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = database.getReference("offers/"+d.getKey());
                        mDatabase.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Offer o = dataSnapshot.getValue(Offer.class);
                                if(offersAlert.get(o.getOfferId()) != null){
                                    return;
                                }

                                count++;
                                //TODO errore null object su count
                                labelAlert.setText(String.valueOf(count));
                                mp.start();
                                offersAlert.put(o.getOfferId(), false);
                                saveMap(offersAlert);

                                save(o, d.getKey());
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static void save(Reservation reservation) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations/"+reservation.getReservationId()+"/notified");
        myRef.setValue(true);

        Notification alertInfo = new Notification();
        alertInfo.setRestaurantName(reservation.getRestaurantName());
        alertInfo.setAccepted(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)));
        alertInfo.setMessage(reservation.getNoteByOwner());
        alertInfo.setNewOffer(false);
        alertInfo.setOfferId(null);
        alertInfo.setRestaurantId(null);

        myRef = database.getReference("userAlerts/"+reservation.getUserId());
        String key = myRef.push().getKey();
        myRef = myRef.child(key);
        myRef.setValue(alertInfo);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("alertCount", count);
        editor.commit();
    }

    private static void save(final Offer offer, final String restaurantId) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurants/"+restaurantId+"/restaurantName");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notification alertInfo = new Notification();
                alertInfo.setMessage(null);
                alertInfo.setNewOffer(true);
                alertInfo.setOfferId(offer.getOfferId());
                alertInfo.setRestaurantName((String)dataSnapshot.getValue());
                alertInfo.setRestaurantId(restaurantId);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userAlerts/"+userId);
                String key = myRef.push().getKey();
                myRef = myRef.child(key);
                myRef.setValue(alertInfo);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("alertCount", count);
                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void resetAlertCount() {
        if(prefs == null){
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("alertCount", 0);
        editor.commit();

        count = 0;
        //labelAlert.setText("0");
    }

    private static void saveMap(Map<String,Boolean> inputMap){
        if (prefs != null){
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("OffersAlert").commit();
            editor.putString("OffersAlert", jsonString);
            editor.commit();
        }
    }

    private static Map<String,Boolean> loadMap(){
        Map<String,Boolean> outputMap = new HashMap<String,Boolean>();
        try{
            if (prefs != null){
                String jsonString = prefs.getString("OffersAlert", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Boolean value = (Boolean) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
