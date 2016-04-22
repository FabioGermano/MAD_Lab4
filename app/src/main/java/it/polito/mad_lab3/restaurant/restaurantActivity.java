package it.polito.mad_lab3.restaurant;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import it.polito.mad_lab3.R;

public class RestaurantActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle("Demo");

        setSupportActionBar(toolbar);

        ((ImageView)findViewById(R.id.mapIcon)).setColorFilter(Color.BLACK);
        ((ImageView)findViewById(R.id.phoneIcon)).setColorFilter(Color.BLACK);
    }
}
