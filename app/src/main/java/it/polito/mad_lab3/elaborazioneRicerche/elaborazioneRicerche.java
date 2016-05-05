package it.polito.mad_lab3.elaborazioneRicerche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.MainActivity;
import it.polito.mad_lab3.R;

public class elaborazioneRicerche extends BaseActivity implements fragment_ricercaAvanzata.OnButtonPressedListener{
    private ArrayList<Oggetto_risultatoRicerca> lista_risultati = null;
    private fragment_ricercaAvanzata fragmentRicerca;
    private  boolean finderOpen = false;
    private FrameLayout la;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVisibilityFilter();
        setContentView(R.layout.activity_elaborazione_ricerche);

        setActivityTitle(getResources().getString(R.string.titolo_elaborazioneRicerche));
        try {
            //ottengo i risultati della ricerca

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lista_risultati = (ArrayList<Oggetto_risultatoRicerca>) extras.getSerializable("results");
                extras.clear();

            } else {
                ///SEI NELLA MERDA!!!
                stampaMessaggioErrore();
            }

            fragmentRicerca = (fragment_ricercaAvanzata) getSupportFragmentManager().findFragmentById(R.id.ricerca_avanzata);

            setUpRecyclerView();
        } catch (Exception e){
            stampaMessaggioErrore();
        }

    }

    @Override
    protected void filterButton() {
        if(!finderOpen){
            //apro il fragment
            fragmentRicerca.setFragment();
            finderOpen = true;
        } else {
            //chiudo il fragment
            fragmentRicerca.closeFragment();
            finderOpen = false;
        }
    }

    private void stampaMessaggioErrore(){
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.exceptionError), Toast.LENGTH_SHORT);
        toast.show();

        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void setUpRecyclerView(){
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_ricerca);

        RecyclerAdapter_risultatoRicerca myAdapter = new RecyclerAdapter_risultatoRicerca(this, lista_risultati);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onButtonPressed() {
        Toast t = Toast.makeText(getApplicationContext(), "Bottone premuto", Toast.LENGTH_SHORT);
        t.show();
        //leggo tutte le info che mi passa il fragment ed eseguo la ricerca più approfondita
    }
}
