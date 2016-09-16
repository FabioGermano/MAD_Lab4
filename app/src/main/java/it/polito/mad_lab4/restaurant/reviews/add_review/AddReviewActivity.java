package it.polito.mad_lab4.restaurant.reviews.add_review;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.common.photo_viewer.PhotoViewerListener;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveReviewManager;
import it.polito.mad_lab4.newData.client.ClientPersonalInformation;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.Review;

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
    private String restaurantID;
    private Context context = this;
    private float rbValue=-1;
    private Restaurant restaurant;
    private String restaurantName;
    private FirebaseSaveReviewManager firebaseSaveReviewManager;
    private ClientPersonalInformation clientInfo;
    private String userId, username, userLogo;
    private String coverLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);


        hideToolbar(true);
        hideToolbarShadow(true);
        setActivityTitle(getResources().getString(R.string.add_review_activity_title));

        setVisibilityAlert(false);
        invalidateOptionsMenu();

        editText= (EditText) findViewById(R.id.review);
        cover = (ImageView)findViewById(R.id.cover) ;

        this.restaurantID = getIntent().getExtras().getString("restaurantId");
        this.restaurantName= getIntent().getExtras().getString("restaurantName");
        this.coverLink= getIntent().getExtras().getString("coverLink");
        this.userId= getIntent().getExtras().getString("userId");
        this.username= getIntent().getExtras().getString("username");
        this.userLogo = getIntent().getExtras().getString("userLogo");

        if(coverLink!=null)
            Glide.with(this).load(coverLink).into(cover);
        restaurantNameTextView = (TextView) findViewById(R.id.restaurant_name);


        restaurantNameTextView.setText(restaurantName);

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

                if(!editText.getText().toString().equals("") && rbValue>0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(getResources().getString(R.string.would_you_rate_dishes));
                    builder.setMessage(getResources().getString(R.string.rate_dishes_message));
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(context, RateDishesActivity.class);
                            i.putExtra("review", editText.getText().toString());
                            i.putExtra("restaurantId", restaurantID);
                            i.putExtra("rating", rbValue);
                            i.putExtra("restaurantName", restaurantName);
                            i.putExtra("userLogo", userLogo);
                            i.putExtra("username", username);
                            i.putExtra("userId", userId);
                            i.putExtra("coverLink", coverLink);
                            startActivity(i);

                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Review review = new Review();
                            review.setComment( editText.getText().toString());
                            review.setRank(rbValue);
                            review.setUserId(userId);
                            review.setUserName(username);
                            review.setLogoLink(userLogo);
                            saveReview(restaurantID, review);
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
    private void saveReview(final String restaurantId, final  Review review) {
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
                        review, false, null);

                final boolean res = firebaseSaveReviewManager.waitForResult();



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dismissProgressDialog();

                        if(!res){
                            Toast.makeText(getApplicationContext(), R.string.review_published_failed, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                            Toast.makeText(getApplicationContext(), R.string.review_published, Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });

            }
        }.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("review", null);
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
