package it.polito.mad_lab3.reservation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import ivb.com.materialstepper.stepperFragment;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class PeopleFragment extends Fragment {

    private Button eatin, takeaway;
    private NumberPicker numberPicker;
    private boolean confirmed= false;
    private int seats;

   /* @Override
    public boolean onNextButtonHandler() {


        if(numberPicker.getVisibility() == View.VISIBLE){
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                    getActivity());

            // set title
            alertDialogBuilder.setTitle("Confirm the number of seats");
            alertDialogBuilder
                    .setMessage("You have selected "+seats+"seats.")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            confirmed=true;
                        }
                    })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                            confirmed=false;

                }
            });

            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
            if (!confirmed)
                return false;
        return true;
    }*/

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.people_fragment, container, false);

        eatin = (Button) rootView.findViewById(R.id.eat_in);
        takeaway = (Button) rootView.findViewById(R.id.takeaway);
        numberPicker = (NumberPicker) rootView.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(8);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                    seats= newVal;
            }
        });

       eatin.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               eatin.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
               takeaway.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

               numberPicker.animate()
                       .alpha(1.0f)
                       .setDuration(300)
                       .setListener(new AnimatorListenerAdapter() {
                           @Override
                           public void onAnimationEnd(Animator animation) {
                               super.onAnimationEnd(animation);
                               numberPicker.setVisibility(View.VISIBLE);
                           }
                       });

           }
       });

        takeaway.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                takeaway.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
                eatin.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));

                numberPicker.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                numberPicker.setVisibility(View.GONE);
                            }
                        });
            }
        });


        return rootView;

    }
}
