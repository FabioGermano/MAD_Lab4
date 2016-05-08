package it.polito.mad_lab3.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab3.MainActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.restaurant.BasicInfo;
import it.polito.mad_lab3.data.restaurant.Restaurant;

/**
 * Created by Giovanna on 07/05/2016.
 */
public class BasicInfoFragment extends Fragment {

    private int restaurantId=-1;
    private TextView addressTextView, phoneNumberTextView, closedTextView, openTextView;
    private BasicInfo basicInfo;
    private Button showMore;
    private String restaurantName;

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
                i.putExtra("basicInfo", (Serializable)basicInfo);
                i.putExtra("restaurantName", restaurantName);
                startActivity(i);
            }
        });

        ((ImageView)rootView.findViewById(R.id.mapIcon)).setColorFilter(Color.BLACK);
        ((ImageView)rootView.findViewById(R.id.phoneIcon)).setColorFilter(Color.BLACK);

                return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        addressTextView.setText(basicInfo.getAddress());
        phoneNumberTextView.setText(basicInfo.getPhone());

        //check time and date
        String isOpen = isOpen(basicInfo.getTimeTable());

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

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
        this.basicInfo= RestaurantBL.getRestaurantById(getContext(),this.restaurantId).getBasicInfo();
        this.restaurantName = RestaurantBL.getRestaurantById(getContext(),this.restaurantId).getRestaurantName();
    }
}
