package it.polito.mad_lab4.restaurant.reviews.add_review;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.reservation.ReservedDish;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.restaurant.ReviewFood;

/**
 * Created by Giovanna on 28/04/2016.
 */
public class SectionAdapterOffers extends ArrayAdapter<ReviewFood>{

    private ArrayList<ReviewFood> data;
    private Context context;
    ListView listView;
    private int section;

    Animation slide_in_left, slide_out_right;

    public SectionAdapterOffers(Context context, ArrayList<ReviewFood> objects, int section) {
        super(context, 0, objects);
        this.context=context;
        this.data=objects;
        this.section=section;
    }

    // View lookup cache
    private static class ViewHolder {
        int position;
        TextView name;
        RatingBar ratingBar;
        LinearLayout ll;
        //ViewSwitcher viewSwitcher;
        Button rateIt;
        ImageButton cancel;
        float rating=-1;
        boolean rated=false;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Offer offer;
        Dish dish;

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.review_dishes_item, parent, false);

            //find views
            viewHolder.name = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.ratingBar= (RatingBar) convertView.findViewById(R.id.ratingBar) ;
            //viewHolder.viewSwitcher = (ViewSwitcher) convertView.findViewById(R.id.viewSwitcher);
            viewHolder.ll = (LinearLayout) convertView.findViewById(R.id.rate);

            viewHolder.rateIt = (Button) convertView.findViewById(R.id.rateIt);
            viewHolder.cancel = (ImageButton) convertView.findViewById(R.id.cancel);

            //viewHolder.viewSwitcher.setInAnimation(slide_in_left);
            //viewHolder.viewSwitcher.setOutAnimation(slide_out_right);
            viewHolder.position= position;
            if(data.get(position).getRating()>0) {
                viewHolder.rateIt.setVisibility(View.GONE);
                viewHolder.ll.setVisibility(View.VISIBLE);
                viewHolder.ratingBar.setRating(data.get(position).getRating());
                //viewHolder.viewSwitcher.showPrevious();
            }
            else{
                viewHolder.ll.setVisibility(View.GONE);
                viewHolder.rateIt.setVisibility(View.VISIBLE);
            }
                //viewHolder.viewSwitcher.showNext();



            viewHolder.rateIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //show rating bar
                    viewHolder.ll.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    viewHolder.ll.setVisibility(View.VISIBLE);

                                }
                            });
                    viewHolder.rateIt.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    viewHolder.rateIt.setVisibility(View.GONE);

                                }
                            });
                   //viewHolder.viewSwitcher.showNext();
                }
            });

            viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //show button "rate it "
                    //viewHolder.viewSwitcher.showPrevious();
                    viewHolder.rating=-1;
                    data.get(position).setRating(-1);
                    viewHolder.rateIt.animate()
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    viewHolder.rateIt.setVisibility(View.VISIBLE);

                                }
                            });
                    viewHolder.ll.animate()
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    viewHolder.ll.setVisibility(View.GONE);

                                }
                            });

                }
            });

            viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    viewHolder.rating=rating;
                    data.get(position).setRating(rating);
                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        if(section==0){
            offer = (Offer) getItem(position).getFood();
            viewHolder.name.setText(offer.getOfferName());
        }
        else {
            dish= (Dish) getItem(position).getFood();
            viewHolder.name.setText(dish.getDishName());
        }

        // Return the completed view to render on screen
        return convertView;
    }

}

