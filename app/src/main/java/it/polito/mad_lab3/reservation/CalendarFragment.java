package it.polito.mad_lab3.reservation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.Helper;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class CalendarFragment extends Fragment {

    OnDateSelectedListener mCallback;

    SharedPreferences sp;
    private Button btt;
    private ListView listView;
    private int selection=0;
    private NumberPicker numberPicker;
    private Context context;
    ArrayList<String> datesInDBFormat = new ArrayList<>();

    public interface OnDateSelectedListener {

        public void onDateSelected( String date);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnDateSelectedListener");
        }
    }


    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context=getContext();
        if(savedInstanceState == null){
            selection=0;
        }
        else
        {
            selection= savedInstanceState.getInt("date_selection");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.calendar_fragment, container, false);
        final Calendar c = Calendar.getInstance();
        int year , month, day , week_day;

        ArrayList<String> dates = new ArrayList<>();


        //TODO gestire i giorni in cui è chiuso, visualizzarlo o no?

        for(int i=0; i<7; i++) {
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            week_day = c.get(Calendar.DAY_OF_WEEK);

            //String for the layout
            String str = Helper.intToWeekString(context, week_day) + " " + day + " " + Helper.intToMonthString(context,month+1);// + " "+ year;
            dates.add(str);
            month++; //to start counting from 1=jan
            //string in the db format with first character representing which day of the week (mon, tue...) is
            String wd = String.valueOf(week_day);
            datesInDBFormat.add(wd+year+"-"+month+"-"+day);
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

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //pass  to the activity the date in the db format + day of the week
                mCallback.onDateSelected(datesInDBFormat.get(newVal));

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
         numberPicker.setValue(selection);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("date_selection", numberPicker.getValue());
    }

}
