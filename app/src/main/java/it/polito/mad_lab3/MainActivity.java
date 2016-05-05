package it.polito.mad_lab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import it.polito.mad_lab3.bl.UserBL;
import it.polito.mad_lab3.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab3.common.photo_viewer.PhotoViewerListener;
import it.polito.mad_lab3.dal.DB;
import it.polito.mad_lab3.dal.DBManager;
import it.polito.mad_lab3.data.restaurant.BasicInfo;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.RestaurantEntity;
import it.polito.mad_lab3.data.restaurant.Review;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.elaborazioneRicerche.Oggetto_risultatoRicerca;
import it.polito.mad_lab3.elaborazioneRicerche.elaborazioneRicerche;
import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.RestaurantActivity;

public class MainActivity extends BaseActivity {

    private Button restaurantBtn, reservationBtn, testBtn;
    private RestaurantEntity re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackButtonVisibility(false);
        setIconaToolbar(true);

        setContentView(R.layout.activity_main);

        setActivityTitle(getResources().getString(R.string.titolo_main_activity));

        restaurantBtn = (Button) findViewById(R.id.restaurantBtn);
        reservationBtn = (Button) findViewById(R.id.reservationBtn);

        restaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), RestaurantActivity.class);
                startActivity(i);
            }
        });

        reservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ReservationActivity.class);
                startActivity(i);
            }
        });

        // carico info dal server: quelle  necessarie per la ricerca, possiamo poi implementare una ricerca direttamente
        // sul server che ci restituisce la lista dei risultati con informazioni riassuntive per visualizzare
        // la lista dei locali cercati
        caricaDati();
    }

    @Override
    protected void ModificaProfilo() {
        Toast toast = Toast.makeText(getApplicationContext(), "Modifica profilo", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void ShowPrenotazioni() {
        Intent i = new Intent(getBaseContext(), ReservationActivity.class);
        startActivity(i);
    }

    private void caricaDati() {

        Gson gson = new GsonBuilder().serializeNulls().create();

        String restaurants = DBManager.readJSON(this, DB.Restaurants);

        re = gson.fromJson(restaurants, RestaurantEntity.class);

        /*String json = gson.toJson(re);
        System.out.println(re.getRestaurants().size() + " \n" +json);*/
    }

    public void searchRestaurant(View view) {
        ArrayList<Restaurant> listaRistoranti = re.getRestaurants();


        ArrayList<Oggetto_risultatoRicerca> listaRicerca = new ArrayList<>();
        for(Restaurant r : listaRistoranti){
            Oggetto_risultatoRicerca obj = new Oggetto_risultatoRicerca(r.getRestaurantId(), r.getRestaurantName(), r.getBasicInfo().getAddress(), r.getBasicInfo().getLogoThumb(), r.getAvgPrice(), r.getAvgReview(), Oggetto_risultatoRicerca.type.RISTORANTE);
            listaRicerca.add(obj);
        }

        System.out.println("passo la lista di elementi " + listaRicerca.size());
        Bundle b = new Bundle();
        b.putSerializable("results", listaRicerca);

        Intent i = new Intent(getBaseContext(), elaborazioneRicerche.class);
        i.putExtras(b);
        startActivity(i);
    }
}
