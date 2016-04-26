package it.polito.mad_lab3.reservation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import it.polito.mad_lab3.R;
import ivb.com.materialstepper.stepperFragment;
/**
 * Created by Giovanna on 23/04/2016.
 */
public class CalendarFragment extends stepperFragment {

    SharedPreferences sp;
    private Button btt;
    private ListView listView;
    private GridView gridView;
    private NumberPicker numberPicker;

    @Override
    public boolean onNextButtonHandler() {

        if(gridView.getCheckedItemPosition()==-1) {

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                    getActivity());

            // set title
            alertDialogBuilder.setTitle("Complete all fiealds");
            alertDialogBuilder
                    .setMessage("Pick a date and a time")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            // create alert dialog
            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return false;
        }
            //TODO update toolbar view with current info about reservation
            return true;

        }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.calendar_fragment, container, false);
        final Calendar c = Calendar.getInstance();
        int year , month, day , week_day;

        /* //retrieve datepicker from the rootview
        DatePicker dp = (DatePicker) rootView.findViewById(R.id.datePicker);
        Date today = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        dp.setCalendarViewShown(false);
        dp.setSpinnersShown(true);

        //set minimum date to pick
        dp.setMinDate(today.getTime());

        //add a week and set as maximum date
        calendar.add(Calendar.DATE, 7);
        today.setTime(calendar.getTime().getTime());
        dp.setMaxDate(today.getTime());

        listView = (ListView) rootView.findViewById(R.id.listView);
        DatesAdapter datesAdapter = new DatesAdapter(getContext(), dates);
        listView.setAdapter(datesAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setSelector(R.drawable.selected_background);*/

        ArrayList<String> dates = new ArrayList<>();
        for(int i=0; i<7; i++) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            week_day = c.get(Calendar.DAY_OF_WEEK);
            String str = intToWeekString(week_day) + " " + day + " " + intToMonthString(month);// + " "+ year;
            dates.add(str);
            c.add(Calendar.DATE, 1);
        }
        String[] picker;
        picker = new String[7];

        for(int i =0; i<dates.size();i++){
            picker[i]= dates.get(i);
        }
        numberPicker = (NumberPicker) rootView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0); //from array first value
        //Specify the maximum value/number of NumberPicker
        numberPicker.setMaxValue(dates.size()-1); //to array last value

        //Specify the NumberPicker data source as array elements
        numberPicker.setDisplayedValues(picker);

        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){

                //update time choice view

            }
        });

        /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                listView.setItemChecked(position, true);
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
                if( gridView.getCheckedItemPosition() != -1 )
                    onNextButtonHandler();
            }
        });

            TextView tv;
            tv = new TextView(getContext());
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            tv.setText(str);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextAppearance(android.R.style.TextAppearance_Medium);
            }
            else
                tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);

            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(dpWidthInPx);
            ll.addView(tv);
            */



        ArrayList<String> time = new ArrayList<String>();
        time.add("11:30");
        time.add("12:00");
        time.add("12:30");
        time.add("13:00");
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(new DatesAdapter(getContext(), time));
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
            }
        });


        return rootView;
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

    private String intToMonthString (int month){
        switch (month){
            case 0:
                return getResources().getString(R.string.jenuary);

            case 1:
                return getResources().getString(R.string.february);

            case 2:
                return getResources().getString(R.string.march);

            case 3:
                return getResources().getString(R.string.april);

            case 4:
                return getResources().getString(R.string.may);

            case 5:
                return getResources().getString(R.string.june);

            case 6:
                return getResources().getString(R.string.july);

            case 7:
                return getResources().getString(R.string.ausgust);

            case 8:
                return getResources().getString(R.string.september);

            case 9:
                return getResources().getString(R.string.october);

            case 10:
                return getResources().getString(R.string.november);

            case 11:
                return getResources().getString(R.string.december);
            default:
                return null;
        }
    }
}
