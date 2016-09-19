package it.polito.mad_lab4.reservation.user_history;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.reservation.ReservedDish;

/**
 * Created by f.germano on 12/05/2016.
 */
public class ReservationDetailsFragment extends DialogFragment {

    private int seats=-1;
    private ArrayList<ReservedDish> data;
    private float total=0;
    private String notes;

    static ReservationDetailsFragment newInstance(int seats, ArrayList<ReservedDish> data, String notes) {
        ReservationDetailsFragment f = new ReservationDetailsFragment();
        f.setSeats(seats);
        f.setData(data);
        f.setNotes(notes);
        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.reservation_details_dialog, null);

        TextView seatsTextView= (TextView) v.findViewById(R.id.seats);
        TextView totalTextView = (TextView) v.findViewById(R.id.total);

        LinearLayout ll = (LinearLayout) v.findViewById(R.id.food_order);
        LinearLayout seatsLL = (LinearLayout) v.findViewById(R.id.seatsLL);
        LinearLayout totalLL = (LinearLayout) v.findViewById(R.id.totalLL);
        LinearLayout notesLL = (LinearLayout) v.findViewById(R.id.notesLL);
        TextView notesTextView = (TextView) v.findViewById(R.id.notes);

        if(seats>=1)
            seatsTextView.setText(String.valueOf(seats) + " "+ getResources().getString(R.string.seats_string));
        else{
            seatsLL.setVisibility(View.GONE);
        }
        if(notes != null && !notes.equals("")){
            notesLL.setVisibility(View.VISIBLE);
            notesTextView.setText(notes);
        }
        else{
            notesLL.setVisibility(View.GONE);
        }
        if(data != null && !data.isEmpty()){
            fillLayout(getActivity(),ll, data );
            BigDecimal result;
            result= Helper.round(total,2);
            totalTextView.setText(String.valueOf(result)+" €");
        }
        else {
            ll.setVisibility(View.GONE);
            totalLL.setVisibility(View.GONE);
        }

        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setCancelable(true);


        return builder.create();
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public ArrayList<ReservedDish> getData() {
        return data;
    }

    public void setData(ArrayList<ReservedDish> data) {
        this.data = data;
    }

    public void fillLayout(Context context, LinearLayout ll, ArrayList<ReservedDish> list) {
        for (ReservedDish d : list) {
            if(d.getQuantity()>0){
                View child = LayoutInflater.from(context).inflate(R.layout.your_order_row, null);
                TextView name = (TextView) child.findViewById(R.id.food_name);
                name.setText(d.getName());
                TextView quantity = (TextView) child.findViewById(R.id.food_quantity);
                quantity.setText(d.getQuantity() + " x ");
                TextView price = (TextView) child.findViewById(R.id.food_price);
                BigDecimal result;
                result= Helper.round(d.getPrice(),2);
                price.setText(String.valueOf(result) + " €");
                this.total+=d.getQuantity() * d.getPrice();
                ll.addView(child);
            }
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
