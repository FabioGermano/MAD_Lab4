package it.polito.mad_lab4.restaurant.reviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Review;

/**
 * Created by f.germano on 03/05/2016.
 */
public class ReviewsListAdapter extends ArrayAdapter<Review>  {

    private final ArrayList<Review> reviews;

    // View lookup cache
    private static class ViewHolder {
        public ImageView offDetUserPhoto;
        public TextView offDetDate;
        public RatingBar offDetRatingBar;
        public TextView offDetReview;
        public TextView offDetUserName;
    }

    private ArrayList<Dish> dishes;

    public ReviewsListAdapter(Context context, ArrayList<Review> reviews){
        super(context, 0, reviews);

        this.reviews = reviews;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        Review review = this.reviews.get(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.review_view_detail, parent, false);

            viewHolder.offDetUserPhoto = (ImageView)convertView.findViewById(R.id.offDetUserPhoto);
            viewHolder.offDetDate = (TextView)convertView.findViewById(R.id.offDetDate);
            viewHolder.offDetRatingBar = (RatingBar)convertView.findViewById(R.id.offDetRatingBar);
            viewHolder.offDetReview = (TextView)convertView.findViewById(R.id.offDetReview);
            viewHolder.offDetUserName = (TextView)convertView.findViewById(R.id.offDetUserName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.offDetDate.setText(review.getDate());
        viewHolder.offDetRatingBar.setRating(review.getRank());
        viewHolder.offDetReview.setText(review.getComment());
        viewHolder.offDetUserName.setText(review.getUserName());
        viewHolder.offDetDate.setText(review.getDate());

        Helper.setRatingBarColor(getContext(),
                viewHolder.offDetRatingBar,
                review.getRank());

        // Return the completed view to render on screen
        return convertView;
    }
}
