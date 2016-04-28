package it.polito.mad_lab3.reservation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import it.polito.mad_lab3.R;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class ChoiceFragment extends Fragment {

    private OnChoiceSelectedListener mCallback;
    //private Button eatin, takeaway;
    private ImageButton eatin, takeaway;
    //private NumberPicker numberPicker;
    private LinearLayout seats_layout;
    private boolean confirmed= false;
    private int seats;
    private ImageButton plus, minus;
    private TextView counter;
    private int cnt;

    public interface OnChoiceSelectedListener {

        public void onSeatsNumberSelected( boolean eat_in, int number);

    }

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
        cnt=1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnChoiceSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChoiceSelectedListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.choice_fragment, container, false);

        //eatin = (Button) rootView.findViewById(R.id.eat_in);
        eatin = (ImageButton) rootView.findViewById(R.id.eat_in);
        //takeaway = (Button) rootView.findViewById(R.id.takeaway);
        takeaway = (ImageButton) rootView.findViewById(R.id.takeaway);

        seats_layout = (LinearLayout) rootView.findViewById(R.id.seats);
        seats_layout.setVisibility(View.GONE);

        plus = (ImageButton) seats_layout.findViewById(R.id.plus);
        minus = (ImageButton) seats_layout.findViewById(R.id.minus);
        counter= (TextView) seats_layout.findViewById(R.id.counter);

        //plus.setColorFilter(getResources().getColor(R.color.themeColorLighter));
        //minus.setColorFilter(getResources().getColor(R.color.themeColorLighter));



        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt++;
                counter.setText(String.valueOf(cnt));
            }
        });
        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt>1)
                    cnt--;
                counter.setText(String.valueOf(cnt));
            }
        });


        /*numberPicker = (NumberPicker) rootView.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(8);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected value from picker
                    seats= newVal;
                    mCallback.onSeatsNumberSelected(true, seats);
            }
        });
        */

       eatin.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               //eatin.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
               //takeaway.setBackgroundColor(getActivity().getResources().getColor(R.color.themeColorLighter));
               takeaway.getBackground().setColorFilter(getResources().getColor(R.color.themeColorLighter), PorterDuff.Mode.SRC_OVER);
               eatin.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_OVER);

               seats_layout.animate()
                       .alpha(255)
                       .setDuration(300)
                       .setListener(new AnimatorListenerAdapter() {
                           @Override
                           public void onAnimationEnd(Animator animation) {
                               super.onAnimationEnd(animation);
                               seats_layout.setVisibility(View.VISIBLE);
                               mCallback.onSeatsNumberSelected(true, cnt);
                           }
                       });

           }
       });

        takeaway.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                //eatin.getBackground().setTint(getResources().getColor(R.color.themeColorLighter));
                eatin.getBackground().setColorFilter(getResources().getColor(R.color.themeColorLighter), PorterDuff.Mode.SRC_OVER);
                takeaway.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_OVER);

                seats_layout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                seats_layout.setVisibility(View.GONE);
                                mCallback.onSeatsNumberSelected(false, 0);

                            }
                        });
            }
        });


        return rootView;

    }
}
