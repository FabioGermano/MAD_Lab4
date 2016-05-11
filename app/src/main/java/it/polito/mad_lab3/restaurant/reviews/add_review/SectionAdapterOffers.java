package it.polito.mad_lab3.restaurant.reviews.add_review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.reservation.ReservedDish;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.data.restaurant.ReviewFood;

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
        ViewSwitcher viewSwitcher;
        Button rateIt;
        ImageButton cancel;
        float rating;
        boolean rated=false;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Offer offer;
        Dish dish;

        slide_in_left = AnimationUtils.loadAnimation(this.context,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this.context,
                android.R.anim.slide_out_right);



        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.review_dishes_item, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.ratingBar= (RatingBar) convertView.findViewById(R.id.ratingBar) ;
            viewHolder.viewSwitcher = (ViewSwitcher) convertView.findViewById(R.id.viewSwitcher);
            viewHolder.rateIt = (Button) convertView.findViewById(R.id.rateIt);
            viewHolder.cancel = (ImageButton) convertView.findViewById(R.id.cancel);
            viewHolder.viewSwitcher.setInAnimation(slide_in_left);
            viewHolder.viewSwitcher.setOutAnimation(slide_out_right);

            viewHolder.position= position;

            viewHolder.rateIt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to rate a dish
                    viewHolder.viewSwitcher.showNext();
                }
            });

            viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //to not rate a dish
                    viewHolder.viewSwitcher.showPrevious();
                    data.get(position).setRating(-1);

                }
            });

            viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
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

