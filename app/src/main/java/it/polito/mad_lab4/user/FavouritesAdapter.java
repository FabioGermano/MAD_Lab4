package it.polito.mad_lab4.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Giovanna on 29/05/2016.
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

        private Context context;
        ArrayList<Restaurant> data;

        public FavouritesAdapter(Context context, ArrayList<Restaurant> objects) {
            this.context= context;
            this.data= objects;

        }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int position;
        CardView cardView;
        RatingBar ratingBar;
        ImageView logo;
        TextView restaurantName, address, reviews_number;
        public ViewHolder(View v){
            super(v);
            restaurantName = (TextView)  v.findViewById(R.id.name);
            address = (TextView)  v.findViewById(R.id.address);
            reviews_number = (TextView)  v.findViewById(R.id.reviews_number);
            ratingBar = (RatingBar)  v.findViewById(R.id.ratingBar);
            cardView = (CardView)  v.findViewById(R.id.cardView);
            logo = (ImageView)  v.findViewById(R.id.logo);


        }

        public void setData(int position, Restaurant restaurant) {

            this.position=position;
            restaurantName.setText(restaurant.getRestaurantName());
            String tmp= restaurant.getAddress()+" - "+restaurant.getCity();
            address.setText(tmp);
            tmp = "( "+ String.valueOf(restaurant.getNumReviews())+" )";
            reviews_number.setText(tmp);
            ratingBar.setRating(restaurant.getRanking());
            // TODO settare l'immagine logo
        }

        public void setListeners() {
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToRestaurantActivity();
                    }
                });
        }

        private void goToRestaurantActivity() {

            Restaurant restaurant = data.get(position);
            Intent i= new Intent(context, RestaurantActivity.class);
            i.putExtra("restaurantId", restaurant.getRestaurantId());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favourites_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        vh.setListeners();
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Restaurant r = data.get(position);
        holder.setData(position, r);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }


}
