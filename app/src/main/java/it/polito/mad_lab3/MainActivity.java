package it.polito.mad_lab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.elaborazioneRicerche.Oggetto_risultatoRicerca;
import it.polito.mad_lab3.elaborazioneRicerche.elaborazioneRicerche;
import it.polito.mad_lab3.restaurant.reviews.add_review.AddReviewActivity;

public class MainActivity extends BaseActivity {

    private Button addReview, reservationBtn, testBtn;
    private ArrayList<Restaurant> listaRistoranti;
    private User userInfo;

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
    }

    @Override
    protected boolean controlloLogin() {
        return this.userInfo.getUserLoginInfo().isLogin();
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

       // Gson gson = new GsonBuilder().serializeNulls().create();
        //String restaurants = DBManager.readJSON(this, DB.Restaurants);
        //re = gson.fromJson(restaurants, RestaurantEntity.class);
        /*String json = gson.toJson(re);
        System.out.println(re.getRestaurants().size() + " \n" +json);*/

        //f.germano mod. Ci sono appositi metodi del bl:
        this.listaRistoranti = RestaurantBL.getAllRestaurants(getBaseContext());
    }

    public void searchRestaurant(View view) {
        ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();
        for(Restaurant r : this.listaRistoranti){
            Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE, r.getBasicInfo().getTypesOfServices());
            listaRicerca.add(obj);
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
}
