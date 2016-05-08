package it.polito.mad_lab3.restaurant.reviews;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;

/**
 * Created by Giovanna on 08/05/2016.
 */
public class AddReviewActivity extends BaseActivity{

    private String review;
    private EditText editText;
    private RatingBar rb;
    private TextView ratingTextView, restaurantNameTextView;
    private ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        if (savedInstanceState != null) {
            review= savedInstanceState.getString("review", null);
        }
        hideToolbar(true);
        hideToolbarShadow(true);
        setActivityTitle(getResources().getString(R.string.add_review_activity_title));

        editText= (EditText) findViewById(R.id.review);
        cover = (ImageView)findViewById(R.id.cover) ;
        restaurantNameTextView = (TextView) findViewById(R.id.restaurant_name);
        rb = (RatingBar) findViewById(R.id.rating);
        ratingTextView= (TextView) findViewById(R.id.ratingText);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("review", null);
    }

    @Override
    protected void filterButton() {

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }
}
