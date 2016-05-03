package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.reservation.ReservedDish;

/**
 * Created by Giovanna on 28/04/2016.
 */
public class CheckoutOrder extends BaseActivity {

    private ArrayList<ReservedDish> offers, main, second, dessert, other;
    private TextView dateTextView, timeTextView, seatsTextView, nameTextView, totalTextView;
    private EditText notesTextView;
    private String date, time, weekday,restaurantName;
    private int seatsNumber;
    private int restaurantID = -1;
    private FloatingActionButton confirm;
    private LinearLayout orderLayout;
    private float total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);

        setActivityTitle(getResources().getString(R.string.your_reservation));

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            date = null;
            time = null;
            weekday = null;
            restaurantName=null;
            restaurantID = -1;
            seatsNumber = 0;

        } else {
            date = extras.getString("date");
            time = extras.getString("time");
            weekday = extras.getString("weekday");
            seatsNumber = extras.getInt("seats");
            restaurantName= extras.getString("restaurantName");
            restaurantID = extras.getInt("restaurant");
        }

        offers = getIntent().getParcelableArrayListExtra("offers");
        main = getIntent().getParcelableArrayListExtra("main");
        second = getIntent().getParcelableArrayListExtra("second");
        dessert = getIntent().getParcelableArrayListExtra("dessert");
        other = getIntent().getParcelableArrayListExtra("other");

        orderLayout = (LinearLayout) findViewById(R.id.order);
        dateTextView = (TextView) findViewById(R.id.reservation_date);
        timeTextView = (TextView) findViewById(R.id.reservation_time);
        seatsTextView = (TextView) findViewById(R.id.reservation_seats);
        nameTextView = (TextView) findViewById(R.id.restaurant_name);
        totalTextView = (TextView) findViewById(R.id.total);
        notesTextView = (EditText) findViewById(R.id.notes);

        confirm = (FloatingActionButton) findViewById(R.id.confirm_order);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Add the pending reservation to the db
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
        if(offers!= null && main!=null && second != null && dessert!=null && other!=null) {
            fillLayout(main);
            fillLayout(second);
            fillLayout(dessert);
            fillLayout(other);
            fillLayout(offers);
            totalTextView.setText(getResources().getString(R.string.total)+" "+ String.valueOf(total)+" €");
        }
        else{
            //a reservation with only the seats specified
            totalTextView.setVisibility(View.GONE);
        }


    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

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