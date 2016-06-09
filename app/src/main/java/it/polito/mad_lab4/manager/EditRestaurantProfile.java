package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab4.*;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantProfileManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveRestaurantProfileManager;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.newData.restaurant.CoverImage;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class EditRestaurantProfile extends it.polito.mad_lab4.BaseActivity {

    private PhotoViewer logoPhotoViewer;
    private PhotoViewer[] coversPhotoViewer;
    private Restaurant restaurant;
    private CheckBox takeaway, onplace;
    // Views
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_address;
    private EditText edit_email;
    private EditText edit_description;
    private EditText cuisineText;
    private TextView mon, tue, wed, thu, fri, sat, sun;
    private TextView[] week_array;
    private Switch resSwitch;
    private Switch wifiSwitch;
    private Switch seatsSwitch;
    private Switch creditSwitch;
    private Switch bancomatSwitch;
    private Switch musicSwitch;
    private Switch parkSwitch;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_restaurant_profile);
        getViews();
        setVisibilityAlert(false);

        restaurantId = (String) getIntent().getExtras().getString("restaurantId");

        setToolbarColor();
        setVisibilitySave(true);
        invalidateOptionsMenu();
        setActivityTitle(getResources().getString(R.string.title_activity_edit_restaurant_profile));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        gerRestaurantByRestaurantId(restaurantId);

        initializePhotoManagement();
    }

    private void showDialogType(int i){
        android.support.v7.app.AlertDialog dialog;

        // Strings to Show
        final String[] items = getResources().getStringArray(R.array.userType);

        // Creating and Building the Dialog
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.pick_type)).setSingleChoiceItems(items, i, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                //typeText.setText(items[item]);
                //typeChoice=item;
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.getListView().setDividerHeight(1);
        dialog.show();
    }



    private void getViews() {
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_description = (EditText) findViewById(R.id.edit_description);

        mon = (TextView) findViewById(R.id.monday);
        tue = (TextView) findViewById(R.id.tuesday);
        wed = (TextView) findViewById(R.id.wedenesday);
        thu = (TextView) findViewById(R.id.thursday);
        fri = (TextView) findViewById(R.id.friday);
        sat = (TextView) findViewById(R.id.saturday);
        sun = (TextView) findViewById(R.id.sunday);

        takeaway = (CheckBox) findViewById(R.id.takeaway);
        onplace = (CheckBox) findViewById(R.id.onPlace);

        cuisineText = (EditText) findViewById(R.id.edit_cuisine);
        cuisineText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialogType(0);
            }
        });

        week_array = new TextView[]{mon, tue, wed, thu, fri, sat, sun};

        resSwitch = (Switch) findViewById(R.id.reservation);
        wifiSwitch = (Switch) findViewById(R.id.wifi);
        seatsSwitch = (Switch) findViewById(R.id.seats);
        creditSwitch = (Switch) findViewById(R.id.creditcard);
        bancomatSwitch = (Switch) findViewById(R.id.bancomat);
        musicSwitch = (Switch) findViewById(R.id.music);
        parkSwitch = (Switch) findViewById(R.id.parking);
    }

    private void initializePhotoManagement()
    {
        logoPhotoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.logo_fragment);
        this.coversPhotoViewer = new PhotoViewer[4];
        this.coversPhotoViewer[0] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment1);
        this.coversPhotoViewer[1] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment2);
        this.coversPhotoViewer[2] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment3);
        this.coversPhotoViewer[3] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment4);
    }

    private void gerRestaurantByRestaurantId(final String id){
            showProgressBar();

            new Thread() {
                public void run() {

                    FirebaseGetRestaurantProfileManager firebaseGetManProfileManager = new FirebaseGetRestaurantProfileManager();
                    firebaseGetManProfileManager.getRestaurant(id);
                    final boolean timeout = firebaseGetManProfileManager.waitForResult();
                    restaurant = firebaseGetManProfileManager.getResult();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(timeout){
                                Snackbar.make(findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                                        .show();
                            }

                            dismissProgressDialog();

                            setData();
                        }
                    });
                }
            }.start();
    }

    private void saveRestaurant(final Restaurant restaurant){

        if(!validateData()){
            return;
        }

        showProgressBar();

        final Bitmap[] coversThumb = new Bitmap[4];
        final Bitmap[] coversLarge = new Bitmap[4];

        for(int num = 0; num < coversPhotoViewer.length; num++){
            if(coversPhotoViewer[num].getThumb() != null){
                coversThumb[num] = coversPhotoViewer[num].getThumb();
                coversLarge[num] = coversPhotoViewer[num].getLarge();
            }
        }

        setRestaurantInfo();

        Thread t = new Thread() {
            public void run() {
                FirebaseSaveRestaurantProfileManager firebaseSaveManProfileManager = new FirebaseSaveRestaurantProfileManager();
                firebaseSaveManProfileManager.
                        saveRestaurant(restaurant,
                                logoPhotoViewer.getThumb(),
                                coversThumb,
                                coversLarge);
                firebaseSaveManProfileManager.waitForResult();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
                        toast.show();

                        finish();
                    }
                });
            }
        };
        t.start();

    }

    private boolean validateData() {

        String name, address, phone, email, description, tmp;
        String lun = null, mar = null, mer = null, gio = null, ven = null, sab = null, dom = null;

        String[] str_array;

        if(logoPhotoViewer.isImageTobeSetted()){
            printAlert(getResources().getString(R.string.error_complete));
            return false;
        }
        str_array= new String[]{lun, mar, mer, gio, ven, sab, dom};

        for(int i=0; i<7; i++){
            if(week_array[i].getText().toString() == null || week_array[i].getText().toString()== ""){
                printAlert(getResources().getString(R.string.error_complete));
                return false;
            }
            else{
                str_array[i]=week_array[i].getText().toString();
            }

        }

        if (edit_name != null) {
            name = edit_name.getText().toString();
        }
        else {
            //errore
            printAlert(getResources().getString(R.string.exceptionError));
            return false;
        }

        if (edit_address != null) {
            address = edit_address.getText().toString();
        }
        else {
            //errore
            printAlert(getResources().getString(R.string.exceptionError));
            return false;
        }

        if (edit_phone != null) {
            phone = edit_phone.getText().toString();
        }
        else {
            //errore
            printAlert(getResources().getString(R.string.exceptionError));
            return false;
        }

        if (edit_email != null) {
            email = edit_email.getText().toString();
        }
        else {
            //errore
            printAlert(getResources().getString(R.string.exceptionError));
            return false;
        }

        if (edit_description != null) {
            description = edit_description.getText().toString();
        }
        else {
            //errore
            printAlert(getResources().getString(R.string.exceptionError));
            return false;
        }

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || description.isEmpty()){
            //campo vuoto
            printAlert(getResources().getString(R.string.error_complete));
            return false;
        }

        return true;
    }

    private void setRestaurantInfo(){
        if(this.restaurant != null){
            restaurant.setDescription(edit_description.getText().toString());
            restaurant.setRestaurantName(edit_name.getText().toString());
            restaurant.setAddress(edit_address.getText().toString());
            restaurant.setEmail(edit_email.getText().toString());
            restaurant.setPhone(edit_phone.getText().toString());

            ArrayList<String> timeTable = new ArrayList<String>();
            timeTable.add(mon.getText().toString());
            timeTable.add(tue.getText().toString());
            timeTable.add(wed.getText().toString());
            timeTable.add(thu.getText().toString());
            timeTable.add(fri.getText().toString());
            timeTable.add(sat.getText().toString());
            timeTable.add(sun.getText().toString());
            restaurant.setTimeTable(timeTable);

            restaurant.setOnPlace(onplace.isChecked());
            restaurant.setTakeAway(takeaway.isChecked());

            restaurant.setReservations(resSwitch.isChecked());
            restaurant.setBancomat(bancomatSwitch.isChecked());
            restaurant.setCreditCard(creditSwitch.isChecked());
            restaurant.setMusic(musicSwitch.isChecked());
            restaurant.setParking(parkSwitch.isChecked());
            restaurant.setSeatsOutside(seatsSwitch.isChecked());
            restaurant.setWifi(wifiSwitch.isChecked());

            if(logoPhotoViewer.isImageTobeSetted()) {
                restaurant.setLogoThumbDownloadLink(null);
            }

            for(int i = 0; i < 4; i++){
                if(coversPhotoViewer[i].isImageTobeSetted()) {
                    restaurant.setLargeCoverByIndex(i, null);
                    restaurant.setThumbCoverByIndex(i, null);
                }
            }

        }
    }

    private void setData(){
        Restaurant r = this.restaurant;
        if(r == null){
            return;
        }

        if (edit_name != null){ edit_name.setText(r.getRestaurantName()); }
        if (edit_address != null){ edit_address.setText(r.getAddress()); }
        if (edit_phone != null){ edit_phone.setText(r.getPhone()); }
        if (edit_email != null){ edit_email.setText(r.getEmail()); }
        if (edit_description != null){ edit_description.setText(r.getDescription()); }
        if(takeaway!=null){
            takeaway.setChecked(r.isTakeAway());
        }
        if(onplace!=null){
            onplace.setChecked(r.isOnPlace());
        }
        for (int i =0;i<7 ;i++){
            if(week_array[i] !=null ){
                week_array[i].setText(r.getTimeTable().get(i));
            }
        }
        if (resSwitch != null) { resSwitch.setChecked(r.isReservations()); };
        if (wifiSwitch != null) { wifiSwitch.setChecked(r.isWifi()); };
        if (seatsSwitch != null) { seatsSwitch.setChecked(r.isSeatsOutside()); };
        if (creditSwitch != null) { creditSwitch.setChecked(r.isCreditCard()); };
        if (bancomatSwitch != null) { bancomatSwitch.setChecked(r.isBancomat()); };
        if (musicSwitch != null) { musicSwitch.setChecked(r.isMusic()); };
        if (parkSwitch != null) { parkSwitch.setChecked(r.isParking()); };

        if(r.getLogoThumbDownloadLink() != null) {
            this.logoPhotoViewer.setThumbBitmapByURI(r.getLogoThumbDownloadLink());
        }

        if(r.getCover1_thumbDownloadLink() != null) {
            this.coversPhotoViewer[0].setThumbBitmapByURI(r.getCover1_thumbDownloadLink());
            this.coversPhotoViewer[0].setLargePhotoDownloadLink(r.getCover1_largeDownloadLink());
        }

        if(r.getCover2_thumbDownloadLink() != null) {
            this.coversPhotoViewer[1].setThumbBitmapByURI(r.getCover2_thumbDownloadLink());
            this.coversPhotoViewer[1].setLargePhotoDownloadLink(r.getCover2_largeDownloadLink());
        }

        if(r.getCover3_thumbDownloadLink() != null) {
            this.coversPhotoViewer[2].setThumbBitmapByURI(r.getCover3_thumbDownloadLink());
            this.coversPhotoViewer[2].setLargePhotoDownloadLink(r.getCover3_largeDownloadLink());
        }

        if(r.getCover4_thumbDownloadLink() != null) {
            this.coversPhotoViewer[3].setThumbBitmapByURI(r.getCover4_thumbDownloadLink());
            this.coversPhotoViewer[3].setLargePhotoDownloadLink(r.getCover4_largeDownloadLink());
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
                if(this.restaurant != null) {
                    saveRestaurant(this.restaurant);
                }
                break;
            default:
                break;
        }
        return true;
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