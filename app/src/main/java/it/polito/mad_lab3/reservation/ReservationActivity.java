package it.polito.mad_lab3.reservation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.reservation.food_order.*;

public class ReservationActivity extends BaseActivity implements ChoiceFragment.OnChoiceSelectedListener,CalendarFragment.OnDateSelectedListener, TimeFragment.OnTimeSelectedListener{

    private ChoiceFragment choiceFragment;
    private CalendarFragment calendarFragment;
    private TimeFragment timeFragment;
    private String reservationDate;
    private String reservationTime;
    private String reservationDayOfWeek;
    private String restaurantName;
    private int seats;
    private boolean choice_made;
    private boolean eat_in;
    private boolean completed=false;
    private ArrayList<String> currentDates;
    private int restaurantID = -1;
    private Restaurant restaurant;
    View p, c;
    ArrayList<String> timeTable =  new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_request);


        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);
        calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.date_time);
        TextView name= (TextView) findViewById(R.id.restaurant_name);


        restaurantID=1;
        restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantID);
        this.timeTable= restaurant.getBasicInfo().getTimeTable();
        this.restaurantName= restaurant.getRestaurantName();
        name.setText(restaurantName);

        if(reservationDate==null) {
            final Calendar c = Calendar.getInstance();
            int year, month, day, week_day;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            week_day = c.get(Calendar.DAY_OF_WEEK);
            String wd = String.valueOf(week_day);
            String first_date = wd + year + "-" + month + "-" + day;
            onDateSelected(first_date, true);
        }


    }

    @Override
    protected void filterButton() {

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }


    @Override
    public void onDateSelected(String date, boolean b) {

        //TODO update the right view of time according to the day of the week and show the next step
        //extract the day of the week
        int dayOfTheweek = Integer.parseInt(date.substring(0,1));

        this.reservationDayOfWeek= intToWeekString(dayOfTheweek);
        //extract and set the reservation date
        this.reservationDate = date.substring(1,date.length());

        timeFragment= new TimeFragment();

        Bundle args = new Bundle();
        args.putBoolean("isToday", b);
        args.putString("timeRange", timeTable.get(convertWeekDay(dayOfTheweek)));
        timeFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().setCustomAnimations(0, 0).replace(R.id.time_fragment_container, timeFragment).commit();
        View t = (View) (findViewById(R.id.time_fragment_container));
        //t.requestFocus();

        if(choiceFragment != null) {
            this.choice_made=false;
            getSupportFragmentManager().beginTransaction().remove(choiceFragment).commit();

        }
        if(reservationTime!=null) {
            this.reservationTime = null;
        }

    }

    @Override
    public void onTimeSelected(String time) {

        //set reservation time
        this.reservationTime = time;

        if(!choice_made){

        choiceFragment = new ChoiceFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.choice_fragment_container, choiceFragment).commit();
        View cc = (View) (findViewById(R.id.choice_fragment_container));
        }

    }


    @Override
    public void updateSeatsNumber(int seats) {
        this.seats=seats;
    }


    public void goToFoodOrderAsEatin(){
        Intent i = new Intent(getBaseContext(), FoodOrderActivity.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        i.putExtra("seats", seats);
        i.putExtra("restaurantId", restaurantID);
        i.putExtra("restaurantName", restaurantName);
        startActivity(i);
    }

    public void goToFoodOrderAsTakeaway(){
        Intent i = new Intent(getBaseContext(), FoodOrderActivity.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        i.putExtra("restaurantId", restaurantID);
        i.putExtra("restaurantName", restaurantName);
        startActivity(i);
    }

    public void goToCheckOutAsEatin(){
        Intent i = new Intent(getBaseContext(), CheckoutOrder.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        i.putExtra("seats", seats);
        i.putExtra("restaurantId", restaurantID);
        i.putExtra("restaurantName", restaurantName);
        startActivity(i);

    }


    @Override
    public void onChoiceMade(boolean eat_in) {
        //set choice and number of seats
        this.eat_in=eat_in;
        this.choice_made=true;
        if(!this.eat_in){
            goToFoodOrderAsTakeaway();
        }
    }

    @Override
    public void onCheckOutChoiceMade(boolean checkoutNow) {
        if(checkoutNow)
            goToCheckOutAsEatin();
        else
            goToFoodOrderAsEatin();
    }

    public int convertWeekDay(int wd){
        switch (wd){
            case 1:
                return 6;

            case 2:
                return 0;

            case 3:
                return 1;

            case 4:
                return 2;

            case 5:
                return 3;

            case 6:
                return 4;

            case 7:
                return 5;

            default:
                return 0;
        }

    }
    private String intToWeekString (int weekday){
        switch (weekday){
            case 1:
                return getResources().getString(R.string.sunday);

            case 2:
                return getResources().getString(R.string.monday);

            case 3:
                return getResources().getString(R.string.tuesday);

            case 4:
                return getResources().getString(R.string.wednesday);

            case 5:
                return getResources().getString(R.string.thursday);

            case 6:
                return getResources().getString(R.string.friday);

            case 7:
                return getResources().getString(R.string.saturday);
            default:
                return null;
        }
    }
}
