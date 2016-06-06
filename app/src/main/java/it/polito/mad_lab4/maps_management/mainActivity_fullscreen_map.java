package it.polito.mad_lab4.maps_management;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_offerteVicine;

/**
 * Created by Euge on 06/06/2016.
 */
public class mainActivity_fullscreen_map extends FragmentActivity {

    private ArrayList<Oggetto_offerteVicine> lista_offerte_vicine = null;
    private LatLng myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map_fullscreen);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lista_offerte_vicine = (ArrayList<Oggetto_offerteVicine> ) extras.getSerializable("listaOfferte");
            double lat = extras.getDouble("myLat") ;
            double lon = extras.getDouble("myLong");
            if(lat != 0.0 && lon != 0.0){
                myPosition = new LatLng(lat, lon);
            }
            extras.clear();
            if(lista_offerte_vicine == null){
                ///SEI NELLA MERDA!!!
                stampaMessaggioErrore();
            }

        } else {
            ///SEI NELLA MERDA!!!
            stampaMessaggioErrore();
        }

        // ottengo la lista già caricata dall'activity principale

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mainActivity_map gestoreMappa = new mainActivity_map();
        if(myPosition != null)
            gestoreMappa.setCurrentPosition(myPosition);
        gestoreMappa.setContext(this);
        gestoreMappa.setFullScreen(true);
        gestoreMappa.setListaOfferte(lista_offerte_vicine);
        mapFragment.getMapAsync(gestoreMappa);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void stampaMessaggioErrore(){
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT);
        toast.show();

        finish();

    }
}
