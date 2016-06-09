package it.polito.mad_lab4.manager;

/**
 * Created by Giovanna on 11/04/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;

/**
 * Created by Jose on 24/05/15.
 */
public class TimeRangePickerDialog extends DialogFragment implements View.OnClickListener {
    TabHost tabs;
    TextView header;
    Button setTimeRange, closed;
    TimePicker startTimePicker, endTimePicker;
    OnTimeRangeSelectedListener onTimeRangeSelectedListener;
    boolean is24HourMode;
    int id;

    public static TimeRangePickerDialog newInstance(OnTimeRangeSelectedListener callback, boolean is24HourMode, int id) {
        TimeRangePickerDialog ret = new TimeRangePickerDialog();
        ret.initialize(callback, is24HourMode, id);
        return ret;
    }

    public void initialize(OnTimeRangeSelectedListener callback,
                           boolean is24HourMode, int id) {
        onTimeRangeSelectedListener = callback;
        this.is24HourMode = is24HourMode;
        this.id=id;
    }

    public interface OnTimeRangeSelectedListener {
        void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin, int id);
        void closed(int id);
    }

    public void setOnTimeRangeSetListener(OnTimeRangeSelectedListener callback) {
        onTimeRangeSelectedListener = callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.timerange_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        header = (TextView) root.findViewById(R.id.header);
        tabs = (TabHost) root.findViewById(R.id.tabHost);
        closed = (Button) root.findViewById(R.id.closed);
        setTimeRange = (Button) root.findViewById(R.id.bSetTimeRange);
        startTimePicker = (TimePicker) root.findViewById(R.id.startTimePicker);
        endTimePicker = (TimePicker) root.findViewById(R.id.endTimePicker);
        setTimeRange.setOnClickListener(this);
        header.setText(Helper.intToWeekString(getContext(), Helper.fromMyOrderToCalendarWeekOrder(id)));
        closed.setOnClickListener(this);
        tabs.findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec tabpage1 = tabs.newTabSpec("one");
        tabpage1.setContent(R.id.startTimeGroup);

        tabpage1.setIndicator(getResources().getString(R.string.orarioApertura));

        TabHost.TabSpec tabpage2 = tabs.newTabSpec("two");
        tabpage2.setContent(R.id.endTimeGroup);
        tabpage2.setIndicator(getResources().getString(R.string.orarioChiusura));

        tabs.addTab(tabpage1);
        tabs.addTab(tabpage2);

        //TODO starting time con quello dell'orario attuale

        /*GestioneDB DB = new GestioneDB();
        int[] orario = DB.getOrario(getContext(), id);

        if (orario != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startTimePicker.setHour(orario[0]);
                startTimePicker.setMinute(orario[1]);
                endTimePicker.setHour(orario[2]);
                endTimePicker.setMinute(orario[3]);
            }
        }
        */

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null)
            return;
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bSetTimeRange) {
            dismiss();
            int startHour = startTimePicker.getCurrentHour();
            int startMin = startTimePicker.getCurrentMinute();
            int endHour = endTimePicker.getCurrentHour();
            int endMin = endTimePicker.getCurrentMinute();
            onTimeRangeSelectedListener.onTimeRangeSelected(startHour, startMin, endHour, endMin, id);
        }
        if(v.getId() == R.id.closed){
            dismiss();
            onTimeRangeSelectedListener.closed(id);
        }
    }
}