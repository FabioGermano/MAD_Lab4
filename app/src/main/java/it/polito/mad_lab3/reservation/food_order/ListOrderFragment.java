package it.polito.mad_lab3.reservation.food_order;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.Dish;

/**
 * Created by Giovanna on 25/04/2016.
 */
public class ListOrderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView listView;
    private ArrayList<Dish> data;
    private Context context;

    public ListOrderFragment(){

    }



    public static ListOrderFragment newInstance(int sectionNumber, Context context, ArrayList<Dish> data) {
        ListOrderFragment fragment = new ListOrderFragment();
        fragment.setData(data);
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

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new FoodOrderAdapter(getContext(),data));


        return rootView;
    }

    public ArrayList<Dish> getData() {
        return data;
    }

    public void setData(ArrayList<Dish> data) {
        this.data = data;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

