package it.polito.mad_lab3.reservation;

import android.app.Activity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.ExpandableHeightGridView;
import ivb.com.materialstepper.stepperFragment;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class TimeFragment extends Fragment {

    OnTimeSelectedListener mCallback;

    private ExpandableHeightGridView gridView;
    private ArrayList<String> time;

    public interface OnTimeSelectedListener {

        public void onTimeSelected(String time);

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

        //TODO Get the day of the week in order to update the available time slots
        // ...getArguments()...
        String tmp = getArguments().getString("timeRange", null);

        //extract slots from time range
        Log.w("time", tmp);

        int[] orario = new int[4];
        int n1  = tmp.indexOf(":");
        int n2 = tmp.indexOf(" - ");
        int n3 = tmp.indexOf(":", n2);


        time = new ArrayList<String>();

        orario[0] = Integer.parseInt(tmp.substring(0, n1));
        orario[1] = Integer.parseInt(tmp.substring(n1+1, n2));
        orario[2] = Integer.parseInt(tmp.substring(n2+3, n3));
        orario[3] = Integer.parseInt(tmp.substring(n3+1, tmp.length()));

        //store in a calendar object the closing time
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, orario[2]);
        end.set(Calendar.MINUTE, orario[3]);
        end.set(Calendar.SECOND, 0);

        DateFormat df = new SimpleDateFormat("HH:mm");

        if(orario[1]>30) //eg 11:45-14:00
        {
            Calendar start = Calendar.getInstance();
            //start from the nearest hour
            start.set(Calendar.HOUR_OF_DAY, orario[0]+1);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while(start.before(end)){
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }


        if(orario[1]<30 && orario[1]!=0) //eg 11:20-14:00
        {
            Calendar start = Calendar.getInstance();
            //start from the next half hour
            start.set(Calendar.HOUR_OF_DAY, orario[0]);
            start.set(Calendar.MINUTE, 30);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while(start.before(end)){
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }

        if(orario[1]==0){
            Calendar start = Calendar.getInstance();
            //start from orario[0]
            start.set(Calendar.HOUR_OF_DAY, orario[0]);
            start.set(Calendar.MINUTE, 0);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while(start.before(end)){
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }


        if(orario[1]==30){
            Calendar start = Calendar.getInstance();
            //start from orario[0]
            start.set(Calendar.HOUR_OF_DAY, orario[0]);
            start.set(Calendar.MINUTE, 30);
            start.set(Calendar.SECOND, 0);
            time.add(df.format(start.getTime()));
            start.add(Calendar.MINUTE, 30);
            //add time to pick until the closing time
            while(start.before(end)){
                time.add(df.format(start.getTime()));
                start.add(Calendar.MINUTE, 30);
            }
        }



        gridView = (ExpandableHeightGridView) rootView.findViewById(R.id.gridView);
        gridView.setExpanded(true);
        gridView.setAdapter(new DatesAdapter(getContext(), time));
        gridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                mCallback.onTimeSelected(time.get(position));
            }
        });

        return rootView;
    }
}
