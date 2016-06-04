package it.polito.mad_lab4.maps_management;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Euge on 04/06/2016.
 */
public class mainActivity_map implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private LatLng position= null;
    private Context context;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mainActivity_infoWindow_adapter myAdapter = new mainActivity_infoWindow_adapter(this.context);
        mMap.setInfoWindowAdapter(myAdapter);


        if(position != null) {

            Marker m =mMap.addMarker(new MarkerOptions().position(position).title("TORINO")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.setOnMarkerClickListener(this);
            mMap.setOnInfoWindowClickListener(this);
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
        }
    }

    public void setPosition(double latitudine, double longitudine){
        this.position = new LatLng(latitudine, longitudine);
    }

    public void setContext(Context argC){
        this.context = argC;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("------> PREMUTO MARKER: " + marker.getId()+ " - " + marker.getTitle());

        if(marker.isInfoWindowShown())
            marker.hideInfoWindow();

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("------> PREMUTO INFO_WINDOW: " + marker.getId()+ " - " + marker.getTitle());
    }
}
