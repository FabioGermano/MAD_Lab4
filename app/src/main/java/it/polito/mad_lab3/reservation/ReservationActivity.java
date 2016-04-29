package it.polito.mad_lab3.reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.reservation.food_order.*;

public class ReservationActivity extends BaseActivity implements ChoiceFragment.OnChoiceSelectedListener,CalendarFragment.OnDateSelectedListener, TimeFragment.OnTimeSelectedListener{

    private ChoiceFragment choiceFragment;
    private CalendarFragment calendarFragment;
    private TimeFragment timeFragment;
    private String reservationDate;
    private String reservationTime;
    private String reservationDayOfWeek;
    private int seats;
    private boolean eat_in;
    private boolean completed=false;
    private ArrayList<String> currentDates;
    View p, c;
    ArrayList<String> timeTable =  new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_request);

        setActivityTitle("Make a reservation");

        timeTable.add(0, "10:20 - 14:30");
        timeTable.add(1, "10:30 - 12:30");
        timeTable.add(2, "10:45 - 12:30");
        timeTable.add(3, "10:30 - 12:45");
        timeTable.add(4, "10:30 - 12:30");
        timeTable.add(5, "10:30 - 12:30");
        timeTable.add(6, "10:30 - 12:30");

        calendarFragment = (CalendarFragment) getSupportFragmentManager().findFragmentById(R.id.date_time);

        c = (View) (findViewById(R.id.date_time));
        c.setVisibility(View.VISIBLE);


    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }


    @Override
    public void onDateSelected(String date) {

        //TODO update the right view of time according to the day of the week and show the next step
        //extract the day of the week
        int dayOfTheweek = Integer.parseInt(date.substring(0,1));

        this.reservationDayOfWeek= intToWeekString(dayOfTheweek);
        //extract and set the reservation date
        this.reservationDate = date.substring(1,date.length());

        timeFragment= new TimeFragment();

        Bundle args = new Bundle();
        args.putString("timeRange", timeTable.get(convertWeekDay(dayOfTheweek)));
        timeFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.time_fragment_container, timeFragment).commit();
        View t = (View) (findViewById(R.id.time_fragment_container));

        if(choiceFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(choiceFragment).commit();
            completed=false;
        }
        if(reservationTime!=null) {
            this.reservationTime = null;
            completed=false;
        }

    }

    @Override
    public void initialValue(String date) {
        onDateSelected(date);
    }


    @Override
    public void onTimeSelected(String time) {

        //set reservation time
        this.reservationTime = time;

        choiceFragment = new ChoiceFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.choice_fragment_container, choiceFragment).commit();
        View cc = (View) (findViewById(R.id.choice_fragment_container));
        cc.requestFocus();

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
        startActivity(i);
    }

    public void goToFoodOrderAsTakeaway(){
        Intent i = new Intent(getBaseContext(), FoodOrderActivity.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        startActivity(i);
    }

    public void goToCheckOutAsEatin(){
        Intent i = new Intent(getBaseContext(), CheckoutOrder.class);
        i.putExtra("date", reservationDate);
        i.putExtra("weekday", reservationDayOfWeek);
        i.putExtra("time", reservationTime);
        i.putExtra("seats", seats);
        startActivity(i);

    }


    @Override
    public void onChoiceMade(boolean eat_in) {
        //set choice and number of seats
        this.eat_in=eat_in;

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
