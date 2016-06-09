package it.polito.mad_lab4.alert;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.data.reservation.ReservationType;
import it.polito.mad_lab4.data.reservation.ReservationTypeConverter;
import it.polito.mad_lab4.newData.user.Notification;

/**
 * Created by f.germano on 30/04/2016.
 */
public class UserAlert {

    private static Context context;
    private static boolean isInitialited = false;
    private static SharedPreferences prefs;
    private static TextView labelAlert;
    public static int count = 0;

    public static void init(Context _context, String userId, TextView _labelAlert){
        labelAlert = _labelAlert;

        if(context == null){
            context = _context;
            prefs = context.getSharedPreferences("pref", 0);
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
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == false){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == true){
                    count++;
                    labelAlert.setText(String.valueOf(count));
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
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == false){
                    count++;
                    labelAlert.setText(String.valueOf(count));
                    save(reservation);
                }
                else if(reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) &&
                        reservation.getIsVerified() == true){
                    count++;
                    labelAlert.setText(String.valueOf(count));
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

    public static void resetAlertCount() {
        if(prefs == null){
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("alertCount", 0);
        editor.commit();

        count = 0;
        labelAlert.setText("0");
    }
}
