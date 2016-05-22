package it.polito.mad_lab4.manager;

        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import it.polito.mad_lab4.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class TimeRangeSelecterActivityFragment extends Fragment implements TimeRangePickerDialog.OnTimeRangeSelectedListener {
    Button monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";


    public TimeRangeSelecterActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_range_selecter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        monday = (Button) getActivity().findViewById(R.id.monday_butt);
        tuesday = (Button) getActivity().findViewById(R.id.tuesday_butt);
        wednesday = (Button) getActivity().findViewById(R.id.wednesday_butt);
        thursday = (Button) getActivity().findViewById(R.id.thursday_butt);
        friday = (Button) getActivity().findViewById(R.id.friday_butt);
        saturday = (Button) getActivity().findViewById(R.id.saturday_butt);
        sunday = (Button) getActivity().findViewById(R.id.sunday_butt);

        if (savedInstanceState != null) {
            TimeRangePickerDialog tpd = (TimeRangePickerDialog) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(TIMERANGEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeRangeSetListener(this);
            }
        }
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 0);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 1);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 2);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 3);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 4);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 5);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                        TimeRangeSelecterActivityFragment.this, false, 6);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
            }
        });

    }


    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin, int id) {

        String start="", end = "";
        if (startHour < 10){
            start += "0"+ String.valueOf(startHour);
        }
        else{
            start += String.valueOf(startHour);
        }

        if (startMin < 10){
            start += ":0"+ String.valueOf(startMin);
        }
        else{
            start += ":" + String.valueOf(startMin);
        }

        if (endHour < 10){
            end += "0"+ String.valueOf(endHour);
        }
        else{
            end += String.valueOf(endHour);
        }

        if (endMin < 10){
            end += ":0"+ String.valueOf(endMin);
        }
        else{
            end += ":" + String.valueOf(endMin);
        }

        //###################################################################
        //######    aggiungere caso in cui fine posteriore a inizio   #######
        //###################################################################

        //String str = startHour+":"+ startMin+" - "+endHour+":"+endMin;
        String str = start + " - " + end;
        if((startHour == endHour && endMin<startMin) || endHour<startHour) //da testare
            str = "";
        setText(str, id);
    }

    @Override
    public void closed(int id) {
        String str = getResources().getString(R.string.closed);
        setText(str, id);
    }

    void setText(String string, int id){
        if(string.compareTo("") != 0) {
            switch (id) {
                case 0:
                    monday.setText(getResources().getString(R.string.monday).toUpperCase() + "\n" + string);
                    //monday_text.setText(string);
                    break;
                case 1:
                    tuesday.setText(getResources().getString(R.string.tuesday).toUpperCase() + "\n" + string);
                    //tuesday_text.setText(string);
                    break;
                case 2:
                    wednesday.setText(getResources().getString(R.string.wednesday).toUpperCase() + "\n" + string);
                    //wednesday_text.setText(string);
                    break;
                case 3:
                    thursday.setText(getResources().getString(R.string.thursday).toUpperCase() + "\n" + string);
                    //thursday_text.setText(string);
                    break;
                case 4:
                    friday.setText(getResources().getString(R.string.friday).toUpperCase() + "\n" + string);
                    //friday_text.setText(string);
                    break;
                case 5:
                    saturday.setText(getResources().getString(R.string.saturday).toUpperCase() + "\n" + string);
                    //saturday_text.setText(string);
                    break;
                case 6:
                    sunday.setText(getResources().getString(R.string.sunday).toUpperCase() + "\n" + string);
                    //sunday_text.setText(string);
                    break;
            }
        }
    }
}