package it.polito.mad_lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUserInfoManager;
import it.polito.mad_lab4.login_registrazione.Login;
import it.polito.mad_lab4.login_registrazione.Register;
import it.polito.mad_lab4.login_registrazione.Register_restaurant;
import it.polito.mad_lab4.manager.reservation.ReservationsActivity;
import it.polito.mad_lab4.manager.MainActivityManager;
import it.polito.mad_lab4.newData.user.User;
import it.polito.mad_lab4.reservation.user_history.ReservationsHistoryActivity;
import it.polito.mad_lab4.user.EditUserProfileActivity;
import it.polito.mad_lab4.user.ShowFavouritesActivity;
import it.polito.mad_lab4.user.UserNotificationsActivity;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Toolbar toolbar;

    private boolean hideToolbar=false, hideShadow=false, save_visibility=false,
            calendar_visibility=false,orderby_visibility=false, alert_visibility = true, backbutton_visibility=true, filter_visibility=false;
    private TextView titleTextView, alertCountView;
    String activityTitle =  "Titolo App";
    private View toolbarShadow;
    private boolean useToolbar=true;

    private ProgressDialog pd;
    private int alertCount = 0;
    private ImageButton saveImageButton, alertButton, calendarButton;
    //per visualizzare o meno, e abilitare, l'icona nella toolbar
    private boolean icona_toolbar = false;

    private FirebaseAuth mAuth;
    private FirebaseGetAuthInformation mAuthListener;

    private User infoUser = null;
    private String id = null;
    private String email = null;

    // serve per reindirizzare alla pagina della gestione ristorante se è collegato un utente manager
    private boolean homePageClient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePageClient = false;
        mAuth = FirebaseAuth.getInstance();

        /*if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
    }

    public boolean isLargeDevice(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return false;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return true;
            default:
                return false;
        }
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

    protected void setVisibilityFilter(boolean visible){
        this.filter_visibility = visible;
    }

    protected void setVisibilityOrderBy(boolean visible){
        this.orderby_visibility = visible;
    }
    protected void setVisibilitySave(boolean visible){
        this.save_visibility = visible;
    }

    protected void setVisibilityCalendar(boolean visible){
        this.calendar_visibility = visible;
    }

    protected void setVisibilityAlert(boolean visible){
        this.alert_visibility = visible;
    }
    protected boolean getVisibilityAlert(){
        return alert_visibility;
    }

    protected void configureBarraLaterale(View view) {
        //inizializzo menu laterale
        DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        try {

            if (drawer != null) {
                // se sono nella pagina principale abilito il tasto nella toolbar per visualizzare il menu
                // se no lascio il tasto indietro
                if (icona_toolbar) {
                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                    drawer.setDrawerListener(toggle);
                    toggle.syncState();
                }

                final NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);

                if(navigationView != null) {
                    new Thread()        {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showProgressBar();
                                }
                            });
                            mAuthListener = new FirebaseGetAuthInformation();
                            mAuthListener.waitForResult();
                            final FirebaseUser user = mAuthListener.getUser();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (user != null) {
                                        // User is signed in
                                        System.out.println("--------------------------> utente connesso");
                                        mAuth.removeAuthStateListener(mAuthListener);
                                        id = user.getUid();
                                        email = user.getEmail();
                                        caricaUtenteLoggato(user, navigationView);
                                    } else{
                                        System.out.println("--------------------------> utente non connesso");
                                        mAuth.removeAuthStateListener(mAuthListener);
                                        id = null;
                                        int error = mAuthListener.getErrorType();
                                        if(error == 1){
                                            System.out.println("--------------------------> Ri-autenticazione richiesta");
                                            FirebaseAuth.getInstance().signOut();
                                            id = null;
                                        }
                                        caricaUtenteDefault(navigationView);
                                    }
                                    dismissProgressDialog();
                                }
                            });
                        }
                    }.start();
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }
    }

    private void caricaUtenteDefault(final NavigationView navigationView) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_drawer_no_login);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void caricaUtenteLoggato(FirebaseUser user, final NavigationView navigationView) {
        // scarico info dal server e imposto evento per riempire la schermata con i dati utente
        final String userId = user.getUid();
        new Thread()        {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                FirebaseGetUserInfoManager userInfoManager = new FirebaseGetUserInfoManager();
                userInfoManager.getClientInfo(userId);
                userInfoManager.waitForResult();
                infoUser = userInfoManager.getUserInfo();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(infoUser != null) {
                            //se è un manager lo reindirizzo alla sua pagina corretta
                            if(homePageClient){
                                if(infoUser.getUserType().compareTo("M") == 0){
                                    Intent i= new Intent(getApplicationContext(), MainActivityManager.class);
                                    startActivity(i);
                                    dismissProgressDialog();
                                    finish();
                                }
                            }
                            riempiBarraLaterale(navigationView);
                        }
                        else {
                            //errore caricamento dati utente dal server
                            caricaUtenteDefault(navigationView);
                        }
                        dismissProgressDialog();
                    }
                });
            }
        }.start();
    }

    private void riempiBarraLaterale(NavigationView navigationView){
        View header;
        try {
            if (infoUser.getUserType().compareTo("C") == 0) {
                //CLIENT
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_drawer_login_client);

            } else if (infoUser.getUserType().compareTo("M") == 0) {
                //MANAGER
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_drawer_login_manager);
            }

            // gestore dei tasti del menù laterale e dati nell'header uguali per entrambi
            navigationView.setNavigationItemSelectedListener(this);
            header = navigationView.getHeaderView(0);

            //riempio l'header della barra laterale
            if (header != null) {
                final ImageView user_logo = (ImageView) header.findViewById(R.id.nav_drawer_logo);
                final TextView user_name = (TextView) header.findViewById(R.id.nav_drawer_name);
                final TextView emailField = (TextView) header.findViewById(R.id.nav_drawer_email);

                if (infoUser.getUserType().compareTo("C") == 0) {
                    //CLIENT
                    header.setBackgroundResource(R.drawable.side_nav_bar_client);
                } else if (infoUser.getUserType().compareTo("M") == 0) {
                    //MANAGER
                    header.setBackgroundResource(R.drawable.side_nav_bar_restaurant);
                }

                //setto il nome dell'utente
                if (user_name != null)
                    user_name.setText(infoUser.getName());
                //setto la foto dell'utente
                if (user_logo != null) {

                   System.out.println("LINK: "+ infoUser.getAvatarDownloadLink()) ;
                   Glide.with(this).load(infoUser.getAvatarDownloadLink()).centerCrop().into(user_logo);
                }
                if (emailField != null){
                    emailField.setText(email);
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
    }

    public void setHomePageClient(){
        this.homePageClient = true;
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

    protected  void setToolbarColor(){
        toolbar.setBackgroundColor(getResources().getColor((R.color.themeColor)));
    }

    protected void setTitleVisible(){
        titleTextView.setVisibility(View.VISIBLE);
    }

    protected void hideToolbarShadow(boolean bool){
        toolbarShadow.setVisibility(View.GONE);
    }

    protected void setActivityTitle(String title){
        if(title!=null)
            titleTextView.setText(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        toolbar.inflateMenu(R.menu.action_bar);
        final MenuItem filter = menu.findItem(R.id.menu_find);
        final MenuItem calendar = menu.findItem(R.id.menu_calendar);
        final MenuItem save = menu.findItem(R.id.menu_save);
        final MenuItem notify = menu.findItem(R.id.menu_notify);
        final MenuItem order = menu.findItem(R.id.menu_orderBy);
        filter.setVisible(filter_visibility);
        calendar.setVisible(calendar_visibility);
        save.setVisible(save_visibility);
        notify.setVisible(alert_visibility);
        order.setVisible(orderby_visibility);
        if(alert_visibility){

            RelativeLayout notificationLayout = (RelativeLayout) notify.getActionView();
            alertButton = (ImageButton) notificationLayout.findViewById(R.id.alertButton);
            alertCountView = (TextView) notificationLayout.findViewById(R.id.alertCountView);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(notify);
                }
            };
            alertButton.setOnClickListener(listener);
            notificationLayout.setOnClickListener(listener);
            SetAlertCount(4);
            if(alertCount==0){
                alertButton.setImageResource(R.drawable.ic_bell_white_48dp);
            }


        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_notify:
                //TODO un controllo se sono utente o gestore e recuperare dati diversi
                Intent i = new Intent(getBaseContext(), UserNotificationsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        return true;
    }


    protected void ModificaProfiloManager() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab4.manager.EditRestaurantProfile.class);
        startActivity(intent);
    }


    protected void ModificaMenu() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab4.manager.GestioneMenu.class);
        startActivity(intent);
    }


    protected void ModificaOfferte() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab4.manager.GestioneOfferte.class);
        startActivity(intent);
    }


    protected void ModificaDisponibilità() {
        Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab4.manager.EditAvailability.class);
        startActivity(intent);
    }


    protected void VisualizzaPrenotazioni() {
        Intent intent = new Intent(getApplicationContext(), ReservationsActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            //CLIENT
            case R.id.mod_profilo_client:
                ModificaProfilo();
                //Toast.makeText(getApplicationContext(), "profilo client", Toast.LENGTH_LONG).show();
                break;
            case R.id.prenotazioni_client:
                //Toast.makeText(getApplicationContext(), "prenotazioni client", Toast.LENGTH_LONG).show();
                ShowPrenotazioni();
                break;
            case R.id.favourites_client:
                showFavourites();
                break;
            //MANAGER
            case R.id.mod_profilo_manager:
                ModificaProfiloManager();
                //Toast.makeText(getApplicationContext(), "profilo manager", Toast.LENGTH_LONG).show();
                break;
            case R.id.mod_menu_manager:
                ModificaMenu();
                //Toast.makeText(getApplicationContext(), "menu manager", Toast.LENGTH_LONG).show();
                break;
            case R.id.mod_offerte_manager:
                ModificaOfferte();
                //Toast.makeText(getApplicationContext(), "offerte manager", Toast.LENGTH_LONG).show();
                break;
            case R.id.mod_disponibilità_manager:
                ModificaDisponibilità();
                //Toast.makeText(getApplicationContext(), "disponibilità manager", Toast.LENGTH_LONG).show();
                break;
            case R.id.prenotazioni_manager:
                VisualizzaPrenotazioni();
                //Toast.makeText(getApplicationContext(), "prenotazioni manager", Toast.LENGTH_LONG).show();
                break;
            //DEFAULT
            case R.id.esegui_login:
                Login();
                break;
            case R.id.esegui_registrazione:
                Registrazione();
                break;
            case R.id.esegui_registrazione_ristorante:
                Registrazione_ristorante();
                break;
            case R.id.logout_user:
                Logout();
                break;
            case R.id.nav_contact:
                Contattaci();
                break;
            case R.id.nav_bugs:
                SegnalaBug();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        Questi metodi, essendo uguali sempre possiamo definirli qui
     */
    private void showFavourites(){
        Intent i = new Intent(getBaseContext(), ShowFavouritesActivity.class);
        startActivity(i);
    }
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
        // accedo alla pagina del login e se l'utente vuole memorizzare in modo permanente i dati
        // li salviamo sul file
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

    private void Registrazione(){
        Intent i = new Intent(getApplicationContext(), Register.class);
        startActivity(i);
    }

    private void Registrazione_ristorante(){
        Intent i = new Intent(getApplicationContext(), Register_restaurant.class);
        startActivity(i);
    }



    private void Logout(){
        //eseguo il logout e cancello eventualmente il file con le credenziali
        FirebaseAuth.getInstance().signOut();
        id = null;
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.logout_message), Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    protected void SetAlertCount(int count)
    {
        this.alertCount = count;
        this.alertCountView.setText(String.valueOf(count));
    }

    private void ModificaProfilo(){

        Bundle b = new Bundle();
        b.putString("userId", id);
        Intent i = new Intent(getApplicationContext(), EditUserProfileActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

    private void ShowPrenotazioni() {
        Intent i= new Intent(getApplicationContext(), ReservationsHistoryActivity.class);
        startActivity(i);
    };


    public void showProgressBar(){
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
    }

    public void dismissProgressDialog() {
        if (pd != null && pd.isShowing()){
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth!= null && mAuthListener!=null)
            mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("-------------> OnStop chiamato!!!!");
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
