package it.polito.mad_lab4.reservation.food_order;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.reservation.ReservedDish;

/**
 * Created by Giovanna on 25/04/2016.
 */
public class ListOrderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView listView;
    private ArrayList<ReservedDish> data;
    private Context context;
    private int position;

    public ListOrderFragment(){

    }



    public static ListOrderFragment newInstance(int sectionNumber, Context context, ArrayList<ReservedDish> data) {
        ListOrderFragment fragment = new ListOrderFragment();
        fragment.setData(data);
        fragment.setPosition(sectionNumber);
        fragment.setContext(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_order_fragment, container, false);
        View header = inflater.inflate(R.layout.food_order_header, null);
        TextView headerTextViewL = (TextView) header.findViewById(R.id.dish);
        TextView headerTextViewR = (TextView) header.findViewById(R.id.quantity);
        if(position==0) {
            headerTextViewL.setText(getResources().getString(R.string.offer));
        }
        else
            headerTextViewL.setText(getResources().getString(R.string.dish));

        headerTextViewR.setText(getResources().getString(R.string.quantity));

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.addHeaderView(header);
        listView.setAdapter(new FoodOrderAdapter(context,data, position));


        return rootView;
    }

    public ArrayList<ReservedDish> getData() {
        return data;
    }

    public void setData(ArrayList<ReservedDish> data) {
        this.data = data;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

