package it.polito.mad_lab3.reservation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.ExpandableHeightGridView;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class DatesAdapter extends ArrayAdapter<String> {

    AdapterInterface timeInterface;

    public interface AdapterInterface {
        void timeSelected();
    }

    private ArrayList<String> dates;
    public ExpandableHeightGridView grid;

    public DatesAdapter(Context context, ArrayList<String> dates, ExpandableHeightGridView grid, AdapterInterface timeInterface) {
        super(context, 0, dates);
        this.dates=dates;
        this.grid=grid;
        this.timeInterface=timeInterface;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        String date = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.date_row, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.date);

        tv.setText(date);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.setSelected(true);
                grid.setItemChecked(position, true);
                timeInterface.timeSelected();
            }
        });

        return convertView;
    }
}