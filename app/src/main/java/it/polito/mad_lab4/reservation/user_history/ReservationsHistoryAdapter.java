package it.polito.mad_lab4.reservation.user_history;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservedDish;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Giovanna on 12/05/2016.
 */


public class ReservationsHistoryAdapter extends RecyclerView.Adapter<ReservationsHistoryAdapter.ViewHolder> {

    private Context context;
    ArrayList<Reservation> data;
    Activity activity;

    public ReservationsHistoryAdapter(Context context, Activity activity,  ArrayList<Reservation> objects) {
        this.context= context;
        this.activity= activity;
        this.data= objects;
        // prova
    }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            // each data item is just a string in this case
            TextView restaurantName, date, time, address, status;
            Button info, restaurantPage, cancel;
            int position;

            public ViewHolder(View v){
                super(v);

                restaurantName = (TextView)  v.findViewById(R.id.restaurant_name);
                address = (TextView)  v.findViewById(R.id.address);
                time = (TextView)  v.findViewById(R.id.time);
                date = (TextView)  v.findViewById(R.id.date);
                status = (TextView)  v.findViewById(R.id.status);

                info = (Button)  v.findViewById(R.id.info);
                restaurantPage = (Button)  v.findViewById(R.id.restaurant);
                cancel = (Button) v.findViewById(R.id.cancelBtn);


            }

            public void setData(int position, Reservation r, Restaurant restaurant) {

                restaurantName.setText(restaurant.getRestaurantName());
                address.setText(restaurant.getAddress()+" - "+restaurant.getCity());
                date.setText(r.getDate());
                time.setText(r.getTime());
                status.setText(r.getStatus());
                this.position=position;

                //TODO handle status of reservation in order to make visible button cancel
                /*
                if(r.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)) || r.getStatus().equals(ReservationTypeConverter.toString(ReservationType.PENDING)))
                    cancel.setVisibility(View.VISIBLE);
                else
                    cancel.setVisibility(View.GONE);*/
            }

            public void setListeners() {
                cancel.setOnClickListener(ViewHolder.this);
                restaurantPage.setOnClickListener(ViewHolder.this);
                info.setOnClickListener(ViewHolder.this);
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancelBtn:
                        removeReservation();
                        break;
                    case R.id.restaurant:
                        goToRestaurantActivity();
                        break;
                    case R.id.info:
                        showOrderDetails();
                        break;
                }
            }

            private void goToRestaurantActivity() {

                Reservation r = data.get(position);
                Intent i= new Intent(context, RestaurantActivity.class);
                Restaurant restaurant =null; //= RestaurantBL.getRestaurantById(context, r.getRestaurantId());
                i.putExtra("restaurantId", restaurant.getRestaurantId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }

            private void showOrderDetails() {

                Reservation r = data.get(position);
                //TODO recuperare dal db i dati relativi alla prenotazione
                ArrayList<ReservedDish> reservedDishes=null;
                DialogFragment newFragment = ReservationDetailsFragment.newInstance(Integer.parseInt(r.getPlaces()), reservedDishes);
                newFragment.show(activity.getFragmentManager(), "dialog");
            }

            private void removeReservation() {
                Reservation r = data.get(position);
                //TODO handle the status of the reservation in order to remove it properly
                /*
                if(r.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED))){
                    // se in stato accettato
                    UserBL.cancelReservation(UserBL.getUserById(context, UserSession.userId),r.getReservationId(), false);

                }
                else if(r.getStatus().equals(ReservationTypeConverter.toString(ReservationType.PENDING))){
                    UserBL.cancelReservation(UserBL.getUserById(context, UserSession.userId),r.getReservationId(), true);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());
                }
                */
                //UserBL.saveChanges(context);
                Toast.makeText(activity, R.string.reservation_canceled, Toast.LENGTH_SHORT ).show();
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ReservationsHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.reservations_history_item, parent, false);
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

            Reservation r = data.get(position);
            //TODO
            Restaurant restaurant = null; //= RestaurantBL.getRestaurantById(context, r.getRestaurantId());
            holder.setData(position, r, restaurant);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return data.size();
        }

}