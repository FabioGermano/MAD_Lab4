package it.polito.mad_lab4.manager;

import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.polito.mad_lab4.R;

public abstract class BaseBarraLaterale extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ImageButton saveImageButton, alertButton, calendarButton;
    private TextView titleTextView, alertCountView;
    protected RelativeLayout alertDetailsView;
    String activityTitle =  "Titolo App";

    private boolean hideToolbar=false, hideShadow=false, save_visibility=false, calendar_visibility=false, alert_visibility = true, backbutton_visibility=true;;

    private int alertCount = 0;
    private boolean isAlertExpanded = false;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean useToolbar() {
        return true;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        configureToolbar(view);
        super.setContentView(view);
    }

    protected void hideToolbar(boolean bool){
        hideToolbar = bool;

    }
    protected void hideShadow(boolean bool){
        hideShadow=bool;
    }

    private void configureToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            if (useToolbar()) {

                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(backbutton_visibility);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                titleTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
                if(hideShadow  ){
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                        view.findViewById(R.id.shadow).setVisibility(View.GONE);

                    else
                        view.findViewById(R.id.appbar).setElevation(0);
                }
                if(hideToolbar) {
                    //getSupportActionBar().hide();
                    titleTextView.setVisibility(View.GONE);
                    //toolbar.setBackgroundColor(Color.TRANSPARENT);
                    toolbar.setBackgroundResource(R.drawable.shadow);
                }
                // Get access to the custom title view
                titleTextView.setText(activityTitle);
            } else {
                toolbar.setVisibility(View.GONE);
            }
        }
    }

    protected  void setToolbarColor(){
        toolbar.setBackgroundColor(getResources().getColor((R.color.themeColor)));
    }

    protected void setTitleTextView(String title){
        if(title!=null)
            activityTitle=title;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        toolbar.inflateMenu(R.menu.action_bar);
        final MenuItem notification = menu.findItem(R.id.menu_notify);
        notification.setVisible(alert_visibility);
        if(alert_visibility){

            RelativeLayout notificationLayout = (RelativeLayout) notification.getActionView();
            alertButton = (ImageButton) notificationLayout.findViewById(R.id.alertButton);
            alertCountView = (TextView) notificationLayout.findViewById(R.id.alertCountView);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionsItemSelected(notification);
                }
            };
            alertButton.setOnClickListener(listener);
            notificationLayout.setOnClickListener(listener);
            SetAlertCount(4);
            if(alertCount==0){
                alertButton.setImageResource(R.drawable.ic_bell_white_48dp);
            }


        }

        menu.findItem(R.id.menu_save).setVisible(save_visibility);
        menu.findItem(R.id.menu_calendar).setVisible(calendar_visibility);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                OnBackButtonPressed();
                break;
            case R.id.menu_save:
                OnSaveButtonPressed();
                break;
            case R.id.menu_calendar:
                OnCalendarButtonPressed();
                break;
            case R.id.menu_notify:
                //Intent intent = new Intent(getApplicationContext(), it.polito.mad_lab2.AlertActivity.class);
                //startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    protected void SetCalendarButtonVisibility(boolean visible)
    {
        calendar_visibility=visible;
    }

    protected void SetSaveButtonVisibility(boolean visible)
    {
        save_visibility=visible;
    }

    protected void SetAlertButtonVisibility(boolean visible)
    {
        alert_visibility=visible;
    }

    protected void SetBackButtonVisibility(boolean visible)
    {
        backbutton_visibility=visible;
    }

    protected void SetAlertCount(int count)
    {
        this.alertCount = count;
        this.alertCountView.setText(String.valueOf(count));
    }

    protected abstract void OnSaveButtonPressed();
    protected abstract void OnAlertButtonPressed();
    protected abstract void OnCalendarButtonPressed();
    protected abstract void OnBackButtonPressed();

    protected void SetAlertDelatilsView(int id)
    {
        findViewById(id).bringToFront();
        this.alertDetailsView =(RelativeLayout)findViewById(id);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mod_profilo) {
            ModificaProfilo();
            // Handle the camera action
        } else if (id == R.id.mod_menu) {
            ModificaMenu();

        } else if (id == R.id.mod_offerte) {
            ModificaOfferte();

        } else if (id == R.id.mod_disponibilità) {
            ModificaDisponibilità();

        } else if (id == R.id.gest_prenotazioni) {
            TestPrenotazioni();

        } else if (id == R.id.nav_contact) {
            /*Toast toast = Toast.makeText(getApplicationContext(), "Contattaci pressed", Toast.LENGTH_SHORT);
            toast.show();*/

        } else if (id == R.id.nav_bugs) {
            /*Toast toast = Toast.makeText(getApplicationContext(), "Bugs pressed", Toast.LENGTH_SHORT);
            toast.show();*/

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer != null)
            drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected abstract void ModificaProfilo();
    protected abstract void ModificaMenu();
    protected abstract void ModificaOfferte();
    protected abstract void ModificaDisponibilità();
    protected abstract void TestPrenotazioni();
}

