package it.polito.mad_lab4.elaborazioneRicerche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.MainActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.user.User;

public class elaborazioneRicerche extends BaseActivity implements fragment_ricercaAvanzata.OnButtonPressedListener{
    private ArrayList<Oggetto_risultatoRicerca> lista_risultati = null;
    private ArrayList<Oggetto_risultatoRicerca> lista_risultati_base = null;
    private fragment_ricercaAvanzata fragmentRicerca;
    private  boolean finderOpen = false;
    private RecyclerAdapter_risultatoRicerca myAdapter;
    private User userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setVisibilityFilter();
        setContentView(R.layout.activity_elaborazione_ricerche);

        hideToolbar(true);
        hideToolbarShadow(true);
        setTitleVisible();
        setActivityTitle(getResources().getString(R.string.titolo_elaborazioneRicerche));
        setVisibilityFilter(true);
        invalidateOptionsMenu();

        try {
            //ottengo i risultati della ricerca

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                userInfo = (User) extras.getSerializable("userInfo");
                lista_risultati = (ArrayList<Oggetto_risultatoRicerca>) extras.getSerializable("results");
                //importante tengo una copia per quando eseguo una ricerca avanzata, così posso sempre tornare alla
                //ricerca iniziale senza doverla rifare ogni volta da capo
                lista_risultati_base = lista_risultati;
                extras.clear();

            } else {
                ///SEI NELLA MERDA!!!
                stampaMessaggioErrore();
            }

            if(userInfo == null){
                userInfo = new User(null, null, -1);
            }
            
            fragmentRicerca = (fragment_ricercaAvanzata) getSupportFragmentManager().findFragmentById(R.id.ricerca_avanzata);

            setUpRecyclerView();
        } catch (Exception e){
            stampaMessaggioErrore();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_find:
                filterButton();
                break;
            default:
                break;
        }

        return true;
    }

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

        /*Bundle b = new Bundle();
        b.putSerializable("userInfo", userInfo);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtras(b);
        startActivity(i);*/
        finish();

    }


    private void setUpRecyclerView(){
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_ricerca);

        myAdapter = new RecyclerAdapter_risultatoRicerca(this, lista_risultati);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onButtonPressed(String type, String cost, String rating, String nomePiatto) {
        //leggo tutte le info che mi passa il fragment ed eseguo la ricerca più approfondita
        boolean change = false;
        ArrayList<Oggetto_risultatoRicerca> newList = new ArrayList<>();
        //modifico la lista dei risultati in modo da filtrarla opportunamente

        if(type != null) {
            change = true;
            //filtro per tipo
            for(Oggetto_risultatoRicerca obj : lista_risultati){
                if(type.compareTo("ALL") == 0){
                    if((obj.isTakeAway() && obj.isOnPlace()))
                            newList.add(obj);
                }
                else if(type.equals("TA")) {
                    if (obj.isTakeAway())
                        newList.add(obj);
                }
                else if(type.equals("R")) {
                    if (obj.isOnPlace())
                        newList.add(obj);
                }
            }
            lista_risultati = newList;
            newList = new ArrayList<>();
        }

        if(cost != null) {
            change = true;
            //filtro per costo
            for(Oggetto_risultatoRicerca obj : lista_risultati){
                if(obj.getFasciaPrezzo().compareTo(cost) == 0){
                    newList.add(obj);
                }
            }
            lista_risultati = newList;
            newList = new ArrayList<>();
        }

        if(rating != null){
            change = true;
            //filtro per valutazione
            String val = null;
            for(Oggetto_risultatoRicerca obj : lista_risultati){
                if(obj.getValutazione() <= 1.5 )
                    val = "★";
                else if(obj.getValutazione() <= 3.5)
                    val = "★★";
                else
                    val = "★★★";

                if(val.compareTo(rating) == 0){
                    newList.add(obj);
                }

            }
            lista_risultati = newList;
            newList = new ArrayList<>();
        }
/*
        if(nomePiatto != null ){
            if(!nomePiatto.isEmpty()){
                System.out.println("Ricerco per nome: ");
                change = true;
                //filtro per valutazione
                String val = null;
                for(Oggetto_risultatoRicerca obj : lista_risultati){
                    ArrayList<String> listaPiatti = RestaurantBL.getAllDishName(this, obj.getId());
                    for(String nome : listaPiatti){
                        System.out.println(nome + " == " + nomePiatto);
                        if(nome.contains(nomePiatto)){
                            newList.add(obj);
                        }
                    }
                }
                lista_risultati = newList;
            }
        }*/

        if(change) {
            if(lista_risultati.isEmpty()){
                lista_risultati = lista_risultati_base;

                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
                toast.show();
            } else {
                myAdapter.setNewArray(lista_risultati);
                myAdapter.notifyDataSetChanged();
                fragmentRicerca.setResetButton();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.nessun_risultato), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public void onResetPressed() {
        lista_risultati = lista_risultati_base;
        myAdapter.setNewArray(lista_risultati);
        myAdapter.notifyDataSetChanged();
    }
}
