package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import it.polito.mad_lab3.R;
import ivb.com.materialstepper.stepperFragment;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class TimeFragment extends stepperFragment {

    @Override
    public boolean onNextButtonHandler() {
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.time_fragment, container, false);
        int numCol = 4;
        String[] time ={"11:30", "12:00","12:30"};

        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        gridview.setNumColumns(numCol);

        gridview.setAdapter(new ChooseTimeAdapter(getContext(), time));
        return rootView;
    }
}
