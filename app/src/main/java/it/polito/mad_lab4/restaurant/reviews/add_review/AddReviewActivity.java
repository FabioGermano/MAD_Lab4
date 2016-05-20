package it.polito.mad_lab4.restaurant.reviews.add_review;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.bl.UserBL;
import it.polito.mad_lab4.common.UserSession;
import it.polito.mad_lab4.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.common.photo_viewer.PhotoViewerListener;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.restaurant.Review;
import it.polito.mad_lab4.data.user.User;

/**
 * Created by Giovanna on 08/05/2016.
 */
public class AddReviewActivity extends BaseActivity implements PhotoViewerListener {

    private String review;
    private EditText editText;
    private RatingBar rb;
    private TextView ratingTextView, restaurantNameTextView;
    private ImageView cover;
    private PhotoViewer photoViewer1, photoViewer2, photoViewer3;
    private Button addReview;
    private int restaurantID=-1;
    private Context context = this;
    private float rbValue=-1;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        this.restaurantID = getIntent().getExtras().getInt("restaurantId");
        hideToolbar(true);
        hideToolbarShadow(true);
        setActivityTitle(getResources().getString(R.string.add_review_activity_title));

        editText= (EditText) findViewById(R.id.review);
        cover = (ImageView)findViewById(R.id.cover) ;
        restaurantNameTextView = (TextView) findViewById(R.id.restaurant_name);
        restaurant = RestaurantBL.getRestaurantById(this, restaurantID);

        restaurantNameTextView.setText(restaurant.getRestaurantName());
        rb = (RatingBar) findViewById(R.id.rating);
        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    rbValue=rating;
                }
            }
        });
        ratingTextView= (TextView) findViewById(R.id.ratingText);

        addReview= (Button) findViewById(R.id.add);

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!editText.getText().toString().equals("") && rbValue!=-1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getResources().getString(R.string.would_you_rate_dishes));
                    builder.setMessage(getResources().getString(R.string.rate_dishes_message));
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(context, RateDishesActivity.class);
                            i.putExtra("review", editText.getText().toString());
                            i.putExtra("restaurantId", restaurantID);
                            i.putExtra("rating", rbValue);
                            startActivity(i);

                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO aggiungere review al db
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date today = new Date();
                            User user = UserBL.getUserById(getApplicationContext(), UserSession.userId);
                            Review r = new Review(user.getName(), null, rbValue, df.format(today), editText.getText().toString() );
                            RestaurantBL.addReview(restaurant, r);
                            RestaurantBL.saveChanges(getApplicationContext());
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.review_published), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.insert_review), Toast.LENGTH_SHORT).show();
                }

            }
        });

        //photoViewer1 = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer1);
        //photoViewer2 = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer2);
        //photoViewer3 = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer3);



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("review", null);
    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
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

    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {

    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {

    }
}
