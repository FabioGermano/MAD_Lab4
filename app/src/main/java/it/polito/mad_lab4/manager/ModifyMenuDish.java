package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.Manifest;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.manager.common.PhotoManager;
import it.polito.mad_lab4.manager.common.PhotoType;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewerListener;

public class ModifyMenuDish extends EditableBaseActivity implements PhotoViewerListener {

    private Dish dish = null;
    private Oggetto_menu dish_list= null;
    private int position = -1;
    private boolean newDish = false;
    private DishType initialType = null;
    private DishType modifiedType = null;

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager imageManager;
    private PhotoViewer imageViewer;
    private String id_image;
    private int restaurantId, dishId;

    private  Spinner spinner;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(false);
        //setTitleTextView(getResources().getString(R.string.title_activity_edit_dish));
        //setContentView(R.layout.activity_modify_menu_dish);

        SetSaveButtonVisibility(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        readData();
       // setToolbarColor();
        initializePhotoManagement();

    }

    private void initializePhotoManagement()
    {
        imageViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.imageDish_modifyMenu);
        imageManager = new PhotoManager(getApplicationContext(), PhotoType.MENU, this.imageThumb, this.imageLarge);

        id_image = "image_"+ dish.getDishId();

        imageViewer.setThumbBitmap(BitmapFactory.decodeFile(imageManager.getThumb(id_image)));


    }

    private void readData(){
        try {
            String spinnerType;
            boolean error = false;
            //recupero il piatto da modificare
            Bundle extras = getIntent().getExtras();


            if (extras == null){
                //ERRORE! Da verificare
                this.finish();
            }
            else {

                boolean isEditing = (boolean)getIntent().getBooleanExtra("isEditing", false);
                restaurantId = (int)getIntent().getIntExtra("restaurantId", -1);
                dishId = (int)getIntent().getIntExtra("dishId", -1);

                    if (!isEditing){
                        //è un nuovo piatto --> AGGIUNTA

                        setContentView(R.layout.activity_modify_menu_dish);
                        setTitleTextView(getResources().getString(R.string.title_activity_new_dish));
                        creaSpinner();

                        dish = new Dish(null,
                                RestaurantBL.getNewMenuId(RestaurantBL.getRestaurantById(getApplicationContext(), 1)),
                                (float)0.0,
                                0,
                                (float)0.0,
                                null,
                                null,
                                DishTypeConverter.fromEnumToString(DishType.MainCourses)
                                );

                        newDish = true;
                        extras.clear();
                        return;
                    }
                    else{
                        /*
                            settaggio dinamico del titolo
                         */
                        setContentView(R.layout.activity_modify_menu_dish);

                        setTitleTextView(getResources().getString(R.string.title_activity_edit_dish));
                        creaSpinner();

                        dish = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId).getDishById(dishId);

                        initialType = dish.getEnumType();

                            switch (dish.getEnumType()) {
                                case MainCourses:
                                    spinnerType = getResources().getString(R.string.first);
                                    break;
                                case SecondCourses:
                                    spinnerType = getResources().getString(R.string.second);
                                    break;
                                case Dessert:
                                    spinnerType = getResources().getString(R.string.dessert);
                                    break;
                                case Other:
                                    spinnerType = getResources().getString(R.string.other);
                                    break;
                                default:
                                    System.out.println("Typology unknown");
                                    error = true;
                                    return;
                            }

                            modifiedType = dish.getEnumType(); //inizializzo al valore corrente del piatto da modificare

                            if (!error) {
                                //carico le informazioni nella pagina di modifica
                                EditText editName = (EditText) findViewById(R.id.edit_dishName_modifyMenu);
                                EditText editPrice = (EditText) findViewById(R.id.edit_dishPrice_modifyMenu);

                                imageThumb = dish.getThumbPath();
                                imageLarge = dish.getLargePath();

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

            }

            if (error){
                //qualche errore durante la lettura/modifica
                // DA VERIFICARE!!!
                this.finish();
            }
        } catch(Exception e){
            System.out.println("Eccezione: " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
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
            EditText editName = (EditText) findViewById(R.id.edit_dishName_modifyMenu);
            EditText editPrice = (EditText) findViewById(R.id.edit_dishPrice_modifyMenu);

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
            dish.setThumbPath(imageThumb);
            dish.setLargePath(imageLarge);

            if(newDish){
                RestaurantBL.getRestaurantById(getApplicationContext(), this.restaurantId).getDishes().add(dish);
            }

            RestaurantBL.saveChanges(getApplicationContext());

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

    private void commitPhotos() {
            this.imageThumb = this.imageManager.commitThumb(id_image);
            this.imageLarge = this.imageManager.commitLarge(id_image);

    }

    @Override
    protected void OnSaveButtonPressed() {
        //salvo le info e torno alla schermata di gestione menu principale
        commitPhotos();
        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            this.imageManager.destroy(id_image);

            Bundle b = new Bundle();
            b.putSerializable("dish_list", dish_list);

            Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
            intent.putExtras(b);
            startActivity(intent);
        }
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


    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {
        if (fragmentId == R.id.imageDish_modifyMenu){
            this.imageManager.saveThumb(thumb, id_image);
            this.imageManager.saveLarge(large, id_image);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        if (fragmentId == R.id.imageDish_modifyMenu){
            return BitmapFactory.decodeFile(this.imageManager.getLarge(id_image));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if (fragmentId == R.id.imageDish_modifyMenu){
            this.imageManager.removeThumb(id_image);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imageManager.destroy(id_image);
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
                return;
            }
        }
    }
}
