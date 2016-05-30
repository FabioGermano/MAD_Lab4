package it.polito.mad_lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.polito.mad_lab4.bl.UserBL;
import it.polito.mad_lab4.common.UserSession;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.login.Login;
import it.polito.mad_lab4.user.UserNotificationsActivity;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Toolbar toolbar;

    private boolean hideToolbar=false, hideShadow=false, save_visibility=false,
            calendar_visibility=false,orderby_visibility=false, alert_visibility = true, backbutton_visibility=true, filter_visibility=false;
    private TextView titleTextView, alertCountView;
    String activityTitle =  "Titolo App";
    private View toolbarShadow;
    private boolean useToolbar=true;
    private User userInformation;
    private ProgressDialog pd;
    private int alertCount = 0;
    private ImageButton saveImageButton, alertButton, calendarButton;
    //per visualizzare o meno, e abilitare, l'icona nella toolbar
    private boolean icona_toolbar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            //userInformation = controlloLogin();
            //boolean login = userInformation.getUserLoginInfo().isLogin();
            boolean login = UserSession.userId != null;

            if(login){
                userInformation = UserBL.getUserById(getApplicationContext(), UserSession.userId);
            }

            if(navigationView != null) {
                if (login) {
                    navigationView.inflateMenu(R.menu.activity_drawer_login);
                    navigationView.setNavigationItemSelectedListener(this);
                    header = navigationView.getHeaderView(0);
                } else {
                    navigationView.inflateMenu(R.menu.activity_drawer_no_login);
                    navigationView.setNavigationItemSelectedListener(this);
                    header = navigationView.getHeaderView(0);
                }


                //riempio l'header della barra laterale
                if (header != null && login) {
                    ImageView user_logo = (ImageView) header.findViewById(R.id.nav_drawer_logo);
                    TextView user_name = (TextView) header.findViewById(R.id.nav_drawer_name);

                    //setto il nome dell'utente
                    if(user_name != null)
                        user_name.setText(userInformation.getName());
                    //setto la foto dell'utente
                    if (user_logo != null){
                        String path = userInformation.getPhoto_path();
                        if (path != null){
                            try {
                                Bitmap bitmap = BitmapFactory.decodeFile(path);
                                if(bitmap != null)
                                    user_logo.setImageBitmap(bitmap);
                            } catch (Exception e){
                                System.out.println("Errore creazione bitmap"); //debug
                            }
                        }
                    }
                }
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

    protected abstract User controlloLogin();

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
        } else if(id == R.id.logout_user){
            Logout();
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
        // accedo alla pagina del login e se l'utente vuole memorizzare in modo permanente i dati
        // li salviamo sul file
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }

    private void Logout(){
        //eseguo il logout e cancello eventualmente il file con le credenziali
        UserSession.userId = null;
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.logout_message), Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    protected void SetAlertCount(int count)
    {
        this.alertCount = count;
        this.alertCountView.setText(String.valueOf(count));
    }

    /******************/

    /*
        Questi invece sono personali per ogni utente e quindi è meglio gestirli nella schermata
        appropriata con tutti i dati corretti
     */
    protected abstract void ModificaProfilo();
    protected abstract void ShowPrenotazioni();
    public void showProgressBar(){
        pd = new ProgressDialog(BaseActivity.this,R.style.DialogTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
    }

    public void dismissProgressDialog(){
        if(pd.isShowing())
            pd.dismiss();
    }
}
