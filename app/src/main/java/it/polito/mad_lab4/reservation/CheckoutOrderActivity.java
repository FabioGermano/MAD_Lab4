package it.polito.mad_lab4.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.reservation.ReservationType;
import it.polito.mad_lab4.data.reservation.ReservationTypeConverter;
import it.polito.mad_lab4.firebase_manager.FirebaseGetClientInfoManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveReservationManager;
import it.polito.mad_lab4.newData.client.ClientPersonalInformation;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservedDish;

/**
 * Created by Giovanna on 28/04/2016.
 */
public class CheckoutOrderActivity extends BaseActivity {

    private ArrayList<ReservedDish> reservedDishes;
    private TextView dateTextView, timeTextView, seatsTextView, nameTextView, totalTextView;
    private EditText notesTextView;
    private String date, time, weekday,restaurantName;
    private int seatsNumber;
    private String restaurantID = null;
    private FloatingActionButton confirmFab;
    private LinearLayout orderLayout;
    private float total=0;
    private String currentUserId;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.your_reservation));

        setVisibilityAlert(false);
        invalidateOptionsMenu();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            date = null;
            time = null;
            weekday = null;
            restaurantName=null;
            restaurantID = null;
            seatsNumber = 0;

        } else {
            date = extras.getString("date");
            time = extras.getString("time");
            weekday = extras.getString("weekday");
            seatsNumber = extras.getInt("seats");
            restaurantName= extras.getString("restaurantName");
            restaurantID = extras.getString("restaurantId");
            currentUserId = extras.getString("userId");
            address = extras.getString("address");
        }

        reservedDishes = (ArrayList<ReservedDish>) getIntent().getSerializableExtra("reservedDishes");

        orderLayout = (LinearLayout) findViewById(R.id.order);
        dateTextView = (TextView) findViewById(R.id.reservation_date);
        timeTextView = (TextView) findViewById(R.id.reservation_time);
        seatsTextView = (TextView) findViewById(R.id.reservation_seats);
        nameTextView = (TextView) findViewById(R.id.restaurant_name);
        totalTextView = (TextView) findViewById(R.id.total);
        notesTextView = (EditText) findViewById(R.id.notes);

        confirmFab = (FloatingActionButton) findViewById(R.id.confirm_order);
        confirmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReservation();
            }
        });

        if (date != null && time != null) {
            dateTextView.setText(Helper.formatDate(getApplicationContext(), weekday, date));
            timeTextView.setText(time);
            nameTextView.setText(restaurantName);
        }
        if (seatsNumber != 0) {
            seatsTextView.setText(String.valueOf(seatsNumber) + " " + getResources().getString(R.string.seats_string));
        } else {
            seatsTextView.setVisibility(View.GONE);
        }
        if(reservedDishes!=null){
            fillLayout(reservedDishes);
            totalTextView.setText(getResources().getString(R.string.total)+" "+ String.valueOf(total)+" €");
        }
        else{
            //a reservation with only the seats specified
            totalTextView.setVisibility(View.GONE);
        }


    }

    private void saveReservation(){
        final Reservation r = new Reservation();
        r.setDate(date);
        r.setTime(time);
        r.setStatus(ReservationTypeConverter.toString(ReservationType.PENDING));
        r.setPlaces(String.valueOf(seatsNumber));
        r.setRestaurantId(restaurantID);
        r.setUserId(currentUserId);
        r.setNoteByUser(notesTextView.getText().toString());
        r.setTotalIncome(total);
        r.setReservedDishes(reservedDishes);
        r.setAddress(address);
        r.setRestaurantName(restaurantName);
        r.setRestaurantIdAndDate(restaurantID + " " + date);

        new Thread()
        {
            public void run() {

                FirebaseGetClientInfoManager clientManager = new FirebaseGetClientInfoManager();
                clientManager.getClientInfo(currentUserId);
                clientManager.waitForResult();
                ClientPersonalInformation client = clientManager.getResult();

                r.setUserName(client.getName());
                r.setPhone(client.getPhoneNumber());
                r.setAvatarDownloadLink(client.getAvatarDownloadLink());

                FirebaseSaveReservationManager firebaseSaveReservationManager = new FirebaseSaveReservationManager();
                firebaseSaveReservationManager.saveReservation(r, reservedDishes);

                boolean res = firebaseSaveReservationManager.waitForResult();

                if(!res){
                    Log.e("FirebaseSaveReservation", "Error saving the reservation");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.reservation_added), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        }.start();
    }

    public void fillLayout(ArrayList<ReservedDish> list) {
        for (ReservedDish d : list) {
            if(d.getQuantity()>0){
                View child = LayoutInflater.from(getBaseContext()).inflate(R.layout.your_order_row, null);
                TextView name = (TextView) child.findViewById(R.id.food_name);
                name.setText(d.getName());
                TextView quantity = (TextView) child.findViewById(R.id.food_quantity);
                quantity.setText(d.getQuantity() + " x ");
                TextView price = (TextView) child.findViewById(R.id.food_price);
                price.setText(String.valueOf(d.getPrice()) + " €");
                this.total+=d.getQuantity() * d.getPrice();
                this.orderLayout.addView(child);
            }
        }
    }

}