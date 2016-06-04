package it.polito.mad_lab4.manager;

        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TableRow;
        import android.widget.TextView;

        import it.polito.mad_lab4.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class TimeRangeSelecterActivityFragment extends Fragment implements TimeRangePickerDialog.OnTimeRangeSelectedListener {
    TableRow monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";

    private TextView mon, tue, wed, thu, fri, sat, sun;
    TextView[] tv_array;
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


        mon = (TextView) getActivity().findViewById(R.id.monday);
        tue = (TextView) getActivity().findViewById(R.id.tuesday);
        wed = (TextView) getActivity().findViewById(R.id.wedenesday);
        thu = (TextView) getActivity().findViewById(R.id.thursday);
        fri = (TextView) getActivity().findViewById(R.id.friday);
        sat = (TextView) getActivity().findViewById(R.id.saturday);
        sun = (TextView) getActivity().findViewById(R.id.sunday);

        monday = (TableRow) getActivity().findViewById(R.id.mon_row);
        tuesday = (TableRow) getActivity().findViewById(R.id.tue_row);
        wednesday = (TableRow) getActivity().findViewById(R.id.wed_row);
        thursday = (TableRow) getActivity().findViewById(R.id.thu_row);
        friday = (TableRow) getActivity().findViewById(R.id.fri_row);
        saturday = (TableRow) getActivity().findViewById(R.id.sat_row);
        sunday = (TableRow) getActivity().findViewById(R.id.sun_row);


        TableRow[] array = {monday, tuesday, wednesday, thursday, friday, saturday, sunday};
        tv_array = new TextView[]{mon, tue, wed, thu, fri, sat, sun};

        if (savedInstanceState != null) {
            TimeRangePickerDialog tpd = (TimeRangePickerDialog) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(TIMERANGEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeRangeSetListener(this);
            }
        }

        for(int i =0; i<7;i++){
            final int finalI = i;
            array[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                            TimeRangeSelecterActivityFragment.this, false, finalI);
                    timePickerDialog.show(getActivity().getSupportFragmentManager(), TIMERANGEPICKER_TAG);
                }
            });
        }

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

        tv_array[id].setText(string);

    }
}