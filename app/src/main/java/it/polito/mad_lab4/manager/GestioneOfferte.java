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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;

public class GestioneOfferte extends EditableBaseActivity {
    private ArrayList<Offer> lista_offerte = null;
    private JSONObject jsonRootObject;
    private boolean availability_mode = false;
    private RecyclerAdapter_offerte myAdapter;

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
            readOffers();

            setUpRecyclerView();
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
        }
    }

    private void readOffers() {
        lista_offerte = new ArrayList<Offer>();// RestaurantBL.getRestaurantById(getApplicationContext(), 1).getOffers();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        showProgressBar();

        DatabaseReference myRef = database.getReference("offers/" + "-KIrgaSxr9VhHllAjqmp");
        myRef.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    dismissProgressDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef = database.getReference("offers/" + "-KIrgaSxr9VhHllAjqmp");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                dismissProgressDialog();

                Offer o = dataSnapshot.getValue(Offer.class);
                lista_offerte.add(o);
                myAdapter.notifyItemInserted(lista_offerte.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Offer o = dataSnapshot.getValue(Offer.class);

                int position = findPositionOnList(lista_offerte, o.getOfferId());
                lista_offerte.remove(position);
                myAdapter.notifyItemRemoved(position);
                myAdapter.notifyItemRangeChanged(position, lista_offerte.size());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int findPositionOnList(ArrayList<Offer> lista_offerte, String offerId) {
        int i = 0;
        for(Offer d : lista_offerte){
            if(d.getOfferId().equals(offerId)){
                return i;
            }
            i++;
        }

        return i;
    }

    //imposto la lista di tutte le offerte
    private void setUpRecyclerView(){
        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView_offerte);

        myAdapter = new RecyclerAdapter_offerte(this, lista_offerte, availability_mode);
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
        b.putString("restaurantId", "-KIrgaSxr9VhHllAjqmp");
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
