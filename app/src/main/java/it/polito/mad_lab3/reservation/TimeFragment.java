package it.polito.mad_lab3.reservation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.ExpandableHeightGridView;
/**
 * Created by Giovanna on 23/04/2016.
 */
public class TimeFragment extends Fragment implements DatesAdapter.AdapterInterface {

    OnTimeSelectedListener mCallback;

    private ExpandableHeightGridView gridView;
    private DatesAdapter datesAdapter;
    private ArrayList<String> timeRangeSplit;
    private int time_position=-1;



    public interface OnTimeSelectedListener {

        void onTimeSelected(String time);

    }


    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            time_position=-1;
        }
        else
        {
            time_position= savedInstanceState.getInt("time_selection");
        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTimeSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTimeSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.time_fragment, container, false);

        //get the time range of the chosen day  from the arguments
        String tmp = getArguments().getString("timeRange", null);
        Boolean isToday = getArguments().getBoolean("isToday", false);

        TextView closed = (TextView) rootView.findViewById(R.id.closed);
        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.gridView);

        //extract slots from time range
        timeRangeSplit= splitTimeRange(tmp, isToday );

        if(timeRangeSplit.isEmpty()) {
            closed.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
        }
        else{
            gridView.setVisibility(View.VISIBLE);
            closed.setVisibility(View.GONE);
        }


        gridView.setExpanded(true);
        gridView.setSelector(R.drawable.selected_background);
        datesAdapter= new DatesAdapter(getContext(), timeRangeSplit, gridView, this);
        gridView.setAdapter(datesAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        gridView.requestFocus(View.FOCUS_FORWARD);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        if(time_position!=-1){
            gridView.setItemChecked(time_position, true);
        }
    }

    @Override
    public void timeSelected() {
        mCallback.onTimeSelected(timeRangeSplit.get(gridView.getCheckedItemPosition()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        time_position= gridView.getCheckedItemPosition();
        outState.putInt("time_selection",time_position  );

    }


    /**
     * Given a time range in the format eg 10:30-15:45 splits in half hour slots
     * @param tmp : time range
     * @return an array of string representing a time
     */
    private ArrayList<String> splitTimeRange(String tmp, boolean isToday){

        ArrayList<String> time = new ArrayList<String>();

        if(tmp == "" || tmp.toLowerCase().equals("closed")){
            return time;
        }

        DateFormat df = new SimpleDateFormat("HH:mm");

        int[] orario = new int[4];
        int n1  = tmp.indexOf(":");
        int n2 = tmp.indexOf(" - ");
        int n3 = tmp.indexOf(":", n2);

        orario[0] = Integer.parseInt(tmp.substring(0, n1)); // opening hour
        orario[1] = Integer.parseInt(tmp.substring(n1+1, n2)); // opening minutes
        orario[2] = Integer.parseInt(tmp.substring(n2+3, n3)); //closing hour
        orario[3] = Integer.parseInt(tmp.substring(n3+1, tmp.length())); //closing minutes

        //store in a calendar object the closing time
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, orario[2]);
        end.set(Calendar.MINUTE, orario[3]);
        end.set(Calendar.SECOND, 0);

        if(isToday) {

            Calendar now = Calendar.getInstance();
            int nowHour = now.get(Calendar.HOUR_OF_DAY);
            int nowMinutes = now.get(Calendar.MINUTE);

            if (nowHour > end.get(Calendar.HOUR_OF_DAY)) //restaurant already closed
                return time;
            if(nowHour == end.get(Calendar.HOUR_OF_DAY) && nowMinutes > end.get(Calendar.MINUTE))
                return time;

            if (nowHour == orario[0] && nowMinutes > orario[1]) {
                //if the hour is the same as opening but the minutes are greater
                //show from nowMinutes on
                orario[1] = nowMinutes;
            }
            //if after the opening hour but before the closure
            if (nowHour > orario[0] && nowHour <= end.get(Calendar.HOUR_OF_DAY)) {
                orario[0] = nowHour;
                orario[1] = nowMinutes;
            }
            if(nowHour == end.get(Calendar.HOUR_OF_DAY) && nowMinutes < end.get(Calendar.MINUTE)){
                orario[0] = nowHour;
                orario[1] = nowMinutes;}
        }
        // split normally
        if (orario[1] > 30) //eg 11:45-14:00
        {
            Calendar start = Calendar.getInstance();
            //start from the nearest hour
            start.set(Calendar.HOUR_OF_DAY, orario[0] + 1);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while (start.before(end)) {
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }


        if (orario[1] < 30 && orario[1] != 0) //eg 11:20-14:00
        {
            Calendar start = Calendar.getInstance();
            //start from the next half hour
            start.set(Calendar.HOUR_OF_DAY, orario[0]);
            start.set(Calendar.MINUTE, 30);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while (start.before(end)) {
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }

        if (orario[1] == 0) {
            Calendar start = Calendar.getInstance();
            //start from orario[0]
            start.set(Calendar.HOUR_OF_DAY, orario[0]);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while (start.before(end)) {
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }


        if (orario[1] == 30) {
            Calendar start = Calendar.getInstance();
            //start from orario[0]
            start.set(Calendar.HOUR_OF_DAY, orario[0]);
            start.set(Calendar.MINUTE, 30);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while (start.before(end)) {
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }

        return time;
    }
}
