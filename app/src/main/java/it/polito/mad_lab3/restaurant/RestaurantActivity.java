package it.polito.mad_lab3.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.foodPhoto.ContainerUserPhotoFragment;
import it.polito.mad_lab3.restaurant.menu.MenuActivity;
import it.polito.mad_lab3.restaurant.menu_prev.MenuListPrevFragment;
import it.polito.mad_lab3.restaurant.menu_prev.MenuPrevFragment;

public class  RestaurantActivity extends BaseActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ContainerUserPhotoFragment containerUserPhotoFragment;
    private Restaurant restaurant;
    private MenuPrevFragment menuPrevFragment;
    private User user;
    private Button showAllMenuButton;

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

        ((ImageView)findViewById(R.id.mapIcon)).setColorFilter(Color.BLACK);
        ((ImageView)findViewById(R.id.phoneIcon)).setColorFilter(Color.BLACK);

  /*      LinearLayout llOffers = (LinearLayout)findViewById(R.id.LLOffersPrev);
        View child = getLayoutInflater().inflate(R.layout.offer_prev_view, null);
        llOffers.addView(child);
        child = getLayoutInflater().inflate(R.layout.offer_prev_view, null);
        //test
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ShowOfferActivity.class);
                int offerId=1;
                i.putExtra("offerId", offerId);
                startActivity(i);

            }
        });
        llOffers.addView(child);
*/
        restaurant = RestaurantBL.getRestaurantById(getBaseContext(), 1);

        containerUserPhotoFragment = (ContainerUserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.UserPhotoFragment);
        containerUserPhotoFragment.init(restaurant);

        menuPrevFragment = (MenuPrevFragment)getSupportFragmentManager().findFragmentById(R.id.menuPrevFragment);
        menuPrevFragment.setRestaurantId(restaurant.getRestaurantId());

        showAllMenuButton = (Button)findViewById(R.id.showAllMenuButton);
        showAllMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllMenuButtonPressed();
            }
        });

    }

    private void showAllMenuButtonPressed() {
        Intent i = new Intent(getBaseContext(), MenuActivity.class);
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
