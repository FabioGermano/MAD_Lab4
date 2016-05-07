package it.polito.mad_lab3.restaurant.offer_prev;

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

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.DishType;
import it.polito.mad_lab3.data.restaurant.DishTypeConverter;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.ShowOfferActivity;

/**
 * Created by f.germano on 24/04/2016.
 */
public class OfferItemPrevFragment extends Fragment {

    private int restaurantId;
    private int sectionNumber;

    public OfferItemPrevFragment(){

    }

    public static OfferItemPrevFragment newInstance(int sectionNumber, int restaurantId) {
        OfferItemPrevFragment fragment = new OfferItemPrevFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setRestaurantId(restaurantId);

        return fragment;
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

        final ArrayList<Offer> offers = RestaurantBL.getRestaurantById(getContext(), this.restaurantId).getOffers();
        TextView offerName = (TextView)rootView.findViewById(R.id.offerName);
        TextView offerPrice = (TextView)rootView.findViewById(R.id.offerPrice);
        RatingBar offerRatingProgress = (RatingBar)rootView.findViewById(R.id.offerRatingProgress);
        TextView offerNumRatings = (TextView)rootView.findViewById(R.id.offerNumRatings);
        TextView offerDetails = (TextView)rootView.findViewById(R.id.offerDetails);

        offerName.setText(offers.get(this.sectionNumber).getOfferName());
        offerPrice.setText(offers.get(this.sectionNumber).getPrice()+"€");
        offerRatingProgress.setRating(offers.get(this.sectionNumber).getAvgRank());
        offerNumRatings.setText("("+offers.get(this.sectionNumber).getNumRanks()+")");
        offerDetails.setText(offers.get(this.sectionNumber).getDetails());

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerClicked(offers.get(sectionNumber));
            }
        });

        ImageView foodIV = (ImageView)rootView.findViewById(R.id.foodIV);

        if(offers.get(this.sectionNumber).getThumbPath() != null){
            foodIV.setImageBitmap(BitmapFactory.decodeFile(offers.get(this.sectionNumber).getThumbPath()));
        }
        else if(offers.get(this.sectionNumber).getResPhoto() != null){
            int imgRes = Helper.getResourceByName(getContext(), offers.get(this.sectionNumber).getResPhoto(), "drawable");
            if(imgRes != 0){
                foodIV.setImageResource(imgRes);
            }
        }

        return rootView;
    }

    private void offerClicked(Offer offer) {
        Intent i = new Intent(getContext(), ShowOfferActivity.class);
        i.putExtra("offer", (Serializable)offer);
        startActivity(i);
    }
}
