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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.io.Console;

import it.polito.mad_lab4.*;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantProfileManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUserInfoManager;
import it.polito.mad_lab4.manager.reservation.ReservationsActivity;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.user.User;
import it.polito.mad_lab4.restaurant.gallery.PhotoGaleryActivity;
import it.polito.mad_lab4.restaurant.reviews.ReviewsActivity;
import it.polito.mad_lab4.restaurant.reviews_prev.ReviewsPrevFragment;

public class MainActivityManager extends it.polito.mad_lab4.BaseActivity implements View.OnClickListener{

    /*private PhotoViewer photoViewer;
    private Bitmap largeBitmap;*/

    private String name, email;
    private FirebaseUser currentUser;
    private Bitmap logo;
    private Button showAllReviewsButton;
    private ReviewsPrevFragment reviewsPrevFragment;
    private String restaurantId = null;
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
        setActivityTitle(getResources().getString(R.string.titolo_main_manager));
        setBackButtonVisibility(false);
        setVisibilityAlert(false);
        invalidateOptionsMenu();
        setIconaToolbar(true);

        LinearLayout offer = (LinearLayout) findViewById(R.id.new_offer);
        offer.setOnClickListener(MainActivityManager.this);

        LinearLayout reviews = (LinearLayout) findViewById(R.id.go_to_reviews);
        reviews.setOnClickListener(MainActivityManager.this);


        LinearLayout photos = (LinearLayout) findViewById(R.id.go_to_photos);
        photos.setOnClickListener(MainActivityManager.this);
        //CardView cv = (CardView) findViewById(R.id.add_new_offer);
        /*cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if (restaurantId != null) {
                        Intent i = new Intent(getApplicationContext(), ModifyOfferDish.class);
                        Bundle b = new Bundle();
                        b.putString("restaurantId", restaurantId);
                        b.putBoolean("isEditing", false);
                        i.putExtras(b);
                        startActivity(i);
                    }
                    else
                        Toast.makeText(MainActivityManager.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
                else
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.connection_error), Snackbar.LENGTH_LONG).show();
            }
        });




        showAllReviewsButton = (Button)findViewById(R.id.showAllReviewsButton);
        showAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllReviewsButtonPressed();
            }
        });*/
    }

    @Override
    public void onClick(View v) {

        if (!isNetworkAvailable()){
            Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.connection_error), Snackbar.LENGTH_LONG).show();
            return;
        }
        switch (v.getId()){
            case R.id.new_offer:
                addNewOffer();
                break;
            case R.id.go_to_photos:
                showGallery();
                break;
            case R.id.go_to_reviews:
                showReviews();
                break;
        }
    }

    private void addNewOffer(){

            if (restaurantId != null) {
                Intent i = new Intent(getApplicationContext(), ModifyOfferDish.class);
                Bundle b = new Bundle();
                b.putString("restaurantId", restaurantId);
                b.putBoolean("isEditing", false);
                i.putExtras(b);
                startActivity(i);
            }

    }

    private void showGallery (){
        if (restaurantId != null) {
            Intent i = new Intent(getApplicationContext(), PhotoGaleryActivity.class);
            i.putExtra("restaurantId", this.restaurantId);
            i.putExtra("isManager", true);
            startActivity(i);
        }
    }


    private void showReviews() {


        Intent i = new Intent(getApplicationContext(), ReviewsActivity.class);
        i.putExtra("restaurantId", this.restaurantId);
        i.putExtra("ranking",restaurant.getTotRanking());
        i.putExtra("numRanking", restaurant.getNumReviews());
        i.putExtra("noAlertButton", true);
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //showProgressBar();
        new Thread() {
            public void run() {


                FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                firebaseGetAuthInformation.waitForResult();
                currentUser = firebaseGetAuthInformation.getUser();


                if (currentUser == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setVisibilityAlert(false);
                            invalidateOptionsMenu();

                        }
                    });
                } else {
                    FirebaseGetUserInfoManager userInfoManager = new FirebaseGetUserInfoManager();
                    userInfoManager.getClientInfo(currentUser.getUid());
                    userInfoManager.waitForResult();
                    final User infoUser = userInfoManager.getUserInfo();

                    restaurantId = currentUser.getUid();
                    //reviewsPrevFragment = (ReviewsPrevFragment)getSupportFragmentManager().findFragmentById(R.id.reviewsPrevFragment);
                    //reviewsPrevFragment.setRestaurantId(restaurantId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (infoUser != null) {
                                final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                                if (navigationView != null) {
                                    View header = navigationView.getHeaderView(0);

                                    if (header != null) {
                                        ImageView user_logo = (ImageView) header.findViewById(R.id.nav_drawer_logo);
                                        TextView user_name = (TextView) header.findViewById(R.id.nav_drawer_name);

                                        if (user_logo != null && infoUser.getAvatarDownloadLink() != null)
                                            Glide.with(getApplicationContext()).load(infoUser.getAvatarDownloadLink()).centerCrop().into(user_logo);
                                        if (user_name != null && infoUser.getName() != null)
                                            user_name.setText(infoUser.getName());
                                    }
                                }
                            }
                        }
                    });
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                FirebaseGetRestaurantProfileManager firebaseGetManProfileManager = new FirebaseGetRestaurantProfileManager();
                firebaseGetManProfileManager.getRestaurant(restaurantId);
                final boolean timeout = firebaseGetManProfileManager.waitForResult();
                restaurant = firebaseGetManProfileManager.getResult();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (timeout) {
                            Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.connection_error), Snackbar.LENGTH_LONG).show();
                        }
                        dismissProgressDialog();
                        if (restaurant != null){
                            //initSection();
                            }
                        else {
                            Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.connection_error), Snackbar.LENGTH_LONG).show();
                            return;
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
            reviewsPrevFragment.updateData();

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