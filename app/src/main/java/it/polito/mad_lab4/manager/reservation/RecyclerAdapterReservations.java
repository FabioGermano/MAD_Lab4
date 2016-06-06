package it.polito.mad_lab4.manager.reservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.reservation.Reservation;

/**
 * Created by Giovanna on 12/04/2016.
 */
public class RecyclerAdapterReservations extends RecyclerView.Adapter<ReservationHolder> {

    private LayoutInflater myInflater;
    private Context context;
    private ReservationFragment containerFragment;
    private ArrayList<Reservation> data;

    public RecyclerAdapterReservations(Context context, ArrayList<Reservation> data, ReservationFragment containerFragment) {
        this.data = data;
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
        this.containerFragment = containerFragment;
    }

    @Override
    public ReservationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.reservation_row, parent, false);
        ReservationHolder holder = new ReservationHolder(v, context, containerFragment, data);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReservationHolder holder, int position) {
        holder.setData(data.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
