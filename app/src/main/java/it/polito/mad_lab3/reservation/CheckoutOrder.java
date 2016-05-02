package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.reservation.Dish;

/**
 * Created by Giovanna on 28/04/2016.
 */
public class CheckoutOrder extends BaseActivity {

    private ArrayList<Dish> offers;
    private ArrayList<Dish> main;
    private ArrayList<Dish> second;
    private ArrayList<Dish> dessert;
    private ArrayList<Dish> other;
    private TextView dateTextView, timeTextView, seatsTextView;
    private TextView txt;
    private String date, time, weekday;
    private int seatsNumber;
    private int restaurantID = -1;
    private LinearLayout orderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_order);

        setActivityTitle("Your order");

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            date = null;
            time = null;
            weekday = null;
            restaurantID = -1;
            seatsNumber = 0;

        } else {
            date = extras.getString("date");
            time = extras.getString("time");
            weekday = extras.getString("weekday");
            seatsNumber = extras.getInt("seats");
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

        if (date != null && time != null) {
            dateTextView.setText(Helper.formatDate(getApplicationContext(), weekday, date));
            timeTextView.setText(time);
        }
        if (seatsNumber != 0) {
            seatsTextView.setText(String.valueOf(seatsNumber) + " " + getResources().getString(R.string.seats_string));
            //collapsingToolbarLayout.setTitle("Order for eating in");
        } else {
            seatsTextView.setVisibility(View.GONE);
            //collapsingToolbarLayout.setTitle("Order for take-away");
        }
        fillLayout(main);
        fillLayout(second);
        fillLayout(dessert);
        fillLayout(other);
        fillLayout(offers);

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    public void fillLayout(ArrayList<Dish> list) {
        for (Dish d : list) {
            View child = LayoutInflater.from(getBaseContext()).inflate(R.layout.your_order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText(d.getDishName());
            TextView quantity = (TextView) child.findViewById(R.id.food_quantity);
            quantity.setText(d.getQuantity() + " x ");
            TextView price = (TextView) child.findViewById(R.id.food_price);
            price.setText(String.valueOf(d.getQuantity() * d.getPrice()) + " €");
            this.orderLayout.addView(child);
        }
    }

}