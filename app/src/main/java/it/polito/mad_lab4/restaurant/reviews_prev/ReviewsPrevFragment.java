package it.polito.mad_lab4.restaurant.reviews_prev;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.firebase_manager.FirebaseGetMenuByTypeManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetReviewsListManager;
import it.polito.mad_lab4.newData.restaurant.Review;

/**
 * Created by f.germano on 08/05/2016.
 */
public class ReviewsPrevFragment extends Fragment {

    private String restaurantId;
    private LinearLayout containerLayout;
    private Button showAllButton;
    private TextView numReviewsInPrev;
    private RatingBar ratingBarInPrevReviews;
    private ArrayList<Review> reviews;
    private float ranking;
    private int numRanking;

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

    @Override
    public void onResume() {
        super.onResume();


        final ArrayList<Review> reviews = new ArrayList<>();

        Thread t = new Thread()
        {
            public void run() {
                reviews.clear();
                FirebaseGetReviewsListManager firebaseGetReviewsListManager = new FirebaseGetReviewsListManager();
                firebaseGetReviewsListManager.getReviews(restaurantId, 3);
                firebaseGetReviewsListManager.waitForResult();
                reviews.addAll(firebaseGetReviewsListManager.getResult());

                if(reviews == null){
                    Log.e("returned null dishes", "resturned null dishes");
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData(reviews);
                    }
                });

            }
        };

        t.start();
    }

    private void setData(ArrayList<Review> reviews) {

        //Restaurant restaurant =  RestaurantBL.getRestaurantById(getContext(), restaurantId);
        if(!reviews.isEmpty())
            ratingBarInPrevReviews.setRating(ranking/numRanking);
        String str = getResources().getString(R.string.reviewsStrPrev);
        str = str.replace("%", String.valueOf(numRanking));
        numReviewsInPrev.setText(str);

        Helper.setRatingBarColor(getContext(),
                ratingBarInPrevReviews,
                ranking);

        Collections.sort(reviews, new Comparator<Review>() {
            @Override
            public int compare(Review o1, Review o2) {
                return o1.getDate().compareTo(o2.getDate()) * -1;
            }
        });

        for(int i = 0; i<reviews.size(); i++){

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

        if(numRanking < 4){
            showAllButton.setVisibility(View.GONE);
        }
    }

    public void setRanking(float ranking, int numRanking) {
        this.ranking = ranking;
        this.numRanking= numRanking;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId=restaurantId;
    }
}
