package it.polito.mad_lab4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

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
import it.polito.mad_lab4.newData.other.RestaurantPosition;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class MainActivity extends BaseActivity{

    private Button addReview, reservationBtn, testBtn;
    private ArrayList<RestaurantPosition> listaRistoranti;
    //private User userInfo;
    private ImageButton ricercaLuogoBtn, ricercaRistoranteBtn;
    private boolean ricerca_luogo=false, ricerca_ristorante=true;

    private SearchView ricerca;



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
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(), MainActivityManager.class);
                startActivity(i);
            }
        });

        /*
        addReview = (Button) findViewById(R.id.addReview);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddReviewActivity.class);
                i.putExtra("restaurantId", 1);
                startActivity(i);
            }
        });
        */

        // TODO con firebase
        caricaDati();

        ricerca = (SearchView) findViewById(R.id.searchView_main);
        ricerca.setQueryHint(getString(R.string.search_byrestaurant));
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

        ricercaLuogoBtn = (ImageButton) findViewById(R.id.ricerca_luogo);
        ricercaRistoranteBtn = (ImageButton) findViewById(R.id.ricerca_ristorante);

        // TODO ottengo la posizione corrente
        //LatLng myPosition = getCurrentPosition();

        //gestione MAPPA
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mainActivity_map gestoreMappa = new mainActivity_map();
        gestoreMappa.setContext(this);
        //gestoreMappa.setCurrentPosition(myPosition);
        mapFragment.getMapAsync(gestoreMappa);

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
        if (query.length() > 20){
            Toast.makeText(MainActivity.this, getResources().getString(R.string.query_troppo_lunga), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Oggetto_risultatoRicerca> listaRicerca = null;

        if(ricerca_luogo){
            System.out.println("Ricerco per luogo");
            listaRicerca = ricercaPerLuogo(query);
        }
        if(ricerca_ristorante){
            System.out.println("Ricerco per ristorante");
            ricercaPerRistorante(query);
        }


    }

    //implemento ricerca per luogo
    private ArrayList<Oggetto_risultatoRicerca> ricercaPerLuogo(String query) {
        ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();

        /*for(Restaurant r : this.listaRistoranti){
            String address = r.getBasicInfo().getAddress().toLowerCase();
            if (address.contains(query.toLowerCase())){
                Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
                listaRicerca.add(obj);
            }
        }*/

        return listaRicerca;
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

                System.out.println("----> ristoranti trovati: " + listaRicerca.size());
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






       /* for(Restaurant r : this.listaRistoranti){
            String nome = r.getRestaurantName().toLowerCase();
            if (nome.contains(query.toLowerCase())){
                Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
                listaRicerca.add(obj);
            }
        }

        return listaRicerca;*/
    }


    public void ricercaLuogo(View view) {
        if(ricercaLuogoBtn != null && ricercaRistoranteBtn != null) {
            if (!ricerca_luogo) {
                ricercaLuogoBtn.setImageResource(R.drawable.ic_ricerca_luogo_selezione);
                ricerca_luogo = true;
                ricerca.setQueryHint(getString(R.string.search_byplace));

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
}
