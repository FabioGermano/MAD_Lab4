package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Offer;

public class GestioneOfferte extends EditableBaseActivity {
    private ArrayList<Offer> lista_offerte = null;
    private JSONObject jsonRootObject;
    private boolean availability_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);
        SetSaveButtonVisibility(false);
        SetAlertButtonVisibility(true);

        setContentView(R.layout.activity_gestione_offerte);
        setToolbarColor();
        setTitleTextView(getResources().getString(R.string.title_activity_edit_offers));
        InitializeFABButtons(false, false, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        try {
            //recupero eventuali modifiche apportate ad un piatto
            /*Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lista_offerte = (ArrayList<Oggetto_offerta>) extras.getSerializable("lista_offerte");
                extras.clear();
                if (lista_offerte == null) {
                    boolean ris = readData();
                }

            } else {
                //altrimenti carico info dal server (o da locale)
                boolean ris = readData();
            }*/

            readOffers();

            setUpRecyclerView();
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private void readOffers() {

        lista_offerte = RestaurantBL.getRestaurantById(getApplicationContext(), 1).getOffers();
    }

    /*private boolean readData(){
        try{

            lista_offerte = new ArrayList<>();

            GestioneDB DB = new GestioneDB();
            String db = DB.leggiDB(this, "db_offerte");

            if (db != null){
                System.out.println("Leggo le offerte");
                jsonRootObject = new JSONObject(db);

                //Get the instance of JSONArray that contains JSONObjects
                JSONArray arrayDebug = jsonRootObject.optJSONArray("lista_offerte");

                //Iterate the jsonArray and print the info of JSONObjects
                for(int i=0; i < arrayDebug.length(); i++) {
                    JSONObject jsonObject = arrayDebug.getJSONObject(i);

                    String nome = jsonObject.optString("nome");
                    int prezzo = Integer.parseInt(jsonObject.optString("prezzo"));
                    String note = jsonObject.optString("note");
                    String thumb = jsonObject.optString("foto_thumb");
                    String large = jsonObject.optString("foto_large");

                    boolean days[] = new boolean[7];
                    days[0] = jsonObject.optBoolean("lun");
                    days[1] = jsonObject.optBoolean("mar");
                    days[2] = jsonObject.optBoolean("mer");
                    days[3] = jsonObject.optBoolean("gio");
                    days[4] = jsonObject.optBoolean("ven");
                    days[5] = jsonObject.optBoolean("sab");
                    days[6] = jsonObject.optBoolean("dom");

                    //creo la lista delle offerte
                    Oggetto_offerta obj = new Oggetto_offerta(nome, prezzo, days);
                    obj.setId(Integer.parseInt(jsonObject.optString("id")));
                    obj.setNote(note);
                    obj.setPhoto(thumb, large);
                    lista_offerte.add(obj);
                    System.out.println("Offerta aggiunta");
                }
                if(lista_offerte.isEmpty())
                    System.out.println("La lista è vuota");
                return true;
            }
            else {
                return false;
            }

        }catch (JSONException e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        } catch (Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            return false;
        }
    }*/

    //imposto la lista di tutte le offerte
    private void setUpRecyclerView(){
        System.out.println("Imposto CardView e adapter");
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_offerte);

        RecyclerAdapter_offerte myAdapter = new RecyclerAdapter_offerte(this, lista_offerte, availability_mode);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(this);
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5  && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void OnSaveButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }

    @Override
    protected void OnCalendarButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnBackButtonPressed() {

    }

    @Override
    protected void OnDeleteButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnEditButtonPressed() {
        //in questa schermata è disabilitato
    }

    @Override
    protected void OnAddButtonPressed() {
        Intent intent = new Intent(getApplicationContext(), ModifyOfferDish.class);
        Bundle b = new Bundle();
        b.putInt("restaurantId", 1);
        b.putBoolean("isEditing", false);
        intent.putExtras(b);
        startActivity(intent);
    }

    private void checkStoragePermission(){
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    finish();
                    startActivity(getIntent());

                } else {
                    printAlert("Negando i permessi l'app non funzionerà correttamente");

                }
                return;
            }
        }
    }

    private void printAlert(String msg){
        System.out.println(msg);
        AlertDialog.Builder miaAlert = new AlertDialog.Builder(this);
        miaAlert.setMessage(msg);

        //titolo personalizzato
        TextView title = new TextView(this);
        title.setText(getResources().getString(R.string.attenzione));
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        miaAlert.setCustomTitle(title);

        AlertDialog alert = miaAlert.create();
        alert.show();

        //centrare il messaggio
        TextView messageView = (TextView)alert.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
}
