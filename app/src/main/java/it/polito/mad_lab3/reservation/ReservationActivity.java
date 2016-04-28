package it.polito.mad_lab3.reservation;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import it.polito.mad_lab3.R;

public class ReservationActivity extends AppCompatActivity implements CalendarFragment.OnDateSelectedListener, TimeFragment.OnTimeSelectedListener{

    private PeopleFragment peopleFragment;
    private CalendarFragment calendarFragment;
    private TimeFragment timeFragment;
    private String reservationDate;
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
        reservationDate = date.substring(1,date.length());

        //fake timeTable of the Restaurant


        timeFragment= new TimeFragment();

        Bundle args = new Bundle();
        args.putString("timeRange", timeTable.get(convertWeekDay(dayOfTheweek)));
        timeFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.time_fragment_container, timeFragment).commit();

    }


    @Override
    public void onTimeSelected(String time) {

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
}
