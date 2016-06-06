package it.polito.mad_lab4.restaurant.reviews.add_review;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.ReviewFood;

/**
 * Created by Giovanna on 28/04/2016.
 */
public class RateAdapter extends ArrayAdapter<ReviewFood>{

    private ArrayList<ReviewFood> data;
    private Context context;
    ListView listView;
    private int section;

    public RateAdapter(Context context, ArrayList<ReviewFood> objects, int section) {
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

        //data.get(position).setPosition(position);
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
            if(data.get(position).getRating()>=0) {
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
                                    data.get(position).setRating(0);

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
        if(data.get(position).getFood() instanceof Offer)
            viewHolder.name.setText(((Offer)data.get(position).getFood() ).getOfferName());

        else {
            viewHolder.name.setText(((Dish)data.get(position).getFood() ).getDishName());
        }

        // Return the completed view to render on screen
        return convertView;
    }

}

