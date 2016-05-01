package it.polito.mad_lab3;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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

    //per visualizzare o meno, e abilitare, l'icona nella toolbar
    private boolean icona_toolbar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        configureToolbar(view);
        super.setContentView(view);

        configureBarraLaterale(view);

    }

    protected void hideToolbar(boolean bool){
        hideToolbar = bool;
        titleTextView.setVisibility(View.GONE);
        //toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setBackgroundResource(R.drawable.shadow);

    }

    protected boolean useToolbar(boolean useToolbar) {
        return useToolbar;
    }

    protected  void setIconaToolbar(boolean flag){
        this.icona_toolbar = flag;
    }

    protected void configureBarraLaterale(View view) {
        System.out.println("Configuro barra laterale");
        View header = null;
        //inizializzo menu laterale
        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        if(drawer != null) {
            // se sono nella pagina principale abilito il tasto nella toolbar per visualizzare il menu
            // se no lascio il tasto indietro
            if(icona_toolbar) {
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

                drawer.setDrawerListener(toggle);
                toggle.syncState();
            }

            NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);

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
            } else {
                System.out.println("navigationView == null");

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
        } else
            System.out.println("barra laterale == null");

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
        ActionBar actionB = getSupportActionBar();
        if(actionB != null)
            actionB.setDisplayHomeAsUpEnabled(backbutton_visibility);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mod_profilo_user) {
            ModificaProfilo();
        } else if (id == R.id.prenotazioni_user) {
            ShowPrenotazioni();
        } else if (id == R.id.nav_contact_user) {
            Contattaci();
        } else if (id == R.id.nav_bugs_user) {
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
