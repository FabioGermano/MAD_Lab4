package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad_lab3.R;
import ivb.com.materialstepper.stepperFragment;
/**
 * Created by Giovanna on 23/04/2016.
 */
public class CalendarFragment extends stepperFragment {

    @Override
    public boolean onNextButtonHandler() {
        return true;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.calendar_fragment, container, false);
    }
}
