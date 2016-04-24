package it.polito.mad_lab3.restaurant.menu_prev;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad_lab3.R;

/**
 * Created by f.germano on 24/04/2016.
 */
public class MenuListPrevFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public MenuListPrevFragment(){

    }

    public static MenuListPrevFragment newInstance(int sectionNumber) {
        MenuListPrevFragment fragment = new MenuListPrevFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_list_prev_fragment, container, false);

        return rootView;
    }
}
