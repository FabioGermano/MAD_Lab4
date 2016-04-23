package it.polito.mad_lab3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.Button;

import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.RestaurantActivity;

public class MainActivity extends AppCompatActivity {

    private Button restaurantBtn, reservationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restaurantBtn = (Button) findViewById(R.id.restaurantBtn);
        reservationBtn = (Button) findViewById(R.id.reservationBtn);

        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), RestaurantActivity.class);
                startActivity(i);
            }
        });

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ReservationActivity.class);
                startActivity(i);
            }
        });
    }
}
