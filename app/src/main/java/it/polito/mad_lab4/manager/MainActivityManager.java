package it.polito.mad_lab4.manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import it.polito.mad_lab4.*;
import it.polito.mad_lab4.manager.reservation.ReservationsActivity;

public class MainActivityManager extends it.polito.mad_lab4.BaseActivity{

    /*private PhotoViewer photoViewer;
    private Bitmap largeBitmap;*/

    private String name, email;
    private Bitmap logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Call some material design APIs here
        } else {
            // Implement this feature without material design
        }



        setContentView(R.layout.activity_main_manager);
        setToolbarColor();
        setActivityTitle(getResources().getString(R.string.app_name));

        View header = null;

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        checkDB();


        //provaGson();
    }

    /*private void provaGson() {
        GestioneDB db = new GestioneDB();

        ReservationEntity res = db.getAllReservations(getApplicationContext());
    }*/

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }



    public void checkDB(){
        GestioneDB DB = new GestioneDB();

        /*
        DB.deleteDB(this, "db_menu");
        DB.deleteDB(this, "db_offerte");
        DB.deleteDB(this, "db_profilo");
        DB.deleteDB(this, "db_reservation");
        */
        
        DB.creaDB(this);

        name = DB.getRestaurantName(this);
        email = DB.getRestaurantEmail(this);
        logo = DB.getRestaurantLogo(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode == 6709) {
            Toast toast = Toast.makeText(getApplicationContext(), "infatti", Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }

}