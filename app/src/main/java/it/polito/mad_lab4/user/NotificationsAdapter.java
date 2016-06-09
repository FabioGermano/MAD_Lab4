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
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.user.Notification;
import it.polito.mad_lab4.reservation.user_history.ReservationsHistoryActivity;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Giovanna on 29/05/2016.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    ArrayList<Notification> data;
    Activity activity;

    public NotificationsAdapter(Context context, Activity activity, ArrayList<Notification> objects) {
        this.context= context;
        this.data= objects;
        this.activity=activity;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int position;
        ImageView icon;
        CardView cardView;
        TextView message, restaurant_message;

        public ViewHolder(View v){
            super(v);
            icon = (ImageView)  v.findViewById(R.id.icon);
            cardView = (CardView)  v.findViewById(R.id.cardView);
            message = (TextView)  v.findViewById(R.id.message);
            restaurant_message = (TextView)  v.findViewById(R.id.restaurant_message);
        }

        public void setData(int position, Notification notification) {

            this.position=position;
            String s = new String();
            if(notification.isNewOffer()){
                icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_local_offer_holo_light));
                s = context.getResources().getString(R.string.the_restaurant)+notification.getRestaurantName()+context.getResources().getString(R.string.new_offer_added);

            }
            else {
                if(notification.isAccepted()){
                    icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_event_available_holo_light));
                    s= context.getResources().getString(R.string.the_restaurant)+notification.getRestaurantName()+context.getResources().getString(R.string.accepted_your_reserv);
                }
                else{
                    icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_event_busy_holo_light));
                    s= context.getResources().getString(R.string.the_restaurant)+notification.getRestaurantName()+context.getResources().getString(R.string.rejected_your_reserv);
                }
            }
            if (notification.getMessage()!=null && !notification.getMessage().equals("")){
                String tmp = "\""+notification.getMessage()+"\"";
                restaurant_message.setText(tmp);
            }
            else
                restaurant_message.setVisibility(View.GONE);
            message.setText(s);
        }

        public void setListeners() {

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(position).isNewOffer())
                        goToRestaurantActivity();
                    else
                        goToHistoryActivity();
                }
            });}

        private void goToHistoryActivity() {
            Intent i= new Intent(context, ReservationsHistoryActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            activity.finish();
        }


        private void goToRestaurantActivity() {

            Notification n = data.get(position);
            Intent i= new Intent(context, RestaurantActivity.class);
            //TODO hard code
            i.putExtra("restaurantId", 2);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            activity.finish();
        }

    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_list_item, parent, false);
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

        Notification n = data.get(position);
        holder.setData(position, n);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }

}
