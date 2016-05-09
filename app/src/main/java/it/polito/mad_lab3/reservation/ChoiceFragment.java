package it.polito.mad_lab3.reservation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
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

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.reservation.food_order.FoodOrderActivity;

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
    private Button checkOut, orderNow;
    private TextView counter;
    private int cnt;
    private boolean onlyTakeaway, onlySeats;

    public interface OnChoiceSelectedListener {

        void updateSeatsNumber(int seats);
        void onChoiceMade(boolean eat_in);
        void onCheckOutChoiceMade(boolean checkoutNow);


    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cnt=1;
        mCallback.updateSeatsNumber(cnt);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.choice_fragment, container, false);

        LinearLayout choice_layout = (LinearLayout) rootView.findViewById(R.id.choice_layout);
        //eatin = (Button) rootView.findViewById(R.id.eat_in);
        eatin = (ImageButton) rootView.findViewById(R.id.eat_in);
        //takeaway = (Button) rootView.findViewById(R.id.takeaway);
        takeaway = (ImageButton) rootView.findViewById(R.id.takeaway);


        seats_layout = (LinearLayout) rootView.findViewById(R.id.seats);
        plus = (ImageButton) seats_layout.findViewById(R.id.plus);
        minus = (ImageButton) seats_layout.findViewById(R.id.minus);
        counter= (TextView) seats_layout.findViewById(R.id.counter);

        checkOut = (Button) rootView.findViewById(R.id.checkout);
        orderNow = (Button) rootView.findViewById(R.id.order_food);


        if(!getArguments().isEmpty()){
            onlySeats = getArguments().getBoolean("onlySeats");
            if(onlySeats) {
                choice_layout.setVisibility(View.GONE);
                seats_layout.setVisibility(View.VISIBLE);
                checkOut.setVisibility(View.VISIBLE);
                orderNow.setVisibility(View.GONE);
            }

        }


        else {
            seats_layout.setVisibility(View.GONE);
            checkOut.setVisibility(View.GONE);
            orderNow.setVisibility(View.GONE);
        }

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCheckOutChoiceMade(true);

            }
        });
        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onCheckOutChoiceMade(false);
            }
        });


        //plus.setColorFilter(getResources().getColor(R.color.themeColorLighter));
        //minus.setColorFilter(getResources().getColor(R.color.themeColorLighter));



        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt++;
                counter.setText(String.valueOf(cnt));
                mCallback.updateSeatsNumber(cnt);
            }
        });
        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnt>1)
                    cnt--;
                counter.setText(String.valueOf(cnt));
                mCallback.updateSeatsNumber(cnt);
            }
        });

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
                               mCallback.onChoiceMade(true);
                               checkOut.setVisibility(View.VISIBLE);
                               orderNow.setVisibility(View.VISIBLE);


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
                                checkOut.setVisibility(View.GONE);
                                orderNow.setVisibility(View.GONE);
                                mCallback.onChoiceMade(false);

                            }
                        });
            }
        });


        return rootView;

    }
}
