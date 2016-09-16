package it.polito.mad_lab4.restaurant.reviews.add_review;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.firebase_manager.FirebaseGetMenuByTypeManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferListManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveReviewManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.Review;
import it.polito.mad_lab4.newData.restaurant.ReviewFood;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Giovanna on 09/05/2016.
 */
public class RateDishesActivity extends BaseActivity{

    private ViewPager mViewPager;
    private FloatingActionButton doneFab;
    private ArrayList<ArrayList<ReviewFood>> lists; //order: offer, main, second, dessert , other
    private String restaurantId;
    private String review;
    private float rating;
    private ImageView cover;
    private TextView nameTextView;
    private FirebaseGetMenuByTypeManager firebaseGetMenuByTypeManager;
    private FirebaseGetOfferListManager firebaseGetOfferListManager;
    private String restaurantName, coverLink;

    private FirebaseSaveReviewManager firebaseSaveReviewManager;
    private String username;
    private String userId;
    private String userLogo;

    private  ReviewFood newReviewFood(Object object) {
        ReviewFood rf = new ReviewFood();
        rf.setFood(object);
        if( object instanceof Offer ){
            rf.setSection(0);
            rf.setId(((Offer) object).getOfferId());
        }
        else if( object instanceof Dish ){
            rf.setId(((Dish) object).getDishId());
            rf.setSection(DishTypeConverter.fromEnumToIndex(DishTypeConverter.fromStringToEnum(((Dish)object).getType())) + 1);
        }

        rf.setRating(-1);
        return rf;
    }
    private void saveReview(final String restaurantId, final Review review, final ArrayList<ReviewFood> list) {
        new Thread()
        {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                firebaseSaveReviewManager = new FirebaseSaveReviewManager();
                firebaseSaveReviewManager.saveReview(restaurantId,
                        review, true, list);

                final boolean res = firebaseSaveReviewManager.waitForResult();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dismissProgressDialog();
                        if(!res){
                            Toast.makeText(getApplicationContext(), R.string.review_published_failed, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {

                            Toast toast = Toast.makeText(getApplicationContext(), R.string.review_published, Toast.LENGTH_SHORT);
                            toast.show();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                            intent.putExtra("restaurantId", restaurantId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });

            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_dishes);

        if (isLargeDevice(getApplicationContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);

        setVisibilityAlert(false);
        invalidateOptionsMenu();

        //retrieve values from intent extras
        this.restaurantId = getIntent().getExtras().getString("restaurantId");
        this.review = getIntent().getExtras().getString("review");
        this.rating = getIntent().getExtras().getFloat("rating");
        this.restaurantName = getIntent().getExtras().getString("restaurantName");
        this.userId = getIntent().getExtras().getString("userId");
        this.username = getIntent().getExtras().getString("username");
        this.userLogo = getIntent().getExtras().getString("usserLogo");
        this.coverLink= getIntent().getExtras().getString("coverLink");

        nameTextView= (TextView) findViewById(R.id.restaurant_name);
        cover = (ImageView) findViewById(R.id.cover);
        nameTextView.setText(restaurantName);

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(restaurantId == null){
            return;
        }

        final ArrayList<Dish> dishes = new ArrayList<>();
        final ArrayList<Offer> offers = new ArrayList<>();

        Thread t = new Thread()
        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });
                firebaseGetMenuByTypeManager = new FirebaseGetMenuByTypeManager();
                firebaseGetMenuByTypeManager.getMenu(restaurantId, null, null);
                final boolean timeout1  =firebaseGetMenuByTypeManager.waitForResult();
                dishes.addAll(firebaseGetMenuByTypeManager.getResult());

                firebaseGetOfferListManager = new FirebaseGetOfferListManager();
                firebaseGetOfferListManager.getOffers(restaurantId);
                final boolean timeout2 = firebaseGetOfferListManager.waitForResult();
                offers.addAll(firebaseGetOfferListManager.getResult());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();
                        if(timeout1 || timeout2){
                            Toast.makeText(RateDishesActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        if(coverLink!=null)
                            Glide.with(getBaseContext()).load(coverLink).into(cover);

                        if(dishes != null && offers != null)
                            initAdapter(dishes, offers);
                    }
                });

            }
        };

        t.start();
    }
    private void initAdapter(ArrayList<Dish> dishes, ArrayList<Offer> offers) {
        //Conversion from Offer to ReservedDish

        //initialize the lists
        lists = new ArrayList<ArrayList<ReviewFood>>();
        for(int i=0;i<5;i++)
            lists.add(new ArrayList<ReviewFood>());

        for(Offer offer : offers){
            ReviewFood rf= newReviewFood(offer);
            //rf.setPosition(j);
            lists.get(0).add(rf);
        }

        //Conversion from Dish to ReservedDish
        for(Dish dish : dishes){
            ReviewFood rf= newReviewFood(dish);
            lists.get(DishTypeConverter.fromEnumToIndex(DishTypeConverter.fromStringToEnum(dish.getType()))+1).add(rf);
        }

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                getBaseContext(), lists );
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
        doneFab = (FloatingActionButton) findViewById(R.id.done);

        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Review r = new Review( );
                r.setComment(review);
                r.setRank(rating);
                r.setUserId(userId);
                r.setUserName(username);
                r.setLogoLink(userLogo);

                ArrayList<ReviewFood> saveData = new ArrayList<ReviewFood>();
                for(ArrayList<ReviewFood> list : lists){
                    for(ReviewFood rev : list){
                        if(rev.getRating()>=0){
                            saveData.add(rev);
                        }
                    }
                }
                if (saveData.size() == 0){
                    Toast.makeText(RateDishesActivity.this, "No dishes evaluated", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveReview(restaurantId, r, saveData);

            }
        });

    }


}
