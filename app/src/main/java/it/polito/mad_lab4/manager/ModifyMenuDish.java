package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.MenuItem;
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
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseGetDishManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveDishManager;
import it.polito.mad_lab4.firebase_manager.FirebaseUpdateAvgDishesAndOffers;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;

public class ModifyMenuDish extends EditableBaseActivity {

    private Dish dish = new Dish();
    private boolean newDish = false;
    private DishType initialType = null;
    private DishType modifiedType = null;
    private int typeInt=-1;
    private PhotoViewer imageViewer;
    private String restaurantId, dishId;

    private ArrayAdapter<String> adapter;
    private String downloadLinkThumb, downloadLinkLarge;

    private Spinner spinner;
    private EditText editName;
    private EditText editType;
    private EditText editPrice;

    private FirebaseSaveDishManager firebaseSaveDishManager;
    private FirebaseGetDishManager firebaseGetDishManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setVisibilitySave(true);
        invalidateOptionsMenu();
        setContentView(R.layout.activity_modify_menu_dish);
        getViews();
        setVisibilityAlert(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        if(setData()){
            setActivityTitle(getResources().getString(R.string.title_activity_new_dish));
        }

        else{
            setActivityTitle(getResources().getString(R.string.title_activity_edit_dish));
        }
        setToolbarColor();
    }

    private void getViews() {
        //creaSpinner();

        editName = (EditText) findViewById(R.id.edit_dishName_modifyMenu);
        editPrice = (EditText) findViewById(R.id.edit_dishPrice_modifyMenu);
        editType = (EditText) findViewById(R.id.list_dishType_modifyMenu);
        editType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeInt!=-1)
                    showDialogType(typeInt);
                else
                    showDialogType(0);
            }
        });
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

                final boolean res = firebaseSaveDishManager.waitForResult();

                if(!res){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dismissProgressDialog();
                            Log.e("Error saving the dish", "Error saving the dish");
                            return;
                        }
                    });
                }

                // AGGIORNAMENTO AVG PREZZO PIATTI/OFFERTE
                FirebaseUpdateAvgDishesAndOffers updateAvgDishesAndOffers = new FirebaseUpdateAvgDishesAndOffers();
                boolean updateRequired = updateAvgDishesAndOffers.updateAvgDishesAndOffers(restaurantId, dish.getPrice(), dish.getLastPrice(), isNewDish);
                final boolean res2;
                if(updateRequired)
                    res2 = updateAvgDishesAndOffers.waitForResult();
                else
                    res2 = true;


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dismissProgressDialog();
                        if(!res2){
                            Log.e("Error saving the dish", "Error saving the dish");
                            return;
                        }

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
                        toast.show();

                        finish();
                    }
                });

            }
        }.start();
    }

    private boolean setData(){
        try {
            //recupero il piatto da modificare
            Bundle extras = getIntent().getExtras();

            boolean isEditing = (boolean)getIntent().getBooleanExtra("isEditing", false);
            restaurantId = getIntent().getStringExtra("restaurantId");
            dishId = getIntent().getStringExtra("dishId");

            if (!isEditing){
                //è un nuovo piatto --> AGGIUNTA

                //creaSpinner();

                newDish = true;
                extras.clear();
                return true;
            }
            else{
                //setTitleTextView(getResources().getString(R.string.title_activity_edit_dish));

                getDishOnFirebase();

            }
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
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

    private void showDialogType(int i){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show In Dialog with Radio Buttons
        final String[] items = getResources().getStringArray(R.array.dishType);

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.pick_dish_type)).setSingleChoiceItems(items, i, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                editType.setText(items[item]);
                switch (item){
                    //dish.setDishType(Oggetto_piatto.type_enum.PRIMI);
                    case 0:
                        typeInt=0;
                        modifiedType = DishType.MainCourses;
                        break;
                    case 1:
                        typeInt=1;
                        //dish.setDishType(Oggetto_piatto.type_enum.SECONDI);
                        modifiedType = DishType.SecondCourses;
                        break;
                    case 2:
                        typeInt=2;
                        //dish.setDishType(Oggetto_piatto.type_enum.DESSERT);
                        modifiedType = DishType.Dessert;
                        break;
                    case 3:
                        typeInt=3;
                        //dish.setDishType(Oggetto_piatto.type_enum.ALTRO);
                        modifiedType = DishType.Other;
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
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
            float priceD;

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
                    priceD = Float.parseFloat(editPrice.getText().toString());
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

            if (!newDish)
                dish.setLastPrice(dish.getPrice());

            dish.setPrice(priceD);
            dish.setType(DishTypeConverter.fromEnumToString(modifiedType));

            saveDish(restaurantId, newDish);

            return true;

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), R.string.exceptionError, Toast.LENGTH_SHORT);
            toast.show();

            finish();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_save:
                saveInfo();
                break;
            default:
                break;
        }
        return true;
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
            /*if (spinner != null)
                spinner.setSelection(adapter.getPosition(spinnerType));*/
            editType.setText(spinnerType);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
