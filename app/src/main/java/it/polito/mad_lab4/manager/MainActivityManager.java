package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import it.polito.mad_lab4.*;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantProfileManager;
import it.polito.mad_lab4.manager.reservation.ReservationsActivity;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.restaurant.reviews_prev.ReviewsPrevFragment;

public class MainActivityManager extends it.polito.mad_lab4.BaseActivity{

    /*private PhotoViewer photoViewer;
    private Bitmap largeBitmap;*/

    private String name, email;
    private FirebaseUser currentUser;
    private Bitmap logo;
    private Button showAllReviewsButton;
    private ReviewsPrevFragment reviewsPrevFragment;
    private String restaurantId = "5QLyzE18mNe434WBhs1KtnYPAWw2"; //TODO hardcode
    private Restaurant restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else {
            // Implement this feature without material design
        }



        setContentView(R.layout.activity_main_manager);
        setToolbarColor();
        setActivityTitle("Gestione Ristorante");
        setBackButtonVisibility(false);
        setVisibilityAlert(false);
        invalidateOptionsMenu();
        setIconaToolbar(true);

        View header = null;


        CardView cv = (CardView) findViewById(R.id.add_new_offer);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ModifyOfferDish.class);
                i.putExtra("isEditing", false );
                startActivity(i);
            }
        });

        reviewsPrevFragment = (ReviewsPrevFragment)getSupportFragmentManager().findFragmentById(R.id.reviewsPrevFragment);
        reviewsPrevFragment.setRestaurantId(this.restaurantId);
        //provaGson();

        /*showAllReviewsButton = (Button)findViewById(R.id.showAllReviewsButton);
        showAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllReviewsButtonPressed();
            }
        });*/
    }

    /*private void provaGson() {
        GestioneDB db = new GestioneDB();

        ReservationEntity res = db.getAllReservations(getApplicationContext());
    }*/

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    protected void onResume() {
        super.onResume();

        showProgressBar();
        new Thread() {
            public void run() {
                FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                firebaseGetAuthInformation.waitForResult();
                currentUser = firebaseGetAuthInformation.getUser();

                if(currentUser == null) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            setVisibilityAlert(false);
                            invalidateOptionsMenu();

                        }
                    });
                }

            }
        }.start();

        showProgressBar();


        new Thread() {
            public void run() {
                FirebaseGetRestaurantProfileManager firebaseGetManProfileManager = new FirebaseGetRestaurantProfileManager();
                firebaseGetManProfileManager.getRestaurant(restaurantId);
                final boolean timeout = firebaseGetManProfileManager.waitForResult();
                restaurant = firebaseGetManProfileManager.getResult();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(timeout){
                            Snackbar.make(findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                                    .show();
                        }

                        dismissProgressDialog();
                        if(restaurant != null)
                            initSection();
                        else {
                            Toast.makeText(MainActivityManager.this, "Connection error", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });
            }
        }.start();

    }
    void initSection(){
       if(restaurant.getNumReviews()>0){
            ((LinearLayout)findViewById(R.id.reviewsFragmentContainer)).setVisibility(View.VISIBLE);
            reviewsPrevFragment.setRanking(restaurant.getTotRanking(), restaurant.getNumReviews());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5 && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?")
                    .setPositiveButton("NO", null)
                    .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}