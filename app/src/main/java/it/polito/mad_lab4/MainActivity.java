package it.polito.mad_lab4;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_offerteVicine;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_risultatoRicerca;
import it.polito.mad_lab4.elaborazioneRicerche.elaborazioneRicerche;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferListManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantInfoManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantProfileManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUserInfoManager;
import it.polito.mad_lab4.firebase_position.FirebaseGetRestaurantsPositions;
import it.polito.mad_lab4.manager.MainActivityManager;
import it.polito.mad_lab4.maps_management.mainActivity_map;
import it.polito.mad_lab4.newData.other.Position;
import it.polito.mad_lab4.newData.other.RestaurantPosition;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class MainActivity extends BaseActivity implements LocationListener{

    private Button addReview, reservationBtn, testBtn;
    private ArrayList<RestaurantPosition> listaRistoranti;
    //private User userInfo;
    private ImageButton ricercaLuogoBtn, ricercaRistoranteBtn;
    private boolean ricerca_luogo=false, ricerca_ristorante=true;

    private SearchView ricerca, number, city;
    private CardView numberCard, cityCard;

    private Context context = null;

    private String searchCity = "Torino";

    private boolean netAllowed = false;
    private boolean wifiAllowed = false;
    private boolean locAllowed = false;

    private mainActivity_map gestoreMappa;
    private LocationManager locationManager;
    private LatLng myPosition = null;
    private LatLng foundedPosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(false);
        setIconaToolbar(true);

        setHomePageClient();

        setContentView(R.layout.activity_main);

        hideToolbar(true);
        hideToolbarShadow(true);

        setActivityTitle(getResources().getString(R.string.titolo_main_activity));

        inizializzaSearchView();
        myPosition = setPosition();



        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(), MainActivityManager.class);
                startActivity(i);
            }
        });


        context = this;

        //prova acquisizione posizione
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Register the listener with the Location Manager to receive location updates

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
           checkNetPermission();
        else {
            netAllowed = true;
            wifiAllowed = true;
            locAllowed = true;
        }

        if (netAllowed && wifiAllowed && locAllowed){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }


        caricaDati();

        ricercaLuogoBtn = (ImageButton) findViewById(R.id.ricerca_luogo);
        ricercaRistoranteBtn = (ImageButton) findViewById(R.id.ricerca_ristorante);


        //gestione MAPPA
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        gestoreMappa = new mainActivity_map();
        gestoreMappa.setContext(this);
        gestoreMappa.setCurrentPosition(myPosition);
        mapFragment.getMapAsync(gestoreMappa);

    }

    private LatLng setPosition(){
        LatLng pos;

        //se loggato setta come default l'ateneo
        pos = new LatLng(45.06455, 7.65833);


        //se non loggato setta come default Torino
        return pos;
    }

    private void checkNetPermission(){
        int net = ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.ACCESS_NETWORK_STATE);
        int wifi = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE);
        int loc = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (net != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NETWORK_STATE} , 10);
        else
            netAllowed = true;

        if (wifi != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_WIFI_STATE}, 11);
        else
            wifiAllowed = true;
        if (loc != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION }, 12);
        else
            locAllowed = true;
    }


    private void inizializzaSearchView(){
        ricerca = (SearchView) findViewById(R.id.searchView_main);
       // number = (SearchView) findViewById(R.id.searchView_number);
        //city = (SearchView) findViewById(R.id.searchView_city);

        //numberCard = (CardView) findViewById(R.id.searchNumber_card);
        //cityCard = (CardView) findViewById(R.id.searchCity_card);

        ricerca.setQueryHint(getString(R.string.search_byrestaurant));
        //number.setQueryHint("number");
        //city.setQueryHint("City");

        ricerca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRestaurant(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        ricerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.searchView_main:
                        ricerca.onActionViewExpanded();
                        break;
                }
            }
        });
