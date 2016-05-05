package it.polito.mad_lab3.restaurant;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.photo_manager.PhotoManager;
import it.polito.mad_lab3.common.photo_manager.PhotoType;
import it.polito.mad_lab3.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab3.common.photo_viewer.PhotoViewerListener;
import it.polito.mad_lab3.data.restaurant.Offer;

/**
 * Created by Giovanna on 03/05/2016.
 */
public class ShowOfferActivity extends BaseActivity implements PhotoViewerListener{

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager photoManager;
    private PhotoViewer photoViewer;
    private String offerName, offerDetails, offerPrice;
    private float rating;
    private int ratingsNumber;
    private TextView nameTextView, detailsTextView,priceTextView, ratingsNumberTextView;
    private Offer offer;
    private RatingBar rb;
    private int offerId=-1;
    private ToggleButton mo, tu, we, th, fr, sa, su;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        setActivityTitle("Offer name");


        offer = new Offer("Menu kebab", (float) 2.67,25, (float) 4.50, null, null, "Pizza kebab +" +
                " Bibita + patatatine (a scelta salsa fra maionese ketchup e salsa barbecue)" );

        photoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer);
        photoManager = new PhotoManager(getApplicationContext(), PhotoType.OFFER, this.imageThumb, this.imageLarge);



        nameTextView = (TextView) findViewById(R.id.name);
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

        ArrayList<Boolean> available = new ArrayList<>();
        available.add(true);
        available.add(true);
        available.add(true);
        available.add(true);
        available.add(true);
        available.add(false);
        available.add(false);

        nameTextView.setText(offer.getOfferName());
        detailsTextView.setText(offer.getDetails());
        priceTextView.setText(String.valueOf(offer.getPrice())+" €");
        ratingsNumberTextView.setText(String.valueOf("("+offer.getNumRanks())+")");
        rb.setRating(offer.getAvgRank());

        mo.setChecked(available.get(0));
        tu.setChecked(available.get(1));
        we.setChecked(available.get(2));
        th.setChecked(available.get(3));
        fr.setChecked(available.get(4));
        sa.setChecked(available.get(5));
        su.setChecked(available.get(6));


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
