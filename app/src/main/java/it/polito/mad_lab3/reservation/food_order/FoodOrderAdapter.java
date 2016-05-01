package it.polito.mad_lab3.reservation.food_order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.reservation.Dish;

/**
 * Created by Giovanna on 28/04/2016.
 */
public class FoodOrderAdapter extends ArrayAdapter<Dish>{

    private ArrayList<Dish> data;

    ListView listView;
    private int section;

    public FoodOrderAdapter(Context context, ArrayList<Dish> objects, int section) {
        super(context, 0, objects);
        this.data=objects;
        this.section=section;
    }

    // View lookup cache
    private static class ViewHolder {
        int counter;
        TextView name;
        TextView price;
        TextView quantity;
        ImageButton plus;
        ImageButton minus;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Dish dish = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.food_order_item, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.dish_name);
            viewHolder.price = (TextView) convertView.findViewById(R.id.dish_price);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.dish_quantity);
            viewHolder.minus = (ImageButton) convertView.findViewById(R.id.minus);
            viewHolder.plus = (ImageButton) convertView.findViewById(R.id.plus);
            viewHolder.minus.setTag(position);
            viewHolder.plus.setTag(position);
            viewHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewHolder.counter++;
                    viewHolder.quantity.setText(String.valueOf(viewHolder.counter));

                }
            });

            viewHolder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.counter>0) {
                        viewHolder.counter--;
                        viewHolder.quantity.setText(String.valueOf(viewHolder.counter));
                    }

                }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(dish.getDishName());
        viewHolder.price.setText(String.valueOf(dish.getPrice())+" €");
        viewHolder.quantity.setText(String.valueOf(dish.getQuantity()));
        viewHolder.counter=dish.getQuantity();

        // Return the completed view to render on screen
        return convertView;
    }

}

