package it.polito.mad_lab3.restaurant;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.restaurant.foodPhoto.ContainerUserPhotoFragment;
import it.polito.mad_lab3.restaurant.foodPhoto.UserPhotoFragment;

public class  RestaurantActivity extends BaseActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ContainerUserPhotoFragment containerUserPhotoFragment;
    RestaurantBL restaurantBL;
    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);


        useToolbar(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Demo");


        ((ImageView)findViewById(R.id.mapIcon)).setColorFilter(Color.BLACK);
        ((ImageView)findViewById(R.id.phoneIcon)).setColorFilter(Color.BLACK);

        LinearLayout llOffers = (LinearLayout)findViewById(R.id.LLOffersPrev);
        View child = getLayoutInflater().inflate(R.layout.offer_prev_view, null);
        llOffers.addView(child);
        child = getLayoutInflater().inflate(R.layout.offer_prev_view, null);
        llOffers.addView(child);

        restaurantBL = new RestaurantBL(getBaseContext());
        restaurant = restaurantBL.getRestaurantById(1);

        containerUserPhotoFragment = (ContainerUserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.UserPhotoFragment);
        containerUserPhotoFragment.setRestaurant(restaurant);
        containerUserPhotoFragment.setRestaurantBL(this.restaurantBL);
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

}
