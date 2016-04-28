package it.polito.mad_lab3;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Toolbar toolbar;

    private boolean hideToolbar=false, hideShadow=false, save_visibility=false,
            calendar_visibility=false, alert_visibility = true, backbutton_visibility=true;
    private TextView titleTextView, alertCountView;
    String activityTitle =  "Titolo App";
    private View toolbarShadow;
    private boolean useToolbar=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        configureBarraLaterale(view);
        configureToolbar(view);
        super.setContentView(view);

    }
    protected boolean useToolbar(boolean useToolbar) {
        return useToolbar;
    }

    private void configureBarraLaterale(View view) {
        View header = null;
        //inizializzo menu laterale
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, null/*toolbar*/, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

            //controllo se l'utente è collegato e decido quale menu/header visualizzare
            boolean login = controlloLogin();

            if(navigationView != null) {
                if(login){
                    navigationView.inflateMenu(R.menu.activity_drawer_login);
                    navigationView.setNavigationItemSelectedListener(this);
                    header = navigationView.getHeaderView(0);
                } else {
                    navigationView.inflateMenu(R.menu.activity_drawer_no_login);
                    navigationView.setNavigationItemSelectedListener(this);
                    header = navigationView.getHeaderView(0);
                }
            }

            //riempio l'header della barra laterale
            if(header != null && login){
                ImageView user_logo = (ImageView) header.findViewById(R.id.nav_drawer_logo);
                TextView user_name = (TextView) header.findViewById(R.id.nav_drawer_name);

                user_name.setText("Nome dell'utente");
                /*if (restaurant_name != null && restaurant_logo != null) {
                    if (name != null && name.compareTo("") != 0)
                        restaurant_name.setText(name);
                    if (logo != null)
                        restaurant_logo.setImageBitmap(logo);
                }*/
            }
        }
    }

    private void configureToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (useToolbar) {

                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(backbutton_visibility);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
                toolbarShadow = view.findViewById(R.id.shadow);

                if(hideShadow  ){
                        toolbarShadow.setVisibility(View.GONE);

                }
                if(hideToolbar) {
                    //getSupportActionBar().hide();
                    titleTextView.setVisibility(View.GONE);
                    //toolbar.setBackgroundColor(Color.TRANSPARENT);
                    toolbar.setBackgroundResource(R.drawable.shadow);
                }
                // Get access to the custom title view
            }
        }
    }
    protected void setBackButtonVisibility(boolean visible)
    {
        backbutton_visibility=visible;
        getSupportActionBar().setDisplayHomeAsUpEnabled(backbutton_visibility);
    }

    protected void hideToolbarShadow(boolean bool){
        toolbarShadow.setVisibility(View.GONE);

    }

    protected void setActivityTitle(String title){
        if(title!=null)
            titleTextView.setText(title);
    }
    private boolean controlloLogin(){
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mod_profilo) {
            ModificaProfilo();
        } else if (id == R.id.gest_prenotazioni) {
            ShowPrenotazioni();
        } else if (id == R.id.nav_contact) {
            Contattaci();
        } else if (id == R.id.nav_bugs) {
            SegnalaBug();
        } else if(id == R.id.esegui_login) {
            Login();
        } else if(id == R.id.nav_contact_no_login){
            Contattaci();
        } else if(id == R.id.nav_bugs_no_login){
            SegnalaBug();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        Questi metodi, essendo uguali sempre possiamo definirli qui
     */

    private void Contattaci(){
        Toast toast = Toast.makeText(getApplicationContext(), "Contattaci pressed", Toast.LENGTH_SHORT);
        toast.show();
        /*Intent i = new Intent(getBaseContext(), Contattaci.class);
        startActivity(i);*/
    }

    private void SegnalaBug(){
        Toast toast = Toast.makeText(getApplicationContext(), "Bugs pressed", Toast.LENGTH_SHORT);
        toast.show();
        /*Intent i = new Intent(getBaseContext(), SegnalaBug.class);
        startActivity(i);*/
    }

    private void Login(){
        Toast toast = Toast.makeText(getApplicationContext(), "Esegui login", Toast.LENGTH_SHORT);
        toast.show();
        /*Intent i = new Intent(getBaseContext(), EseguiLogin.class);
        startActivity(i);*/
    }

    /******************/

    /*
        Questi invece sono personali per ogni utente e quindi è meglio gestirli nella schermata
        appropriata con tutti i dati corretti
     */
    protected abstract void ModificaProfilo();
    protected abstract void ShowPrenotazioni();
}
