package it.polito.mad_lab4.restaurant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.data.user.User;

/**
 * Created by Giovanna on 03/05/2016.
 */
public class ShowOfferActivity extends BaseActivity{

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoViewer photoViewer;
    private String offerName, offerDetails, offerPrice;
    private float rating;
    private int ratingsNumber;
    private TextView nameTextView, detailsTextView,priceTextView, ratingsNumberTextView, availableTextView, notAvailableTextView;
    private Offer offer;
    private RatingBar rb;
    private int offerId=-1;
    private ToggleButton mo, tu, we, th, fr, sa, su;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        setToolbarColor();

        offer = (Offer)getIntent().getExtras().getSerializable("offer");

        photoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer);

        nameTextView = (TextView) findViewById(R.id.name);
        availableTextView = (TextView) findViewById(R.id.today_available);
        notAvailableTextView = (TextView) findViewById(R.id.today_not_available);
        detailsTextView = (TextView) findViewById(R.id.details);
        priceTextView = (TextView) findViewById(R.id.price);
        ratingsNumberTextView = (TextView) findViewById(R.id.ratings_number);
        rb = (RatingBar) findViewById(R.id.rating);
        mo = (ToggleButton) findViewById(R.id.monday);
        tu = (ToggleButton) findViewById(R.id.tuesday);
        we = (ToggleButton) findViewById(R.id.wedenesday);
        th = (ToggleButton) findViewById(R.id.thursday);
        fr = (ToggleButton) findViewById(R.id.friday);
        sa = (ToggleButton) findViewById(R.id.saturday);
        su = (ToggleButton) findViewById(R.id.sunday);


        setActivityTitle(offer.getOfferName());
        nameTextView.setText(offer.getOfferName());
        detailsTextView.setText(offer.getDetails());
        priceTextView.setText(String.valueOf(offer.getPrice())+" €");
        ratingsNumberTextView.setText(String.valueOf("("+offer.getNumRanks())+")");
        rb.setRating(offer.getAvgRank());

        ArrayList<Boolean> availableOn = offer.getAvailableOn();
        mo.setChecked(availableOn.get(0));
        tu.setChecked(availableOn.get(1));
        we.setChecked(availableOn.get(2));
        th.setChecked(availableOn.get(3));
        fr.setChecked(availableOn.get(4));
        sa.setChecked(availableOn.get(5));
        su.setChecked(availableOn.get(6));

        Calendar today = Calendar.getInstance();
        int week_day = today.get(Calendar.DAY_OF_WEEK);
        //day of week starts from sunday to saturday (1-7)
        if(availableOn.get(Helper.fromCalendarOrderToMyOrder(week_day))) {
            availableTextView.setVisibility(View.VISIBLE);
            notAvailableTextView.setVisibility(View.GONE);
        }
        else{
            availableTextView.setVisibility(View.GONE);
            notAvailableTextView.setVisibility(View.VISIBLE);
        }

        Helper.setRatingBarColor(getApplicationContext(),
                rb,
                offer.getAvgRank());

        if(offer.getLargeDownloadLink() != null && offer.getThumbDownloadLink() != null){
            photoViewer.setThumbBitmapByURI(offer.getThumbDownloadLink());
            photoViewer.setLargePhotoDownloadLink(offer.getLargeDownloadLink());
        }
    }
}