/*
        number.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ricercaPerLuogo(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.searchView_number:
                        number.onActionViewExpanded();
                        break;
                }
            }
        });
        city.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ricercaPerLuogo(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.searchView_city:
                        city.onActionViewExpanded();
                        break;
                }
            }
        });*/
    }

    private void caricaDati() {

        new Thread()        {
            public void run() {

                FirebaseGetRestaurantsPositions restaurantsPositions = new FirebaseGetRestaurantsPositions();
                restaurantsPositions.waitForResult();
                listaRistoranti = restaurantsPositions.getListaOfferte();
            }
        }.start();
    }

    public void searchRestaurant(String query) {

        if (query.length() < 3){
            Toast.makeText(MainActivity.this, getResources().getString(R.string.query_troppo_corta), Toast.LENGTH_SHORT).show();
            return;
        }

        if (query.length() > 50){
            Toast.makeText(MainActivity.this, getResources().getString(R.string.query_troppo_lunga), Toast.LENGTH_SHORT).show();
            return;
        }

        if(ricerca_luogo){
            System.out.println("Ricerco per luogo");
            ricercaPerLuogo(query);
        }
        if(ricerca_ristorante){
            System.out.println("Ricerco per ristorante");
            ricercaPerRistorante(query);
        }


    }

    //implemento ricerca per luogo
    private void ricercaPerLuogo(final String query) {

        new Thread()        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });
                Geocoder geocoder = new Geocoder(context);
                final ArrayList<Address> nearAddress = new ArrayList<Address>();

                try {
                    double radius = 6378137; // meters , earth Radius approx
                    double DEGREES = 180/Math.PI;
                    int offset = 5000; //distanza in metri dalla posizione attuale



                    double latLowerLeft = myPosition.latitude - (DEGREES)*(offset/radius);
                    double longLowerLeft = myPosition.longitude - (DEGREES)*(offset/radius)/ (Math.cos(Math.PI/180.0*myPosition.latitude));
                    double latUpperRight = myPosition.latitude + (DEGREES)*(offset/radius);
                    double longUpperRight = myPosition.longitude + (DEGREES)*(offset/radius)/ (Math.cos(Math.PI/180.0*myPosition.latitude));

                    System.out.println("----> BOUNDS");
                    System.out.println("----> " + latLowerLeft + ", " + longLowerLeft  );
                    System.out.println("----> " + latUpperRight + ", " + longUpperRight  );



                    List<Address> addresses = geocoder.getFromLocationName(query, 20, latLowerLeft, longLowerLeft, latUpperRight, longUpperRight);
                    System.out.println("-----> TROVATI " + addresses.size());
                    System.out.println("-----> COMPARO I RISULTATI CON " + myPosition.latitude + ", " + myPosition.longitude);


                    if (!addresses.isEmpty()){
                        if (addresses.size() > 1){
                            for (Address a : addresses){
                                System.out.println("-----> " + a.getLocality() + ", " + a.getAddressLine(0) + " - " + a.getLatitude() + ", " + a.getLongitude());
                                if (a.getLocality() != null){
                                    System.out.println("-----> AGGIUNTO");
                                    nearAddress.add(a);
                                }

                            }
                            if (nearAddress.isEmpty()){
                                //nessun risultato trovato
                                runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                });
                                return;
                            }
                            if (nearAddress.size() > 1){
                                //popup di scelta
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogPlaces(nearAddress);
                                    }
                                });
                                return;
                            }
                            else {
                                foundedPosition = new LatLng(nearAddress.get(0).getLatitude(), nearAddress.get(0).getLongitude());
                            }
                        }
                        else{
                            System.out.println("-----> " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAddressLine(0) + " - " + addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude());
                            foundedPosition = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                        }
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        return;
                    }

                    //qui avrò foundedPosition riempito correttamente e parto con la vera e propria ricerca



                } catch (IOException e) {
                    e.printStackTrace();
                }

                final ArrayList<Oggetto_risultatoRicerca> risultatoRicerca = find();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();

                        if(risultatoRicerca == null ){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        if(risultatoRicerca.size() == 0){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        Bundle b = new Bundle();
                        b.putSerializable("results", risultatoRicerca);
                        Intent i = new Intent(getApplicationContext(), elaborazioneRicerche.class);
                        i.putExtras(b);
                        startActivity(i);
                    }
                });
            }
        }.start();
    }

    private ArrayList<Oggetto_risultatoRicerca> find (){
        FirebaseGetRestaurantProfileManager restaurantProfileManager;
        final ArrayList<Oggetto_risultatoRicerca> risultatoRicerca = new ArrayList<Oggetto_risultatoRicerca>();

        for (RestaurantPosition restaurant: listaRistoranti){
            //seleziono ristoranti vicino al posto ricercato
            if ( isNear( foundedPosition, new LatLng(restaurant.getPosition().getLatitudine(), restaurant.getPosition().getLongitudine()), 1000) ){
                restaurantProfileManager = new FirebaseGetRestaurantProfileManager();
                restaurantProfileManager.getRestaurant(restaurant.getRestaurantId());
                restaurantProfileManager.waitForResult();

                Restaurant r = restaurantProfileManager.getResult();
                int avgPrice = 10;
                Oggetto_risultatoRicerca res = new Oggetto_risultatoRicerca(restaurant.getRestaurantId(), r.getRestaurantName(), r.getAddress(), r.getLogoThumbDownloadLink(), avgPrice, r.getRanking(), r.isTakeAway(), r.isOnPlace());
                risultatoRicerca.add(res);
            }
        }
        return risultatoRicerca;
    }

    //implemento ricerca per ristorante
    private void ricercaPerRistorante(final String query) {

        new Thread()        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();
                FirebaseGetRestaurantInfoManager restaurantInfoManager;
                FirebaseGetRestaurantProfileManager restaurantProfileManager;

                for (RestaurantPosition obj: listaRistoranti){
                    restaurantInfoManager = new FirebaseGetRestaurantInfoManager();
                    restaurantInfoManager.getRestaurantInfo(obj.getRestaurantId(), "restaurantName");
                    restaurantInfoManager.waitForResult();
                    String nome = restaurantInfoManager.getResult();

                    if (nome.toLowerCase().contains(query.toLowerCase())){
                        restaurantProfileManager = new FirebaseGetRestaurantProfileManager();
                        restaurantProfileManager.getRestaurant(obj.getRestaurantId());
                        restaurantProfileManager.waitForResult();
                        Restaurant r = restaurantProfileManager.getResult();

                        int avgPrice = 10;
                        Oggetto_risultatoRicerca res = new Oggetto_risultatoRicerca(obj.getRestaurantId(), r.getRestaurantName(), r.getAddress(), r.getLogoThumbDownloadLink(), avgPrice, r.getRanking(), r.isTakeAway(), r.isOnPlace());
                        listaRicerca.add(res);
                    }
                }

                final ArrayList<Oggetto_risultatoRicerca> risultato = listaRicerca;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();

                        if(risultato == null ){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }

                        if(risultato.size() == 0){
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
                            toast.show();
                            return;
                        }
                        Bundle b = new Bundle();
                        b.putSerializable("results", risultato);

                        Intent i = new Intent(getApplicationContext(), elaborazioneRicerche.class);
                        i.putExtras(b);
                        startActivity(i);


                    }
                });
            }
        }.start();
    }


    public void ricercaLuogo(View view) {
        if(ricercaLuogoBtn != null && ricercaRistoranteBtn != null) {
            if (!ricerca_luogo) {
                ricercaLuogoBtn.setImageResource(R.drawable.ic_ricerca_luogo_selezione);
                ricerca_luogo = true;
                ricerca.setQueryHint(getString(R.string.search_byplace));


                //numberCard.setVisibility(View.VISIBLE);
                //cityCard.setVisibility(View.VISIBLE);

                if (ricerca_ristorante) {
                    ricerca_ristorante = false;
                    ricercaRistoranteBtn.setImageResource(R.drawable.ic_ricerca_ristorante);
                }
            }
        }
    }

    public void ricercaRistorante(View view) {
        if(ricercaLuogoBtn != null && ricercaRistoranteBtn != null) {
            if (!ricerca_ristorante) {
                ricercaRistoranteBtn.setImageResource(R.drawable.ic_ricerca_ristorante_selezione);
                ricerca_ristorante = true;
                ricerca.setQueryHint(getString(R.string.search_byrestaurant));

                if (ricerca_luogo) {
                    ricerca_luogo = false;
                    //numberCard.setVisibility(View.GONE);
                    //cityCard.setVisibility(View.GONE);
                    ricercaLuogoBtn.setImageResource(R.drawable.ic_ricerca_luogo);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?")
                    .setPositiveButton("NO", null)
                    .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    netAllowed = true;

                } else {
                    netAllowed = false;
                }
                break;
            }
            case 11: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    wifiAllowed = true;
                } else {
                    wifiAllowed = false;
                }
                break;
            }
            case 12: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    locAllowed = true;
                } else {
                    locAllowed = false;
                }
                break;
            }
        }
    }

    //funzioni per la gestione della posizione
    @Override
    public void onLocationChanged(Location location) {
        Geocoder geo = new Geocoder(context);
        try {
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            for(Address a: addresses){
                System.out.println("----> POSIZIONE");
                System.out.println("----> " + a.getCountryName());
                System.out.println("----> " + a.getLocality());
                System.out.println("----> " + a.getAddressLine(0));
            }

            if(!isNear(null, new LatLng(location.getLatitude(), location.getLongitude()), 25)) {
                System.out.println("----> nuova posizione aggiorno la mappa");
                gestoreMappa.setCurrentPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                gestoreMappa.updatePosition();
            } else{
                System.out.println("----> nuova posizione NON aggiorno la mappa");

            }

            myPosition = new LatLng(location.getLatitude(), location.getLongitude());

            System.out.println("ACCURATEZZA: " + location.getAccuracy());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("----> onStatusChanged");

    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("----> onProviderEnabled");

    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("----> onProviderDisabled");

    }

    private boolean isNear(LatLng start, LatLng end, float meters) {
        double lat, lng;

        if(myPosition == null){
            return false;
        }

        if (start == null){
            lat = myPosition.latitude;
            lng = myPosition.longitude;
        }
        else {
            lat = start.latitude;
            lng = start.longitude;
        }


        float[] distance = new float[1];
        Location.distanceBetween(lat, lng, end.latitude, end.longitude, distance);
        // distance[0] is now the distance between these lat/lons in meters
        System.out.println("-----> Distanza: " + distance[0]);
        if (distance[0] < meters) {
            return true;
        } else
            return false;
    }

    @Override
    public void onStart(){
        super.onStart();
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                checkNetPermission();
            else {
                netAllowed = true;
                wifiAllowed = true;
                locAllowed = true;
            }

            if (netAllowed && wifiAllowed && locAllowed) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                checkNetPermission();
            else {
                netAllowed = true;
                wifiAllowed = true;
                locAllowed = true;
            }

            if (netAllowed && wifiAllowed && locAllowed){
                locationManager.removeUpdates(this);
            }
        }
    }

    private void showDialogPlaces(final ArrayList<Address> addresses){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show
        final String[] places = new String[addresses.size()];

        int i = 0;
        for (Address a: addresses){
            places[i] = a.getAddressLine(0) + ", " + a.getLocality();
            i++;
        }

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("SELECT ADDRESS").
                setItems(places, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        foundedPosition = new LatLng(addresses.get(which).getLatitude(), addresses.get(which).getLongitude());
                        System.out.println("---> SCELTO: " + places[which]);
                        dialog.dismiss();

                        new Thread() {
                            public void run() {
                                final ArrayList<Oggetto_risultatoRicerca> risultatoRicerca = find();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissProgressDialog();

                                        if(risultatoRicerca == null ){
                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT);
                                            toast.show();
                                            return;
                                        }
                                        if(risultatoRicerca.size() == 0){
                                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
                                            toast.show();
                                            return;
                                        }

                                        Bundle b = new Bundle();
                                        b.putSerializable("results", risultatoRicerca);
                                        Intent i = new Intent(getApplicationContext(), elaborazioneRicerche.class);
                                        i.putExtras(b);
                                        startActivity(i);
                                    }
                                });
                            }
                        }.start();

                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dismissProgressDialog();
            }
        });

        dialog = builder.create();
        dialog.getListView().setDividerHeight(1);
        dialog.show();
    }

}
