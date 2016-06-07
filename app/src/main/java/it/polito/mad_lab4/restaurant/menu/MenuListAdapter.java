package it.polito.mad_lab4.restaurant.menu;

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

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.restaurant.Dish;

/**
 * Created by f.germano on 03/05/2016.
 */
public class MenuListAdapter extends ArrayAdapter<Dish>  {

    // View lookup cache
    private static class ViewHolder {
        ImageView dishPhotoIV;
        TextView dishNameTV;
        RatingBar ratingBar;
        TextView numRanksTV;
        TextView dishPriceTV;
        TextView availableTextView, notAvailableTextView;
    }

    private ArrayList<Dish> dishes;

    public MenuListAdapter(Context context, ArrayList<Dish> dishes){
        super(context, 0, dishes);

        this.dishes = dishes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_list_detail, parent, false);

            viewHolder.dishPhotoIV = (ImageView)convertView.findViewById(R.id.dishPhotoIV);
            viewHolder.dishNameTV = (TextView)convertView.findViewById(R.id.dishNameTV);
            viewHolder.ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
            viewHolder.numRanksTV = (TextView)convertView.findViewById(R.id.numRanksTV);
            viewHolder.dishPriceTV = (TextView)convertView.findViewById(R.id.dishPriceTV);

            viewHolder.availableTextView = (TextView) convertView.findViewById(R.id.today_available);
            viewHolder.notAvailableTextView = (TextView) convertView.findViewById(R.id.today_not_available);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.dishNameTV.setText(this.dishes.get(position).getDishName());
        viewHolder.ratingBar.setRating(this.dishes.get(position).getAvgRank());
        if(this.dishes.get(position).getIsTodayAvailable()) {
            viewHolder.availableTextView.setVisibility(View.VISIBLE);
            viewHolder.notAvailableTextView.setVisibility(View.GONE);
        }
        else{
            viewHolder.availableTextView.setVisibility(View.GONE);
            viewHolder.notAvailableTextView.setVisibility(View.VISIBLE);
        }
        viewHolder.numRanksTV.setText("("+String.valueOf(this.dishes.get(position).getNumRanks())+")");
        viewHolder.dishPriceTV.setText(String.valueOf(this.dishes.get(position).getPrice())+"€");

        Helper.setRatingBarColor(getContext(),
                viewHolder.ratingBar,
                this.dishes.get(position).getAvgRank());

        if(this.dishes.get(position).getThumbDownloadLink() != null){
            Glide.with(getContext())
                    .load(this.dishes.get(position).getThumbDownloadLink())
                    .into(viewHolder.dishPhotoIV);
        }

        if(this.dishes.get(position).getLargeDownloadLink() != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), MenuPhotoViewActivity.class);
                    i.putExtra("dish", dishes.get(position));
                    getContext().startActivity(i);
                }
            });
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
