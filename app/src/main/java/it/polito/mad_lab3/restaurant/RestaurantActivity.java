package it.polito.mad_lab3.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.MainActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.foodPhoto.ContainerUserPhotoFragment;
import it.polito.mad_lab3.restaurant.menu.MenuActivity;
import it.polito.mad_lab3.restaurant.menu_prev.MenuPrevFragment;
import it.polito.mad_lab3.restaurant.offer_prev.OfferPrevFragment;
import it.polito.mad_lab3.restaurant.reviews.ReviewsActivity;
//import it.polito.mad_lab3.restaurant.reviews_prev.ReviewsPrevFragment;

public class  RestaurantActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ContainerUserPhotoFragment containerUserPhotoFragment;
    private Restaurant restaurant;
    private BasicInfoFragment basicInfoFragment;
    private MenuPrevFragment menuPrevFragment;
    private OfferPrevFragment offerPrevFragment;
    private User user;
    private Button showAllMenuButton, showAllReviewsButton;
    //private ReviewsPrevFragment reviewsPrevFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        useToolbar(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Demo");
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.reservation);


        Bundle extras = getIntent().getExtras();
        int id = -1;
        if (extras != null) {
            id = (int) extras.getInt("idRestaurant");
            extras.clear();
        }
        if(id == -1){
            Toast toast = Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_SHORT);
            toast.show();
            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);
        }
        //System.out.println("Id ricevuto: " + id);
        restaurant = RestaurantBL.getRestaurantById(getBaseContext(), id);
        //System.out.println("Ristorante: " + restaurant.getRestaurantName());

        containerUserPhotoFragment = (ContainerUserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.UserPhotoFragment);
        containerUserPhotoFragment.init(restaurant);

        basicInfoFragment = (BasicInfoFragment) getSupportFragmentManager().findFragmentById(R.id.basicinfo_fragment);
        basicInfoFragment.setRestaurantId(restaurant.getRestaurantId());


        menuPrevFragment = (MenuPrevFragment)getSupportFragmentManager().findFragmentById(R.id.menuPrevFragment);
        menuPrevFragment.setRestaurantId(restaurant.getRestaurantId());

        offerPrevFragment = (OfferPrevFragment)getSupportFragmentManager().findFragmentById(R.id.offersPrevFragment);
        offerPrevFragment.init(restaurant.getRestaurantId());

        //reviewsPrevFragment = (ReviewsPrevFragment)getSupportFragmentManager().findFragmentById(R.id.reviewsPrevFragment);
        //reviewsPrevFragment.init(restaurant.getRestaurantId());

        showAllMenuButton = (Button)findViewById(R.id.showAllMenuButton);
        showAllMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllMenuButtonPressed();
            }
        });

        /*showAllReviewsButton = (Button)findViewById(R.id.showAllReviewsButton);
        showAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllReviewsButtonPressed();
            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newReservation();
            }
        });

    }

    @Override
    protected void filterButton() {

    }

    private void showAllReviewsButtonPressed() {
        Intent i = new Intent(getBaseContext(), ReviewsActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }

    private void showAllMenuButtonPressed() {
        Intent i = new Intent(getBaseContext(), MenuActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }

    private void newReservation() {
        Intent i = new Intent(getBaseContext(), ReservationActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }


    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }
}
