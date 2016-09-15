package it.polito.mad_lab4.restaurant.offer_prev;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.reservation.ReservationActivity;
import it.polito.mad_lab4.restaurant.ShowOfferActivity;

/**
 * Created by f.germano on 24/04/2016.
 */
public class OfferItemPrevFragment extends Fragment {

    private int restaurantId;
    private int sectionNumber;
    private ArrayList<Offer> offers;

    public OfferItemPrevFragment(){

    }

    public static OfferItemPrevFragment newInstance(int sectionNumber, ArrayList<Offer> offers) {
        OfferItemPrevFragment fragment = new OfferItemPrevFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setOffers(offers);

        return fragment;
    }

    private void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    private void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    private void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.offer_prev_view, container, false);

        TextView offerName = (TextView)rootView.findViewById(R.id.offerName);
        TextView offerPrice = (TextView)rootView.findViewById(R.id.offerPrice);
        RatingBar offerRatingProgress = (RatingBar)rootView.findViewById(R.id.offerRatingProgress);
        TextView offerNumRatings = (TextView)rootView.findViewById(R.id.offerNumRatings);
        TextView offerDetails = (TextView)rootView.findViewById(R.id.offerDetails);

        offerName.setText(offers.get(this.sectionNumber).getOfferName());
        BigDecimal result;
        result= Helper.round(offers.get(this.sectionNumber).getPrice(),2);
        offerPrice.setText(String.valueOf(result)+"€");
        offerRatingProgress.setRating(offers.get(this.sectionNumber).getAvgRank());
        offerNumRatings.setText("("+offers.get(this.sectionNumber).getNumRanks()+")");
        offerDetails.setText(offers.get(this.sectionNumber).getDetails());

        Helper.setRatingBarColor(getContext(),
                offerRatingProgress,
                offers.get(this.sectionNumber).getAvgRank());

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerClicked(offers.get(sectionNumber));
            }
        });

        ImageView foodIV = (ImageView)rootView.findViewById(R.id.foodIV);

        if(offers.get(this.sectionNumber).getThumbDownloadLink() != null){
            Glide.with(this).load(offers.get(this.sectionNumber).getThumbDownloadLink()).into(foodIV);
        }

        return rootView;
    }

    private void offerClicked(Offer offer) {
        Intent i = new Intent(getContext(), ShowOfferActivity.class);
        i.putExtra("offer", (Serializable)offer);
        startActivity(i);
    }
}
