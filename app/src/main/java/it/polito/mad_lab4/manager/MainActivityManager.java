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

import com.google.firebase.auth.FirebaseUser;

import it.polito.mad_lab4.*;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.manager.reservation.ReservationsActivity;

public class MainActivityManager extends it.polito.mad_lab4.BaseActivity{

    /*private PhotoViewer photoViewer;
    private Bitmap largeBitmap;*/

    private String name, email;
    private FirebaseUser currentUser;
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
        setVisibilityAlert(false);
        invalidateOptionsMenu();

        View header = null;

        Button btn = (Button) findViewById(R.id.button);
        btn.setVisibility(View.GONE);


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


    @Override
    protected void onResume() {
        super.onResume();

        new Thread() {
            public void run() {
                FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                firebaseGetAuthInformation.waitForResult();
                currentUser = firebaseGetAuthInformation.getUser();
                if(currentUser == null) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            setVisibilityAlert(false);
                            invalidateOptionsMenu();

                        }
                    });
                }

            }
        }.start();

    }

}