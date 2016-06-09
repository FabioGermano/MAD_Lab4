package it.polito.mad_lab4.restaurant;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;


import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantProfileManager;
import it.polito.mad_lab4.firebase_manager.FirebaseUserFavouritesManager;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.reservation.ReservationActivity;
import it.polito.mad_lab4.restaurant.cover.CoverActivity;
import it.polito.mad_lab4.restaurant.foodPhoto.ContainerUserPhotoFragment;
import it.polito.mad_lab4.restaurant.menu.MenuActivity;
import it.polito.mad_lab4.restaurant.menu_prev.MenuPrevFragment;
import it.polito.mad_lab4.restaurant.offer_prev.OfferPrevFragment;
import it.polito.mad_lab4.restaurant.reviews.ReviewsActivity;
import it.polito.mad_lab4.restaurant.reviews.add_review.AddReviewActivity;
import it.polito.mad_lab4.restaurant.reviews_prev.ReviewsPrevFragment;

public class  RestaurantActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, ChildEventListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ContainerUserPhotoFragment containerUserPhotoFragment;
    private Restaurant restaurant;
    private String restaurantId;
    private BasicInfoFragment basicInfoFragment;
    private MenuPrevFragment menuPrevFragment;
    private OfferPrevFragment offerPrevFragment;
    private Button showAllMenuButton, showAllReviewsButton, reservation;
    private ReviewsPrevFragment reviewsPrevFragment;
    private ImageView coverImage;
    private FloatingActionMenu add;
    private android.support.design.widget.FloatingActionButton fab;
    private FloatingActionButton add_review, add_photo, add_reservation;
    private AppBarLayout appbar;
    public boolean favourite=false;
    private FirebaseUser currentUser;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        setVisibilityAlert(false);
        invalidateOptionsMenu();
        useToolbar(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        appbar= (AppBarLayout) findViewById(R.id.app_bar_layout);
        appbar.setExpanded(false);

        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeButtonPressed();
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themeColor)));

        add = (FloatingActionMenu) findViewById(R.id.add);

        add_review = (FloatingActionButton) findViewById(R.id.add_review);
        add_photo = (FloatingActionButton) findViewById(R.id.add_photo);
        add_reservation = (FloatingActionButton) findViewById(R.id.add_reservation);

        add.setVisibility(View.GONE);
        add.setClosedOnTouchOutside(true);

        add_photo.setOnClickListener(clickListener);
        add_reservation.setOnClickListener(clickListener);
        add_review.setOnClickListener(clickListener);

        Bundle extras = getIntent().getExtras();
        restaurantId = extras.getString("restaurantId");
        //this.restaurantId = "gAFr9RplBOdXm0O7jmUhJH4m98l1";


        menuPrevFragment = (MenuPrevFragment)getSupportFragmentManager().findFragmentById(R.id.menuPrevFragment);
        menuPrevFragment.setRestaurantId(restaurantId);

        offerPrevFragment = (OfferPrevFragment)getSupportFragmentManager().findFragmentById(R.id.offersPrevFragment);
        offerPrevFragment.init(restaurantId);

        reviewsPrevFragment = (ReviewsPrevFragment)getSupportFragmentManager().findFragmentById(R.id.reviewsPrevFragment);
        reviewsPrevFragment.setRestaurantId(this.restaurantId);

        showAllMenuButton = (Button)findViewById(R.id.showAllMenuButton);
        showAllMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllMenuButtonPressed();
            }
        });

        showAllReviewsButton = (Button)findViewById(R.id.showAllReviewsButton);
        showAllReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllReviewsButtonPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        add.close(true);
        appbar.addOnOffsetChangedListener(this);

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

                        initSections();
                    }
                });
            }
        }.start();

        new Thread() {
            public void run() {
                FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                firebaseGetAuthInformation.waitForResult();
                currentUser = firebaseGetAuthInformation.getUser();
                if(currentUser != null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("favourites/"+ currentUser.getUid()+"/"+restaurantId);
                    myRef.addChildEventListener(RestaurantActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            add.setVisibility(View.VISIBLE);
                            fab.setVisibility(View.VISIBLE);

                        }
                    });
                }

            }
        }.start();
    }

    void initSections(){
        collapsingToolbarLayout.setTitle(restaurant.getRestaurantName());
        /*if(restaurant.getCover1_thumbDownloadLink() != null ||
                restaurant.getCover2_thumbDownloadLink() != null ||
                restaurant.getCover3_thumbDownloadLink() != null ||
                restaurant.getCover4_thumbDownloadLink() != null) {
            appbar.setExpanded(true);
        }*/

        containerUserPhotoFragment = (ContainerUserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.UserPhotoFragment);
        containerUserPhotoFragment.init(restaurant);

        basicInfoFragment = (BasicInfoFragment) getSupportFragmentManager().findFragmentById(R.id.basicinfo_fragment);
        basicInfoFragment.setRestaurant(restaurant);

        if(restaurant.getNumReviews()>0){
            ((LinearLayout)findViewById(R.id.reviewsFragmentContainer)).setVisibility(View.VISIBLE);
            reviewsPrevFragment.setRanking(restaurant.getTotRanking(), restaurant.getNumReviews());
        }
        if(favourite){
            fab.setImageResource(R.drawable.ic_star_favourites_active);
        }
        else
            fab.setImageResource((R.drawable.ic_star_disabled));

        coverImage = (ImageView)findViewById(R.id.coverImage);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coverImageClick();
            }
        });
        if( restaurant!= null && !restaurant.isReservations())
            add_reservation.setVisibility(View.GONE);

        setCoverImage();

    }
    /*
    private void saveFavourite() {

        FirebaseSaveFavouriteManager firebaseSaveFavouriteManager = new FirebaseSaveFavouriteManager();
        firebaseSaveFavouriteManager.saveFavourite( currentUser.getUid(), restaurantId);
        fab.setImageResource(R.drawable.ic_star_favourites_active);

    }
    private void removeFavourite() {

        FirebaseSaveFavouriteManager firebaseSaveFavouriteManager = new FirebaseSaveFavouriteManager();
        firebaseSaveFavouriteManager.removeFavourite( currentUser.getUid(), restaurantId);
        fab.setImageResource(R.drawable.ic_star_disabled);

    }
    */
    private void setCoverImage() {
        for(int i = 0; i<4; i++){
            if(this.restaurant.getThumbCoverByIndex(i) != null){
                Glide.with(this).load(this.restaurant.getThumbCoverByIndex(i)).into(coverImage);
                break; // the first cover is shown
            }
        }
    }

    private void coverImageClick() {
        Intent i = new Intent(getApplicationContext(), CoverActivity.class);
        ArrayList<String> covers = new ArrayList<>();
        for(int j = 0;j<4;j++){
            String link= restaurant.getLargeCoverByIndex(j);
            if(link!=null){
                covers.add(link);
            }
        }
        i.putExtra("covers", (Serializable) covers);
        startActivity(i);
    }

    private void showAllReviewsButtonPressed() {
        Intent i = new Intent(getApplicationContext(), ReviewsActivity.class);
        i.putExtra("restaurantId", this.restaurantId);
        i.putExtra("ranking",restaurant.getTotRanking());
        i.putExtra("numRanking", restaurant.getNumReviews());
        startActivity(i);
    }

    private void showAllMenuButtonPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }

    private void likeButtonPressed() {
        FirebaseUserFavouritesManager firebaseUserFavouritesManager = new FirebaseUserFavouritesManager();
        if(favourite){
            firebaseUserFavouritesManager.removeLike( currentUser.getUid(), restaurantId);
        }
        else
        {
            firebaseUserFavouritesManager.saveLike( currentUser.getUid(), restaurantId);
        }
    }
    private void newReservation() {
        Intent i = new Intent(getApplicationContext(), ReservationActivity.class);
        i.putExtra("restaurant", this.restaurant);
        i.putExtra("coverLink", this.restaurant.getCover1_largeDownloadLink());
        i.putExtra("userId", this.currentUser.getUid());
        startActivity(i);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
            switch (v.getId()) {
                case R.id.add_photo:
                    containerUserPhotoFragment.newPhoto();
                    break;
                case R.id.add_review:
                    i= new Intent(getApplicationContext(), AddReviewActivity.class);
                    i.putExtra("restaurantId", restaurantId);
                    i.putExtra("restaurantName", restaurant.getRestaurantName());
                    startActivity(i);
                    break;
                case R.id.add_reservation:
                    newReservation();
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        appbar.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.w("appbar", String.valueOf(verticalOffset));
        if(verticalOffset ==-680 && !getVisibilityAlert()){
            setVisibilityAlert(true);
            invalidateOptionsMenu();
        }
        else if(verticalOffset !=-680 && getVisibilityAlert()==true){
            setVisibilityAlert(false);
            invalidateOptionsMenu();
        }

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        fab.setImageResource(R.drawable.ic_star_favourites_active);
        favourite = true;
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        fab.setImageResource(R.drawable.ic_star_favourites_active);
        favourite = true;
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        fab.setImageResource(R.drawable.ic_star_disabled);
        favourite = false;
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myRef != null){
            myRef.removeEventListener(this);
        }
    }
}
