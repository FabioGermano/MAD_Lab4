package it.polito.mad_lab4.restaurant.reviews.add_review;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.ReviewFood;

/**
 * Created by Giovanna on 25/04/2016.
 */
public class SectionFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView listView;
    private ArrayList<ReviewFood> data;
    private Context context;
    private int position;
    private boolean isOrder;

    public SectionFragment(){

    }



    public static SectionFragment newInstance(int sectionNumber, Context context, ArrayList<ReviewFood> data) {
        SectionFragment fragment = new SectionFragment();
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
        View rootView = inflater.inflate(R.layout.rate_dishes, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(new RateAdapter(context, data, position));


        return rootView;
    }

    public ArrayList<ReviewFood> getData() {
        return data;
    }

    public void setData(ArrayList<ReviewFood> data) {
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

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

}

