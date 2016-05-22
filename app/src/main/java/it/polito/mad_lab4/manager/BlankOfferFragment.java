package it.polito.mad_lab4.manager;

/**
 * Created by Eugenio on 07/04/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.restaurant.Offer;

public class BlankOfferFragment extends Fragment {
    private ArrayList<Offer> offer_list;
    private Context context;
    private boolean mode;


    public BlankOfferFragment() {
        // Required empty public constructor

    }

    public void setValue(ArrayList<Offer> obj, Context c){
        this.offer_list= obj;
        this.context = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mode= getArguments().getBoolean("availability");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_blank_offer, container, false);
        
        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.recyclerView_offerte);
        RecyclerAdapter_offerte myAdapter = new RecyclerAdapter_offerte(context,offer_list, mode);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(getActivity());
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }

        return rootView;
    }

}
