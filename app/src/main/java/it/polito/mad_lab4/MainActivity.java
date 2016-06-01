package it.polito.mad_lab4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_offerteVicine;
import it.polito.mad_lab4.elaborazioneRicerche.Oggetto_risultatoRicerca;
import it.polito.mad_lab4.elaborazioneRicerche.RecyclerAdapter_offerteVicine;
import it.polito.mad_lab4.elaborazioneRicerche.elaborazioneRicerche;
import it.polito.mad_lab4.manager.MainActivityManager;
import it.polito.mad_lab4.reservation.user_history.ReservationsHistoryActivity;
import it.polito.mad_lab4.user.EditUserProfileActivity;

public class MainActivity extends BaseActivity {

    private Button addReview, reservationBtn, testBtn;
    private ArrayList<Restaurant> listaRistoranti;
    private User userInfo;
    private ImageButton ricercaLuogoBtn, ricercaRistoranteBtn;
    private boolean ricerca_luogo=false, ricerca_ristorante=true;

    private SearchView ricerca;

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

        // carico info dal server: quelle  necessarie per la ricerca, possiamo poi implementare una ricerca direttamente
        // sul server che ci restituisce la lista dei risultati con informazioni riassuntive per visualizzare
        // la lista dei locali cercati
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
        //ricercaPiattoBtn = (ImageButton) findViewById(R.id.ricerca_piatto);

        setUpRecyclerView();

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



    private void caricaDati() {

        lista_offerte_vicine = new ArrayList<>();

        //f.germano mod. Ci sono appositi metodi del bl:
        this.listaRistoranti = RestaurantBL.getAllRestaurants(getApplicationContext());

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
            listaRicerca = ricercaPerRistorante(query);
        }

        if(listaRicerca == null ){
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(listaRicerca.size() == 0){
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        Bundle b = new Bundle();
        b.putSerializable("results", listaRicerca);
        b.putSerializable("userInfo", userInfo);

        Intent i = new Intent(getApplicationContext(), elaborazioneRicerche.class);
        i.putExtras(b);
        startActivity(i);
    }

    //implemento ricerca per luogo
    private ArrayList<Oggetto_risultatoRicerca> ricercaPerLuogo(String query) {
        ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();

        for(Restaurant r : this.listaRistoranti){
            String address = r.getBasicInfo().getAddress().toLowerCase();
            if (address.contains(query.toLowerCase())){
                Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
                listaRicerca.add(obj);
            }
        }

        return listaRicerca;
    }

    //implemento ricerca per ristorante
    private ArrayList<Oggetto_risultatoRicerca> ricercaPerRistorante(String query) {
        ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();

        for(Restaurant r : this.listaRistoranti){
            String nome = r.getRestaurantName().toLowerCase();
            if (nome.contains(query.toLowerCase())){
                Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
                listaRicerca.add(obj);
            }
        }

        return listaRicerca;
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

    public void ricercaLuogo(View view) {
        if(ricercaLuogoBtn != null && ricercaRistoranteBtn != null) {
            if (!ricerca_luogo) {
                ricercaLuogoBtn.setImageResource(R.drawable.ic_ricerca_luogo_selezione);
                ricerca_luogo = true;
                ricerca.setQueryHint(getString(R.string.search_byplace));
                /*if (ricerca_piatto) {
                    ricerca_piatto = false;
                    ricercaPiattoBtn.setImageResource(R.drawable.ic_ricerca_luogo);
                }*/
                if (ricerca_ristorante) {
                    ricerca_ristorante = false;
                    ricercaRistoranteBtn.setImageResource(R.drawable.ic_ricerca_ristorante);
                }
            }/* else {
                ricerca_luogo = false;
                ricercaLuogoBtn.setImageResource(R.drawable.ic_ricerca_luogo);
            }*/
        }
    }

    public void ricercaRistorante(View view) {
        if(ricercaLuogoBtn != null && ricercaRistoranteBtn != null) {
            if (!ricerca_ristorante) {
                ricercaRistoranteBtn.setImageResource(R.drawable.ic_ricerca_ristorante_selezione);
                ricerca_ristorante = true;
                ricerca.setQueryHint(getString(R.string.search_byrestaurant));
                /*if (ricerca_piatto) {
                    ricerca_piatto = false;
                    ricercaPiattoBtn.setImageResource(R.drawable.ic_ricerca_luogo);
                }*/
                if (ricerca_luogo) {
                    ricerca_luogo = false;
                    ricercaLuogoBtn.setImageResource(R.drawable.ic_ricerca_luogo);
                }
            } /*else {
                ricerca_ristorante = false;
                ricercaRistoranteBtn.setImageResource(R.drawable.ic_ricerca_luogo);
            }*/
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
