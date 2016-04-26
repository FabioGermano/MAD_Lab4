package it.polito.mad_lab3.reservation;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import it.polito.mad_lab3.R;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class ChooseTimeAdapter extends BaseAdapter {

    private Context mContext;
    private String[] times;

    // Constructor
    public ChooseTimeAdapter(Context c, String[] times) {
        this.mContext = c;
        this.times = times;
    }
    @Override
    public int getCount() {
        return times.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Button time_button;
        if (convertView == null) {
            time_button = new Button(mContext);
            time_button.setText(times[position]);
        }
        else
        {
            time_button = (Button) convertView;
        }
        time_button.setId(position);
        time_button.setHighlightColor(Color.GREEN);
        time_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "" + time_button.getId(),
                        Toast.LENGTH_SHORT).show();
                time_button.setPressed(true);
                time_button.setBackgroundColor(Color.BLUE);
            }
        });
        return time_button;
    }
}
