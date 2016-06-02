package it.polito.mad_lab4.restaurant;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.Cover;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.reservation.ReservationActivity;
import it.polito.mad_lab4.restaurant.cover.CoverActivity;
import it.polito.mad_lab4.restaurant.foodPhoto.ContainerUserPhotoFragment;
import it.polito.mad_lab4.restaurant.menu.MenuActivity;
import it.polito.mad_lab4.restaurant.menu_prev.MenuPrevFragment;
import it.polito.mad_lab4.restaurant.offer_prev.OfferPrevFragment;
import it.polito.mad_lab4.restaurant.reviews.ReviewsActivity;
import it.polito.mad_lab4.restaurant.reviews.add_review.AddReviewActivity;
import it.polito.mad_lab4.restaurant.reviews_prev.ReviewsPrevFragment;

public class  RestaurantActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener{

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ContainerUserPhotoFragment containerUserPhotoFragment;
    private Restaurant restaurant;
    private BasicInfoFragment basicInfoFragment;
    private MenuPrevFragment menuPrevFragment;
    private OfferPrevFragment offerPrevFragment;
    private User user;
    private Button showAllMenuButton, showAllReviewsButton, reservation;
    private ReviewsPrevFragment reviewsPrevFragment;
    private ImageView coverImage;
    private FloatingActionMenu add;
    private android.support.design.widget.FloatingActionButton fab;
    private FloatingActionButton add_review, add_photo, add_reservation;
    private AppBarLayout appbar;
    private boolean favourite=false;

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

        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO if the user doesn't have the restaurant in his favourites
                if(!favourite) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                    // add to the favourite list
                }
                else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_disabled));
                    // remove from the favourites list
                }
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.themeColor)));

        add = (FloatingActionMenu) findViewById(R.id.add);

        add_review = (FloatingActionButton) findViewById(R.id.add_review);
        add_photo = (FloatingActionButton) findViewById(R.id.add_photo);
        add_reservation = (FloatingActionButton) findViewById(R.id.add_reservation);
        add.setClosedOnTouchOutside(true);

        add_photo.setOnClickListener(clickListener);
        add_reservation.setOnClickListener(clickListener);
        add_review.setOnClickListener(clickListener);

        Bundle extras = getIntent().getExtras();
        int id = -1;
        if (extras != null) {
            id = (int) extras.getInt("restaurantId");
            extras.clear();
        }
        if(id == -1){
            Toast toast = Toast.makeText(getApplicationContext(), "Errore", Toast.LENGTH_SHORT);
            toast.show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }

        restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), id);

        collapsingToolbarLayout.setTitle(restaurant.getRestaurantName());
        appbar= (AppBarLayout) findViewById(R.id.app_bar_layout);

        containerUserPhotoFragment = (ContainerUserPhotoFragment)getSupportFragmentManager().findFragmentById(R.id.UserPhotoFragment);
        containerUserPhotoFragment.init(restaurant);

        basicInfoFragment = (BasicInfoFragment) getSupportFragmentManager().findFragmentById(R.id.basicinfo_fragment);
        basicInfoFragment.setRestaurantId(restaurant.getRestaurantId());


        menuPrevFragment = (MenuPrevFragment)getSupportFragmentManager().findFragmentById(R.id.menuPrevFragment);
        menuPrevFragment.setRestaurantId(restaurant.getRestaurantId());

        offerPrevFragment = (OfferPrevFragment)getSupportFragmentManager().findFragmentById(R.id.offersPrevFragment);
        offerPrevFragment.init(restaurant.getRestaurantId());

        reviewsPrevFragment = (ReviewsPrevFragment)getSupportFragmentManager().findFragmentById(R.id.reviewsPrevFragment);
        reviewsPrevFragment.init(restaurant.getRestaurantId());

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

        coverImage = (ImageView)findViewById(R.id.coverImage);
        coverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coverImageClick();
            }
        });
        setCoverImage();
    }

    private void setCoverImage() {
        Cover cover = this.restaurant.getBasicInfo().getCovers().get(0);

        if(cover.getThumbPath() == null) {
            coverImage.setImageBitmap(
                    BitmapFactory.decodeResource(getResources(),
                            Helper.getResourceByName(getApplicationContext(), cover.getResPhoto(), "drawable")
                    )
            );
        }
        else
        {
            coverImage.setImageBitmap(
                    BitmapFactory.decodeFile(cover.getThumbPath())
            );
        }

    }

    private void coverImageClick() {
        Intent i = new Intent(getApplicationContext(), CoverActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }



    private void showAllReviewsButtonPressed() {
        Intent i = new Intent(getApplicationContext(), ReviewsActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }

    private void showAllMenuButtonPressed() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
        startActivity(i);
    }

    private void newReservation() {
        Intent i = new Intent(getApplicationContext(), ReservationActivity.class);
        i.putExtra("restaurantId", this.restaurant.getRestaurantId());
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
                    i.putExtra("restaurantId", restaurant.getRestaurantId());
                    startActivity(i);
                    break;
                case R.id.add_reservation:
                    newReservation();
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        appbar.addOnOffsetChangedListener(this);
    }

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


}
