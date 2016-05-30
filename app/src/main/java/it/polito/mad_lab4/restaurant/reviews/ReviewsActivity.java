package it.polito.mad_lab4.restaurant.reviews;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.restaurant.Review;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.restaurant.menu.MenuListAdapter;

public class ReviewsActivity extends BaseActivity {

    private ListView reviewsListView;
    private ArrayList<Review> reviews;
    private Restaurant restaurant;
    private ReviewsListAdapter adapter;
    private int checkedInDialog = 0;

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

        init(getIntent().getExtras());

        ((RatingBar)findViewById(R.id.restaurantAvgRank)).setRating(this.restaurant.getAvgReview());
        String strNumReviews = getResources().getString(R.string.numReviewsText);
        strNumReviews = strNumReviews.replace("%", String.valueOf(this.restaurant.getNumReviews()));
        ((TextView)findViewById(R.id.numReviews)).setText(strNumReviews);

        Helper.setRatingBarColor(getApplicationContext(),
                (RatingBar)findViewById(R.id.restaurantAvgRank),
                restaurant.getAvgReview());

        this.adapter = new ReviewsListAdapter(getApplicationContext(), this.reviews);
        reviewsListView.setAdapter(adapter);
    }

    private void init(Bundle bundle){
        int restaurantId = bundle.getInt("restaurantId");
        restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId);
        this.reviews = restaurant.getReviews();
        sortByTime();
    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }



    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

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
        final CharSequence[] items = {"Ordina dal più recente", "Ordina per valutazione"};

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
}
