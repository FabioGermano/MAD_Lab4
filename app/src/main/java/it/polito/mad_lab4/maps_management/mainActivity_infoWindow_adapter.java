package it.polito.mad_lab4.maps_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_offerteVicine;

/**
 * Created by Euge on 04/06/2016.
 */
public class mainActivity_infoWindow_adapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    private LayoutInflater myInflater;
    private ArrayList<Oggetto_offerteVicine> listaOfferte = null;

    public mainActivity_infoWindow_adapter(Context context, ArrayList<Oggetto_offerteVicine> lista_offerte){
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
        this.listaOfferte = lista_offerte;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = myInflater.inflate(R.layout.activity_main_infowindow_map, null);
        TextView titolo = (TextView) v.findViewById(R.id.titolo_map);

        Oggetto_offerteVicine offerta = getOffeta(marker.getId());
        if(offerta != null){
            if(titolo != null) {
                titolo.setText(offerta.getRestaurantPosition().getRestaurantId() + " "  +offerta.getMarkerAssociato());
            }
        } else {
            if(titolo != null) {
                titolo.setText("NUOVO TITOLO!!!");
            }
        }
        return v;
    }

    private Oggetto_offerteVicine getOffeta(String id){
        for (Oggetto_offerteVicine obj : listaOfferte) {
            if(obj.getMarkerAssociato().compareTo(id) == 0)
                return obj;
        }

        return null;
    }
}
