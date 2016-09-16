package it.polito.mad_lab4.restaurant.reviews;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.firebase_manager.FirebaseGetReviewsListManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.Review;
import it.polito.mad_lab4.newData.restaurant.ReviewFood;
import it.polito.mad_lab4.restaurant.reviews.add_review.SectionsPagerAdapter;

public class ReviewsActivity extends BaseActivity {

    private ListView reviewsListView;
    private ArrayList<Review> reviews;
    private Restaurant restaurant;
    private ReviewsListAdapter adapter;
    private int checkedInDialog = 0;
    private float ranking;
    private int numRanking;
    private String restaurantId;
    private FirebaseGetReviewsListManager firebaseGetReviewsListManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        if(savedInstanceState != null){
            this.checkedInDialog = savedInstanceState.getInt("checkedInDialog");
        }

        hideToolbarShadow(true);
        setToolbarColor();
        setVisibilityOrderBy(true);
        invalidateOptionsMenu();
        setActivityTitle(getResources().getString(R.string.reviews_activity_title));

        reviewsListView = (ListView) findViewById(R.id.reviewsListView);

        //init(getIntent().getExtras());
        Bundle bundle = getIntent().getExtras();
        restaurantId = bundle.getString("restaurantId");
        numRanking = bundle.getInt("numRanking");
        ranking = bundle.getFloat("ranking");

        if (bundle.getBoolean("noAlertButton"))
            setVisibilityAlert(false);

        //((RatingBar)findViewById(R.id.restaurantAvgRank)).setRating(this.restaurant.getAvgReview());
        String strNumReviews = getResources().getString(R.string.numReviewsText);
        strNumReviews = strNumReviews.replace("%", String.valueOf(numRanking));
        ((TextView)findViewById(R.id.numReviews)).setText(strNumReviews);
        ((RatingBar)findViewById(R.id.restaurantAvgRank)).setRating(ranking/numRanking);
        Helper.setRatingBarColor(getApplicationContext(),
                (RatingBar)findViewById(R.id.restaurantAvgRank),
                ranking/numRanking);


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_orderBy:
                showDialogForSorting();
                break;
            default:
                break;
        }
        return true;
    }

    private void sortByRank(){
        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                return (new Float(o1.getRank()))
                        .compareTo(new Float(o2.getRank())) * -1;
            }
        });
    }

    private void sortByTime(){
        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                return o1.getDate().compareTo(o2.getDate()) * -1;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("checkedInDialog", this.checkedInDialog);
    }

    private void notifyAdapter() {
        this.adapter.notifyDataSetChanged();
        this.reviewsListView.smoothScrollToPosition(0);
    }

    private void showDialogForSorting(){
        AlertDialog dialog;

        // Strings to Show In Dialog with Radio Buttons
        final CharSequence[] items = {getString(R.string.orderbydate), getString(R.string.orderbyranking)};

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setSingleChoiceItems(items, checkedInDialog, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        checkedInDialog = 0;
                        sortByTime();
                        notifyAdapter();
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.toast_when_sorted), Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        checkedInDialog = 1;
                        sortByRank();
                        notifyAdapter();
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.toast_when_sorted), Toast.LENGTH_LONG).show();
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onResume() {
        super.onResume();


        final ArrayList<Review> reviews = new ArrayList<>();

        Thread t = new Thread()
        {
            public void run() {
                firebaseGetReviewsListManager = new FirebaseGetReviewsListManager();
                firebaseGetReviewsListManager.getReviews(restaurantId, null);
                firebaseGetReviewsListManager.waitForResult();
                reviews.addAll(firebaseGetReviewsListManager.getResult());

                if(reviews == null){
                    Toast.makeText(ReviewsActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter(reviews);
                    }
                });

            }
        };

        t.start();
    }
    private void initAdapter(ArrayList<Review> reviews) {
        this.reviews=reviews;
        sortByTime();
        this.adapter = new ReviewsListAdapter(getBaseContext(), reviews);
        reviewsListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseGetReviewsListManager.terminate();
    }
}
