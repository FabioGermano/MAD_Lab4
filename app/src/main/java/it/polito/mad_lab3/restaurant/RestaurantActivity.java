package it.polito.mad_lab3.restaurant;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.restaurant.foodPhoto.UserPhotoFragment;

public class  RestaurantActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    UserPhotoFragment photo, photo2;

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

        //photo = (UserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.photo1);
        //photo2 = (UserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.photo2);
        //photo.setImageByDrawable(R.drawable.cibo1);
        //photo2.setImageByDrawable(R.drawable.cibo2);

        LinearLayout llOffers = (LinearLayout)findViewById(R.id.LLOffersPrev);
        View child = getLayoutInflater().inflate(R.layout.offer_prev_view, null);
        llOffers.addView(child);
        child = getLayoutInflater().inflate(R.layout.offer_prev_view, null);
        llOffers.addView(child);
    }

}
