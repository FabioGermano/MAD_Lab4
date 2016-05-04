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
import it.polito.mad_lab3.data.restaurant.BasicInfo;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.RestaurantEntity;
import it.polito.mad_lab3.data.restaurant.Review;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.elaborazioneRicerche.elaborazioneRicerche;
import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.RestaurantActivity;

public class MainActivity extends BaseActivity {

    private Button restaurantBtn, reservationBtn, testBtn;

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
        Restaurant r = new Restaurant(1, "Nome ristorante");
        ArrayList<String> timeTable = new ArrayList<>();
        timeTable.add(0, "10:20 - 14:30");
        timeTable.add(1, "10:30 - 12:30");
        timeTable.add(2, "10:45 - 12:30");
        timeTable.add(3, "10:30 - 12:45");
        timeTable.add(4, "10:30 - 12:30");
        timeTable.add(5, "10:30 - 12:30");
        timeTable.add(6, "10:30 - 12:30");
        BasicInfo bi = new BasicInfo("Via Monginevro, 12", "333-2535312", "restaurant@email.com", "Descrizione locale", null,timeTable);
        ArrayList<Dish> dishes = new ArrayList<Dish>();
        Dish d1 = new Dish("kebab", (float)4.0, 8, (float)2.5, null, null, "Other");
        Dish d2 = new Dish("pizza", (float)3.0, 80, (float)1.5, null, null, "Other");
        Dish d3 = new Dish("panino", (float)2.0, 18, (float)4, null, null, "Other");

        Dish d4 = new Dish("pasta", (float)4.0, 8, (float)2.5, null, null, "Main courses");
        Dish d5 = new Dish("pizza", (float)3.0, 80, (float)1.5, null, null, "Main courses");
        Dish d6 = new Dish("panino", (float)2.0, 18, (float)4, null, null, "Second courses");

        Dish d7 = new Dish("cassata", (float)3.0, 18, (float)3, null, null, "Dessert");

        dishes.add(d1);
        dishes.add(d2);
        dishes.add(d3);
        dishes.add(d4);
        dishes.add(d5);
        dishes.add(d6);
        dishes.add(d7);
        r.setDishes(dishes);

        Offer o1 = new Offer("Offerta1", (float)4.5, 10, (float)8.0, null, null, "details 1");
        Offer o2 = new Offer("Offerta2", (float)2.5, 19, (float)8.0, null, null, "details 2");
        Offer o3 = new Offer("Offerta3", (float)1.5, 15, (float)10.0, null, null, "details 3");
        r.addOffer(o1);
        r.addOffer(o2);
        r.addOffer(o3);

        Review r1 = new Review("userName 1", null, (float)1.7, "2015-05-08", "commento 1 sdkmsdfl fggmdf dgl fgmgdf gd fl");
        Review r2 = new Review("userName 2", null, (float)3.7, "2015-05-10", "commento 2 sdkmsdfl fggmdf dgl fgmgdf gd fl");
        Review r3 = new Review("userName 3", null, (float)2.7, "2015-06-08", "commento 3 sdkmsdfl fggmdf dgl fgmgdffg fgh fg hfh fg h gd fl");
        Review r4 = new Review("userName 4", null, (float)4.7, "2016-05-08", "commento 4 sdkmsdfl fggmdf dgl fgmgdf gd fl");

        r.addReview(r1);
        r.addReview(r2);
        r.addReview(r3);
        r.addReview(r4);
        r.setBasicInfo(bi);

        RestaurantEntity re = new RestaurantEntity();
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        restaurants.add(r);

        re.setRestaurants(restaurants);

        Gson gson = new GsonBuilder().serializeNulls().create();

        String json = gson.toJson(re);
    }

    public void searchRestaurant(View view) {
        Intent i = new Intent(getBaseContext(), elaborazioneRicerche.class);
        //  eseguo qui la ricerca e passo la lista dei risultati da visualizzare,
        // così se ho problemi di qualunque tipo li visualizzo in questa pagina
        startActivity(i);
    }
}
