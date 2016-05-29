package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.firebase_manager.FirebaseGetDishManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveDishManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;

public class ModifyMenuDish extends EditableBaseActivity {

    private Dish dish = new Dish();
    private boolean newDish = false;
    private DishType initialType = null;
    private DishType modifiedType = null;

    private PhotoViewer imageViewer;
    private String restaurantId, dishId;

    private ArrayAdapter<String> adapter;
    private String downloadLinkThumb, downloadLinkLarge;

    private Spinner spinner;
    private EditText editName;
    private EditText editPrice;

    private FirebaseSaveDishManager firebaseSaveDishManager;
    private FirebaseGetDishManager firebaseGetDishManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(false);

        SetSaveButtonVisibility(true);

        setContentView(R.layout.activity_modify_menu_dish);
        getViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        setData();
        setToolbarColor();
    }

    private void getViews() {
        creaSpinner();

        editName = (EditText) findViewById(R.id.edit_dishName_modifyMenu);
        editPrice = (EditText) findViewById(R.id.edit_dishPrice_modifyMenu);

        imageViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.imageDish_modifyMenu);
    }

    private void saveDish(final String restaurantId, final  boolean isNewDish) {
        new Thread()
        {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                firebaseSaveDishManager = new FirebaseSaveDishManager();
                firebaseSaveDishManager.saveDish(restaurantId,
                        isNewDish,
                        dish,
                        !imageViewer.isImageTobeSetted(),
                        imageViewer.getThumb(),
                        imageViewer.getLarge());

                boolean res = firebaseSaveDishManager.waitForResult();

                if(!res){
                    Log.e("Error saving the dish", "Error saving the dish");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dismissProgressDialog();

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
                        startActivity(intent);
                    }
                });

            }
        }.start();
    }

    private void setData(){
        try {
            //recupero il piatto da modificare
            Bundle extras = getIntent().getExtras();

            boolean isEditing = (boolean)getIntent().getBooleanExtra("isEditing", false);
            restaurantId = getIntent().getStringExtra("restaurantId");
            dishId = getIntent().getStringExtra("dishId");

            if (!isEditing){
                //è un nuovo piatto --> AGGIUNTA
                setTitleTextView(getResources().getString(R.string.title_activity_new_dish));
                creaSpinner();

                newDish = true;
                extras.clear();
            }
            else{
                setTitleTextView(getResources().getString(R.string.title_activity_edit_dish));

                getDishOnFirebase();

            }
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void getDishOnFirebase(){
        new Thread()
        {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                firebaseGetDishManager = new FirebaseGetDishManager();
                firebaseGetDishManager.getDish(restaurantId, dishId);
                firebaseGetDishManager.waitForResult();
                dish = firebaseGetDishManager.getResult();

                if(dish == null){
                    Log.e("returned null dish", "resturned null dish");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDish();
                        dismissProgressDialog();
                    }
                });

            }
        }.start();
    }

    private void creaSpinner(){
        //gestisco il menu a tendina per la tipologia del piatto
        spinner = (Spinner)findViewById(R.id.list_dishType_modifyMenu);
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{getResources().getString(R.string.first), getResources().getString(R.string.second), getResources().getString(R.string.dessert), getResources().getString(R.string.other)}
        );
        if(spinner != null) {
            spinner.setAdapter(adapter);

            //listener per salvare la selezione dell'utente
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    if(dish != null) {
                        String selezione = (String) adapter.getItemAtPosition(pos);

                        if(selezione.compareTo(getResources().getString(R.string.first)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.PRIMI);
                            modifiedType = DishType.MainCourses;
                        }
                        else  if(selezione.compareTo(getResources().getString(R.string.second)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.SECONDI);
                            modifiedType = DishType.SecondCourses;
                        }
                        else  if(selezione.compareTo(getResources().getString(R.string.dessert)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.DESSERT);
                            modifiedType = DishType.Dessert;
                        }
                        else  if(selezione.compareTo(getResources().getString(R.string.other)) == 0){
                            //dish.setDishType(Oggetto_piatto.type_enum.ALTRO);
                            modifiedType = DishType.Other;
                        }
                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    private boolean saveInfo(){

        // aggiorno l'oggetto piatto con tutte le nuove informazioni e passo indietro, all'activity di modifica menu principale, l'intere tabelle
        try {
            /*
                piccola modifica per integrità codice, l'oggetto lo modifico solo se ho letto
                correttamente tutti i campi, senza nessun errore.
                Altrimenti, teoricamente, posso riempire alcuni campi si e altri no (nulla di che)
             */
            String nomeD;
            int priceD;

            /* ##################################
                 Lettura campi dalla schermata
               ##################################
             */
            if (editName != null) {
                nomeD = editName.getText().toString();
                if(nomeD.compareTo("")==0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                //else
                    //dish.setName(nomeD);
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }


            if (editPrice != null) {
                String price =  editPrice.getText().toString();
                if (price.compareTo("") != 0) {
                    priceD = Integer.parseInt(editPrice.getText().toString());
                    //dish.setCost(priceD);
                }
                else{
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            dish.setDishName(nomeD);
            dish.setPrice(priceD);
            dish.setType(DishTypeConverter.fromEnumToString(modifiedType));
            //dish.setThumbPath(imageThumb);
            //dish.setLargePath(imageLarge);

            saveDish(restaurantId, newDish);

            return true;

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exceptionError, Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
            startActivity(intent);
            return false;
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

    @Override
    protected void OnSaveButtonPressed() {
        //salvo le info e torno alla schermata di gestione menu principale
        saveInfo();
    }

    @Override
    protected void OnAlertButtonPressed() {
        //vai alla lista delle prenotazioni
    }

    @Override
    protected void OnCalendarButtonPressed() {


    }

    @Override
    protected void OnBackButtonPressed() {
    }

    @Override
    protected void OnDeleteButtonPressed() {
        // TO DO
    }

    @Override
    protected void OnEditButtonPressed() {
        throw  new UnsupportedOperationException();
    }

    @Override
    protected void OnAddButtonPressed() {
        throw  new UnsupportedOperationException();
    }

    private void checkPermessi(){
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) ;
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (camera != PackageManager.PERMISSION_GRANTED || storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
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
            }
        }
    }

    public void setDish() {
        String spinnerType = null;

        if(dish != null){
            initialType = DishTypeConverter.fromStringToEnum(dish.getType());

            if(dish.getType().equals(DishTypeConverter.fromEnumToString(DishType.MainCourses))){
                spinnerType = getResources().getString(R.string.first);
            }
            else if(dish.getType().equals(DishTypeConverter.fromEnumToString(DishType.SecondCourses))){
                spinnerType = getResources().getString(R.string.second);
            }
            else if(dish.getType().equals(DishTypeConverter.fromEnumToString(DishType.Dessert))){
                spinnerType = getResources().getString(R.string.dessert);
            }
            else if(dish.getType().equals(DishTypeConverter.fromEnumToString(DishType.Other))){
                spinnerType = getResources().getString(R.string.other);
            }

            modifiedType = DishTypeConverter.fromStringToEnum(dish.getType()); //inizializzo al valore corrente del piatto da modificare

            if(dish.getThumbDownloadLink() != null) {
                this.imageViewer.setThumbBitmapByURI(dish.getThumbDownloadLink());
                this.imageViewer.setLargePhotoDownloadLink(dish.getLargeDownloadLink());
            }

            if (editName != null) {
                editName.setText(dish.getDishName());
            }

            if (editPrice != null) {
                editPrice.setText(String.valueOf(dish.getPrice()));
            }

            //imposto lo spinner al valore corretto del piatto
            if (spinner != null)
                spinner.setSelection(adapter.getPosition(spinnerType));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
