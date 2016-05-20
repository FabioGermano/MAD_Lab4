package it.polito.mad_lab4.restaurant.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.reservation.ReservedDish;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.reservation.ReservationActivity;

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

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.dishNameTV.setText(this.dishes.get(position).getDishName());
        viewHolder.ratingBar.setRating(this.dishes.get(position).getAvgRank());
        viewHolder.numRanksTV.setText("("+String.valueOf(this.dishes.get(position).getNumRanks())+")");
        viewHolder.dishPriceTV.setText(String.valueOf(this.dishes.get(position).getPrice())+"€");

        Helper.setRatingBarColor(getContext(),
                viewHolder.ratingBar,
                this.dishes.get(position).getAvgRank());

        boolean clickable = false;
        if(this.dishes.get(position).getThumbPath() != null) {
            viewHolder.dishPhotoIV.setImageBitmap(BitmapFactory.decodeFile(this.dishes.get(position).getThumbPath()));
            clickable = true;
        }
        else if(this.dishes.get(position).getResPhoto() != null) {
            int imgRes = Helper.getResourceByName(getContext(), this.dishes.get(position).getResPhoto(), "drawable");
            if (imgRes != 0) {
                viewHolder.dishPhotoIV.setImageResource(imgRes);
                clickable = true;
            }
        }

        if(clickable) {
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
