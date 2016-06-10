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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.FavouritesRestaurantInfos;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Giovanna on 29/05/2016.
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

        private Context context;
        ArrayList<String[]> data;

        public FavouritesAdapter(Context context, ArrayList<String[]> objects) {
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

        public void setData(int position) {

            this.position=position;
            String[] info= data.get(position);
            restaurantName.setText(info[0]);
            String tmp= info[1]+" - "+info[2];
            address.setText(tmp);
            tmp = "( "+ info[5]+" )";
            reviews_number.setText(tmp);
            //reviews_number.setVisibility(View.GONE);
            float rank=0;
            if(Integer.parseInt(info[5])>0)
                rank= Float.parseFloat(info[4])/ Integer.parseInt(info[5]);
            ratingBar.setRating(rank);
            //ratingBar.setVisibility(View.GONE);
            Glide.with(context).load(info[3]).into(logo);
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
            Intent i= new Intent(context, RestaurantActivity.class);
            i.putExtra("restaurantId", data.get(position)[6]);
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

        holder.setData(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }


}
