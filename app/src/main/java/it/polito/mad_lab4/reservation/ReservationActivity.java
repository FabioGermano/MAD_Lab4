package it.polito.mad_lab4.reservation;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.reservation.food_order.*;

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
    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_request);

        this.restaurantID = getIntent().getExtras().getInt("restaurantId");
        if (isLargeDevice(getApplicationContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);

        setVisibilityAlert(false);
        invalidateOptionsMenu();

        nestedScrollView = (NestedScrollView)findViewById(R.id.reservationsNestedScrollView);

        calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.date_time);
        TextView name= (TextView) findViewById(R.id.restaurant_name);


        //restaurantID=3;
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
    protected User controlloLogin() {
        return new User(null, null, -1);
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

        this.reservationDayOfWeek= Helper.intToWeekString(getApplicationContext(), dayOfTheweek);
        //extract and set the reservation date
        this.reservationDate = date.substring(1,date.length());

        timeFragment= new TimeFragment();

        Bundle args = new Bundle();
        args.putBoolean("isToday", b);
        args.putString("timeRange", timeTable.get(Helper.fromCalendarOrderToMyOrder(dayOfTheweek)));
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

        scrollToEnd();

    }

    private void scrollToEnd() {
        this.nestedScrollView.scrollTo(0, nestedScrollView.getBottom());
    }

    @Override
    public void onTimeSelected(String time) {

        //set reservation time
        this.reservationTime = time;

        boolean onlyTakeaway = false, onlySeats= false;
        ArrayList<String> reservationType = restaurant.getBasicInfo().getTypesOfServices();
        // TA : only takeaway
        // R : only seats
        // TA, R: both
        // empty - if empty he shouldn't access this activity (shouldn't be able to make a reservation)

        if(reservationType.isEmpty()){
            throw new RuntimeException();
        }
        if(reservationType.size()<2) {
            if (reservationType.get(0).toLowerCase().equals("ta")) {
                onlyTakeaway = true;
                onlySeats = false;
            } else {
                onlyTakeaway = false;
                onlySeats = true;
            }
        }

        //cases :
        // onlyTakeaway true onlySeats false  -> vai direttamente ad ordinare cibo
        // onlySeats true onlyTakeaway fakse -> scegli il numero di posti e non puoi ordinare cibo
        // entrambi false comportamento standard

        Bundle b = new Bundle();

        //if only takeaway go directly to the foodorder activity
        if(onlyTakeaway && !onlySeats){
            this.choice_made=true;
            this.eat_in= false;
            goToFoodOrderAsTakeaway();

        }
        //if only seats let them choose how many seats
        else if (!onlyTakeaway && onlySeats ){
            this.eat_in= true;
            b.putBoolean("onlySeats", onlySeats);
        }

        if(!choice_made){
            choiceFragment = new ChoiceFragment();
            choiceFragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.choice_fragment_container, choiceFragment).commit();
            View cc = (View) (findViewById(R.id.choice_fragment_container));
        }

        scrollToEnd();
    }


    @Override
    public void updateSeatsNumber(int seats) {
        this.seats=seats;
    }


    public void goToFoodOrderAsEatin(){
        Intent i = new Intent(getApplicationContext(), FoodOrderActivity.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        i.putExtra("seats", seats);
        i.putExtra("restaurantId", restaurantID);
        i.putExtra("restaurantName", restaurantName);
        startActivity(i);
    }

    public void goToFoodOrderAsTakeaway(){
        Intent i = new Intent(getApplicationContext(), FoodOrderActivity.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        i.putExtra("restaurantId", restaurantID);
        i.putExtra("restaurantName", restaurantName);
        startActivity(i);
    }

    public void goToCheckOutAsEatin(){
        Intent i = new Intent(getApplicationContext(), CheckoutOrderActivity.class);
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
}
