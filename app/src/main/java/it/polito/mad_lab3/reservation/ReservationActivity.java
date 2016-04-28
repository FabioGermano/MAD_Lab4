package it.polito.mad_lab3.reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import it.polito.mad_lab3.R;

public class ReservationActivity extends AppCompatActivity implements ChoiceFragment.OnChoiceSelectedListener,CalendarFragment.OnDateSelectedListener, TimeFragment.OnTimeSelectedListener{

    private ChoiceFragment choiceFragment;
    private CalendarFragment calendarFragment;
    private TimeFragment timeFragment;
    private DishesFragment dishesFragment;
    private String reservationDate;
    private String reservationTime;
    private int seats;
    private boolean eat_in;
    View p, c;
    ArrayList<String> timeTable =  new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_request);

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
    public void onDateSelected(String date) {

        //TODO update the right view of time according to the day of the week and show the next step
        //extract the day of the week
        int dayOfTheweek = Integer.parseInt(date.substring(0,1));
        //extract the reservation date
        this.reservationDate = date.substring(1,date.length());

        //fake timeTable of the Restaurant


        timeFragment= new TimeFragment();

        Bundle args = new Bundle();
        args.putString("timeRange", timeTable.get(convertWeekDay(dayOfTheweek)));
        timeFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.time_fragment_container, timeFragment).commit();
        View t = (View) (findViewById(R.id.time_fragment_container));
        t.requestFocus();


    }


    @Override
    public void onTimeSelected(String time) {
        this.reservationTime = time;

        choiceFragment = new ChoiceFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.choice_fragment_container, choiceFragment).commit();
        View cc = (View) (findViewById(R.id.choice_fragment_container));
        cc.requestFocus();

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

    @Override
    public void onSeatsNumberSelected(boolean eat_in, int number) {
        this.eat_in=eat_in;
        this.seats=number;

        if(eat_in){
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                    ReservationActivity.this);

            // set title
            alertDialogBuilder.setTitle("Faster lunch!");
            alertDialogBuilder
                    .setMessage("Do you wanto to order already now?")
                    .setCancelable(true)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dishesFragment= new DishesFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment_container, dishesFragment).commit();

                        }
                    }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getBaseContext(), CheckoutOrder.class);
                            startActivity(i);
                            //TODO pass the checkout values (date, time, seats)
                    }
            });

            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
        else{
            dishesFragment= new DishesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment_container, dishesFragment).commit();

        }








    }
}
