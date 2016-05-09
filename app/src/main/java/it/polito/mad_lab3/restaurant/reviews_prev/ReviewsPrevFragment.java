package it.polito.mad_lab3.restaurant.reviews_prev;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.Review;

/**
 * Created by f.germano on 08/05/2016.
 */
public class ReviewsPrevFragment extends Fragment {

    private int restaurantId;
    private LinearLayout containerLayout;
    private Button showAllButton;
    private TextView numReviewsInPrev;
    private RatingBar ratingBarInPrevReviews;

    public ReviewsPrevFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.reviews_prev_fragment_layout, container, false);

        containerLayout = (LinearLayout)rootView.findViewById(R.id.containerLayoutReviews);
        showAllButton = (Button)rootView.findViewById(R.id.showAllReviewsButton);
        ratingBarInPrevReviews = (RatingBar)rootView.findViewById(R.id.ratingBarInPrevReviews);
        numReviewsInPrev = (TextView)rootView.findViewById(R.id.numReviewsInPrev);

        return rootView;
    }

    public void init(int restaurantId) {

        this.restaurantId = restaurantId;
        Restaurant restaurant =  RestaurantBL.getRestaurantById(getContext(), restaurantId);

        ArrayList<Review> reviews = restaurant.getReviews();

        ratingBarInPrevReviews.setRating(restaurant.getAvgReview());
        String str = getResources().getString(R.string.reviewsStrPrev);
        str = str.replace("%", String.valueOf(restaurant.getNumReviews()));
        numReviewsInPrev.setText(str);

        Helper.setRatingBarColor(getContext(),
                ratingBarInPrevReviews,
                restaurant.getAvgReview());

        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                return o1.getDate().compareTo(o2.getDate()) * -1;
            }
        });

        int size = restaurant.getNumReviews();
        for(int i = 0; i<3; i++){
            if(size > i){
                View viewToAdd = LayoutInflater.from(getContext()).inflate(R.layout.review_view, null);

                Review review = reviews.get(i);

                //((ImageView)viewToAdd.findViewById(R.id.offDetUserPhoto));
                ((TextView)viewToAdd.findViewById(R.id.offDetDate)).setText(review.getDate());;
                ((RatingBar)viewToAdd.findViewById(R.id.offDetRatingBar)).setRating(review.getRank());
                ((TextView)viewToAdd.findViewById(R.id.offDetReview)).setText(review.getComment());
                ((TextView)viewToAdd.findViewById(R.id.offDetUserName)).setText(review.getUserName());

                Helper.setRatingBarColor(getContext(),
                        (RatingBar)viewToAdd.findViewById(R.id.offDetRatingBar),
                        review.getRank());

                containerLayout.addView(viewToAdd);
            }
        }

        if(size < 4){
            showAllButton.setVisibility(View.GONE);
        }
    }
}
