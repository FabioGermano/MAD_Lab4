package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad_lab3.R;

/**
 * Created by Giovanna on 25/04/2016.
 */
public class MenuListFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public MenuListFragment(){

    }

    public static MenuListFragment newInstance(int sectionNumber) {
        MenuListFragment fragment = new MenuListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_list_fragment, container, false);

        return rootView;
    }
}

