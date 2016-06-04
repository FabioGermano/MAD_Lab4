package it.polito.mad_lab4.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

/**
 * Created by Giovanna on 07/05/2016.
 */
public class BasicInfoFragment extends Fragment {

    private TextView addressTextView, phoneNumberTextView, closedTextView, openTextView;
    private Button showMore;
    private LinearLayout call, location;
    private Restaurant restaurant;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.restaurant_basicinfo_fragment, container, false);

        addressTextView = (TextView) rootView.findViewById(R.id.address);
        phoneNumberTextView = (TextView) rootView.findViewById(R.id.phoneNumber);
        openTextView = (TextView) rootView.findViewById(R.id.now_open);
        closedTextView = (TextView) rootView.findViewById(R.id.now_closed);
        showMore = (Button) rootView.findViewById(R.id.show_more);

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ShowAdditionalInfoActivity.class);
                i.putExtra("restaurant", restaurant);
                startActivity(i);
            }
        });

        ((ImageView)rootView.findViewById(R.id.mapIcon)).setColorFilter(Color.BLACK);
        ((ImageView)rootView.findViewById(R.id.phoneIcon)).setColorFilter(Color.BLACK);

        call = (LinearLayout) rootView.findViewById(R.id.telIconLL);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.dialNumber(getContext(), restaurant.getPhone());
            }
        });
        location = (LinearLayout) rootView.findViewById(R.id.mapIconLL);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.findOnGoogleMaps(getContext(),restaurant.getAddress(), restaurant.getCity());
            }
        });

                return rootView;
    }

    private String isOpen(ArrayList<String> timeTable) {

        Calendar now = Calendar.getInstance();
        int weekday = now.get(Calendar.DAY_OF_WEEK);
        int hour = now.get(Calendar.HOUR_OF_DAY); // 24 format
        int minutes = now.get(Calendar.MINUTE);

        String range = timeTable.get(Helper.fromCalendarOrderToMyOrder(weekday));
        //extract orario
        int orario[] = Helper.formatRange(range);

        //store in a calendar object the closing time
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, orario[2]);
        end.set(Calendar.MINUTE, orario[3]);
        end.set(Calendar.SECOND, 0);

        //restaurant closed
        if ( hour < orario[0] || (hour == orario[0] && minutes < orario[1])|| hour > end.get(Calendar.HOUR_OF_DAY)  ||  (hour == end.get(Calendar.HOUR_OF_DAY) && minutes > end.get(Calendar.MINUTE)))
            return null;

        //if open
        return getResources().getString(R.string.open_restaurant);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;

        addressTextView.setText(restaurant.getAddress());
        phoneNumberTextView.setText(restaurant.getPhone());

        //check time and date
        String isOpen = isOpen(restaurant.getTimeTable());

        if(isOpen==null) {
            closedTextView.setVisibility(View.VISIBLE);
            openTextView.setVisibility(View.GONE);
            closedTextView.setText(getResources().getString(R.string.closed_restaurant));
        }
        else{
            openTextView.setVisibility(View.VISIBLE);
            closedTextView.setVisibility(View.GONE);
            openTextView.setText(isOpen);
        }
    }
}
