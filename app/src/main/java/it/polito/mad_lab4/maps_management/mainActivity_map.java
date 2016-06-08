package it.polito.mad_lab4.maps_management;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_offerteVicine;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferListManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantInfoManager;
import it.polito.mad_lab4.firebase_position.FirebaseGetRestaurantsPositions;
import it.polito.mad_lab4.newData.other.Position;
import it.polito.mad_lab4.newData.other.RestaurantPosition;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Euge on 04/06/2016.
 */
public class mainActivity_map implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;

    private boolean fullScreen = false;
    private boolean caricamentoCompletato = false;
    private ArrayList<Oggetto_offerteVicine>  listaOfferte = null;

    private LatLng myPosition = new LatLng(45.06455, 7.65833); //qui definiamo una posizione di default se per caso non ne abbiamo altre
    private Context context;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(!fullScreen){
            //scarico ed elaboro i dati dal server e poi chiamo settaMarker()
            caricaOfferteDaVisualizzare();
            System.out.println("------> NON SONO IN FULLSCREEN, CARICO DATI DAL SERVER ");
        } else {
            //ho già scaricato ed elaborato i dati dal server ---> chiamo settaMarker()
            System.out.println("------> SONO IN FULLSCREEN");
            if(listaOfferte != null)
                settaMarker();
        }

        if(listaOfferte != null) {
            mainActivity_infoWindow_adapter myAdapter = new mainActivity_infoWindow_adapter(this.context, this.listaOfferte);
            mMap.setInfoWindowAdapter(myAdapter);
        }

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        if(!fullScreen)
            mMap.setOnMapClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));

    }

    private void settaMarker(){
        LatLng posizioneOfferta;
        Marker marker;

        mMap.addMarker(new MarkerOptions().position(myPosition)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        for (Oggetto_offerteVicine obj:listaOfferte) {
            posizioneOfferta = new LatLng( obj.getRestaurantPosition().getPosition().getLatitudine(), obj.getRestaurantPosition().getPosition().getLongitudine());
            if(obj.isNew()){
                //metto l'icona con il punto esclamativo
                marker =mMap.addMarker(new MarkerOptions().position(posizioneOfferta)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                obj.setMarkerAssociato(marker.getId());
            } else {
                //metto l'icona semplice
                marker =mMap.addMarker(new MarkerOptions().position(posizioneOfferta)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                obj.setMarkerAssociato(marker.getId());
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("------> PREMUTO MARKER: " + marker.getId()+ " - " + marker.getTitle());

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        System.out.println("------> PREMUTO INFO_WINDOW: " + marker.getId()+ " - " + marker.getTitle());
        for (Oggetto_offerteVicine obj: listaOfferte){
            if (obj.getMarkerAssociato().equals(marker.getId())){
                Intent i = new Intent(context, RestaurantActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("restaurantId", obj.getRestaurantPosition().getRestaurantId());
                i.putExtras(b);
                context.startActivity(i);
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println("------> PREMUTO SULLA MAPPA: "+ "\n-lat: " + latLng.latitude + " -long: " + latLng.longitude);

        if(!caricamentoCompletato){
            Toast toast = Toast.makeText(context, "Loading ...", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } else {
            if(listaOfferte == null){
                Toast toast = Toast.makeText(context, "Errore caricamento offerte vicine ...", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            // carico la nuova activity a cui passo tutte le info già create da visualizzare
            Intent i = new Intent(context, mainActivity_fullscreen_map.class);
            Bundle b = new Bundle();
            b.putSerializable("listaOfferte", listaOfferte);
            b.putDouble("myLat", myPosition.latitude);
            b.putDouble("myLong", myPosition.longitude);
            i.putExtras(b);
            context.startActivity(i);
        }
    }

    private void caricaOfferteDaVisualizzare(){
        new Thread()        {
            public void run() {

                FirebaseGetRestaurantsPositions restaurantsPositions = new FirebaseGetRestaurantsPositions();
                restaurantsPositions.waitForResult();
                ArrayList<RestaurantPosition> listaPosizioniR = restaurantsPositions.getListaOfferte();

                listaOfferte = new ArrayList<Oggetto_offerteVicine>();

                if(listaOfferte != null){
                    Oggetto_offerteVicine objOffertaVicina;
                    for (RestaurantPosition rp: listaPosizioniR) {

                        if(ristoranteVicino(rp.getPosition())){
                            objOffertaVicina = new Oggetto_offerteVicine();
                            objOffertaVicina.setRestaurantPosition(rp);
                            listaOfferte.add(objOffertaVicina);
                        }
                    }

                    // ora ho tutti i ristoranti distanti TOT metri dalla mia posizione
                    FirebaseGetOfferListManager offerListManager;
                    FirebaseGetRestaurantInfoManager restaurantInfoManager;

                    // Funzione corretta che scarica per ogni ristorante le offerte e prende la prima
                    // di ogniuno (si può cambiare)

                    for (Oggetto_offerteVicine obj: listaOfferte) {
                        offerListManager = new FirebaseGetOfferListManager();
                        offerListManager.getOffers(obj.getRestaurantPosition().getRestaurantId());
                        offerListManager.waitForResult();
                        ArrayList<Offer> listaOTemp = offerListManager.getResult();
                        obj.setNumOfferte(listaOTemp.size());

                        restaurantInfoManager = new FirebaseGetRestaurantInfoManager();
                        restaurantInfoManager.getRestaurantInfo(obj.getRestaurantPosition().getRestaurantId(), "restaurantName");
                        restaurantInfoManager.waitForResult();
                        obj.setNomeRistorante(restaurantInfoManager.getResult());
                    }

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            // UI code goes here
                            settaMarker();
                        }
                    });
                }

                caricamentoCompletato = true;

            }
        }.start();
    }

    private boolean ristoranteVicino(Position restaurantPosition) {

        float[] distance = new float[1];
        Location.distanceBetween(myPosition.latitude, myPosition.longitude, restaurantPosition.getLatitudine(), restaurantPosition.getLongitudine(), distance);
        // distance[0] is now the distance between these lat/lons in meters
        System.out.println("-----> Distanza: " + distance[0]);
        if (distance[0] < 700.0) {
            return true;
        } else
            return false;
    }



    public void setCurrentPosition(LatLng currentPosition){
        if (currentPosition != null)
            this.myPosition = currentPosition;
    }

    public void setContext(Context argC){
        this.context = argC;
    }

    public void setFullScreen(boolean flag){
        this.fullScreen = flag;
    }

    public void setListaOfferte(ArrayList<Oggetto_offerteVicine>  obj){
        this.listaOfferte = obj;
    }

    public void updatePosition(){
        if(!fullScreen){
            mMap.clear();
            //scarico ed elaboro i dati dal server e poi chiamo settaMarker()
            caricaOfferteDaVisualizzare();
            System.out.println("------> NUOVA POSIZIONE, CARICO DATI DAL SERVER ");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15));

        }
    }
}
