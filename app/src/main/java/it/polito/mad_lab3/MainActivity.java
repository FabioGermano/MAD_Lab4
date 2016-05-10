package it.polito.mad_lab3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.elaborazioneRicerche.Oggetto_offerteVicine;
import it.polito.mad_lab3.elaborazioneRicerche.Oggetto_risultatoRicerca;
import it.polito.mad_lab3.elaborazioneRicerche.RecyclerAdapter_offerteVicine;
import it.polito.mad_lab3.elaborazioneRicerche.RecyclerAdapter_risultatoRicerca;
import it.polito.mad_lab3.elaborazioneRicerche.elaborazioneRicerche;
import it.polito.mad_lab3.restaurant.reviews.add_review.AddReviewActivity;

public class MainActivity extends BaseActivity {

    private Button addReview, reservationBtn, testBtn;
    private ArrayList<Restaurant> listaRistoranti;
    private User userInfo;

    private ArrayList<Oggetto_offerteVicine> lista_offerte_vicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(false);
        setIconaToolbar(true);

        //gestione login e dati utente
        //controllo file di configurazione per login automatico (così non deve farlo l'utente ad ogni avvio)
        try {
            //ottengo i dati utente dalle altre activity

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                userInfo = (User) extras.getSerializable("userInfo");
                extras.clear();

            } else {
                userInfo = controlloLoginAutomatico();

            }
        }catch(Exception e){

            userInfo = new User(null, null, -1);
        }

        setContentView(R.layout.activity_main);
        hideToolbar(true);
        hideToolbarShadow(true);


        setActivityTitle(getResources().getString(R.string.titolo_main_activity));

        addReview = (Button) findViewById(R.id.addReview);
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), AddReviewActivity.class);
                i.putExtra("restaurantId", 1);
                startActivity(i);
            }
        });

        // carico info dal server: quelle  necessarie per la ricerca, possiamo poi implementare una ricerca direttamente
        // sul server che ci restituisce la lista dei risultati con informazioni riassuntive per visualizzare
        // la lista dei locali cercati
        caricaDati();

        final SearchView ricerca = (SearchView) findViewById(R.id.searchView_main);
        ricerca.setQueryHint("Restaurant Search");
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

        //ricerca.setIconifiedByDefault(false);

        setUpRecyclerView();

    }

    @Override
    protected boolean controlloLogin() {
        if(userInfo != null)
            return this.userInfo.getUserLoginInfo().isLogin();
        else{
            userInfo = new User(null, null , -1);
            return false;
        }
    }

    private User controlloLoginAutomatico(){
        //controllo file di configurazione ed eventualmente eseguo login e carico le info dell'utente nella struttura dati
        User user = null;
        try{

            return new User(null, null, -1);
        }catch(Exception e) {
            return new User(null, null, -1);
        }
    }

    @Override
    protected void filterButton() {

    }

    @Override
    protected void ModificaProfilo() {
        Toast toast = Toast.makeText(getApplicationContext(), "Modifica profilo", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void ShowPrenotazioni() {
    }

    private void caricaDati() {

        lista_offerte_vicine = new ArrayList<>();

        //f.germano mod. Ci sono appositi metodi del bl:
        this.listaRistoranti = RestaurantBL.getAllRestaurants(getBaseContext());

        for (Restaurant r: listaRistoranti){
            if (r.getBasicInfo().getDistance() <= 15){
                for (Offer o: r.getOffers()){
                    lista_offerte_vicine.add(new Oggetto_offerteVicine(o, r.getRestaurantId()));
                }
            }
        }
    }

    public void searchRestaurant(String query) {

        if (query.length() < 3){
            Toast.makeText(MainActivity.this, "Search key too short!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (query.length() > 20){
            Toast.makeText(MainActivity.this, "Search key too long!", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();

        for(Restaurant r : this.listaRistoranti){
            String nome = r.getRestaurantName().toLowerCase();
            String address = r.getBasicInfo().getAddress().toLowerCase();
            if (nome.contains(query.toLowerCase())){
                Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
                listaRicerca.add(obj);
            }
            else if (address.contains(query.toLowerCase())){
                Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
                listaRicerca.add(obj);
            }
        }
        if(listaRicerca.size() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), "Nessun Ristorante Trovato", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Bundle b = new Bundle();
        b.putSerializable("results", listaRicerca);
        b.putSerializable("userInfo", userInfo);

        Intent i = new Intent(getBaseContext(), elaborazioneRicerche.class);
        i.putExtras(b);
        startActivity(i);
    }

    private void setUpRecyclerView(){
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_nearOffers);

        RecyclerAdapter_offerteVicine myAdapter = new RecyclerAdapter_offerteVicine(this, lista_offerte_vicine);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.HORIZONTAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
