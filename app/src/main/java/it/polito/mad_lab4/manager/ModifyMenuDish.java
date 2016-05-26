package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.jar.Manifest;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;

public class ModifyMenuDish extends EditableBaseActivity implements ValueEventListener, DatabaseReference.CompletionListener{

    private Dish dish = new Dish();
    private Oggetto_menu dish_list= null;
    private int position = -1;
    private boolean newDish = false;
    private DishType initialType = null;
    private DishType modifiedType = null;

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoViewer imageViewer;
    private String restaurantId, dishId;

    private ArrayAdapter<String> adapter;

    private Spinner spinner;
    private EditText editName;
    private EditText editPrice;

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

    private void  getDishById(final String restaurantId, final String dishId){
        Thread t = new Thread() {
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("menu/" + restaurantId+"/"+dishId);
                myRef.addListenerForSingleValueEvent(ModifyMenuDish.this);
            }
        };
        t.start();
    }

    private void saveDish(final String restaurantId, final String dishId, final boolean isNewDish){
        Thread t = new Thread() {
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                if(!isNewDish) {
                    DatabaseReference myRef = database.getReference("menu/" + restaurantId + "/" + dishId);
                    myRef.setValue(dish, ModifyMenuDish.this);
                }
                else{
                    DatabaseReference myRef = database.getReference("menu/" + restaurantId);
                    String key = myRef.push().getKey();
                    dish.setDishId(key);
                    myRef.child(key).setValue(dish, ModifyMenuDish.this);
                }
            }
        };
        t.start();
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

                getDishById(restaurantId, dishId);
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

            saveDish(restaurantId, dishId, newDish);

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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        dish = dataSnapshot.getValue(Dish.class);
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

            //imageThumb = dish.getThumbPath();
            //imageLarge = dish.getLargePath();

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
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if(databaseError == null){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://mad-lab4-2ef30.appspot.com");
            StorageReference thumbname = storageRef.child(dish.getDishId()+".jpg");

            // Create file metadata including the content type
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();


            Bitmap bitmap = this.imageViewer.getThumb();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = thumbname.putBytes(data, metadata);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(getApplicationContext(), GestioneMenu.class);
                    startActivity(intent);
                }
            });
        }
    }
}
