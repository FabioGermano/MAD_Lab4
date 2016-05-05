package it.polito.mad_lab3.elaborazioneRicerche;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.MainActivity;
import it.polito.mad_lab3.R;

public class elaborazioneRicerche extends BaseActivity {
    private ArrayList<Oggetto_risultatoRicerca> lista_risultati = null;

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
            }

            /*lista_risultati = new ArrayList<>();
            lista_risultati.add(new Oggetto_risultatoRicerca("DaGianni", "corso Duca degli Abruzzi 15", null, "10-20 euro", 5, Oggetto_risultatoRicerca.type.RISTORANTE));
            lista_risultati.add(new Oggetto_risultatoRicerca("Pizza&Pasta", "via ...", null, "5-20 euro", 3, Oggetto_risultatoRicerca.type.RISTORANTE));
            lista_risultati.add(new Oggetto_risultatoRicerca("Bar Ambrogio", "corso Peschiera 44", null, "4-11 euro", 1.5F , Oggetto_risultatoRicerca.type.RISTORANTE));
            lista_risultati.add(new Oggetto_risultatoRicerca("Bar Fuori dal Poli", "via hhhhhh", null, "5-10 euro", 1, Oggetto_risultatoRicerca.type.RISTORANTE));
            lista_risultati.add(new Oggetto_risultatoRicerca("McDonald's", "corso Stati Uniti 34", null, "5-10 euro", 1.4F, Oggetto_risultatoRicerca.type.RISTORANTE));
            lista_risultati.get(0).setDescrizione("Ottimi panini e kebab");*/

            //imposto la lista delle varie ricerche

            setUpRecyclerView();
        } catch (Exception e){
            stampaMessaggioErrore();
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


    public void searchRestaurant(View view) {
    }
}
