package it.polito.mad_lab4.maps_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import it.polito.mad_lab4.R;

/**
 * Created by Euge on 04/06/2016.
 */
public class mainActivity_infoWindow_adapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private LayoutInflater myInflater;

    public mainActivity_infoWindow_adapter(Context context){
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = myInflater.inflate(R.layout.activity_main_infowindow_map, null);
        TextView titolo = (TextView) v.findViewById(R.id.titolo_map);
        titolo.setText("NUOVO TITOLO!!!");
        return v;
    }
}
