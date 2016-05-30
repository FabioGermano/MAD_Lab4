package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.polito.mad_lab4.*;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class EditRestaurantProfile extends it.polito.mad_lab4.BaseActivity implements ValueEventListener, DatabaseReference.CompletionListener {

    private String logoThumbPath;
    private String[] coversThumbPath, coversLargePath;
    private PhotoViewer logoPhotoViewer;
    private PhotoViewer[] coversPhotoViewer;
    private Restaurant restaurant;

    private final String id_logo_photo = "logo_photo";
    private final String[] ids_cover_photo = new String[]{
            "cover_photo_1",
            "cover_photo_2",
            "cover_photo_3",
            "cover_photo_4"
    };

    // Views
    private EditText edit_name;
    private EditText edit_phone;
    private EditText edit_address;
    private EditText edit_email;
    private EditText edit_description;
    private Button lunBtn;
    private Button marBtn;
    private Button merBtn;
    private Button gioBtn;
    private Button venBtn;
    private Button sabBtn;
    private Button domBtn;
    private Switch resSwitch;
    private Switch wifiSwitch;
    private Switch seatsSwitch;
    private Switch creditSwitch;
    private Switch bancomatSwitch;
    private Switch musicSwitch;
    private Switch parkSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_edit_restaurant_profile);
        getViews();

        setToolbarColor();
        setVisibilitySave(true);
        invalidateOptionsMenu();
        setActivityTitle(getResources().getString(R.string.title_activity_edit_restaurant_profile));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        this.coversThumbPath = new String[4];
        this.coversLargePath = new String[4];

        gerRestaurantByRestaurantId("-KIcTNUVIT-BIqARHq3P");

        initializePhotoManagement();
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void getViews() {
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_description = (EditText) findViewById(R.id.edit_description);

        lunBtn = (Button) findViewById(R.id.monday_butt);
        marBtn = (Button) findViewById(R.id.tuesday_butt);
        merBtn = (Button) findViewById(R.id.wednesday_butt);
        gioBtn = (Button) findViewById(R.id.thursday_butt);
        venBtn = (Button) findViewById(R.id.friday_butt);
        sabBtn = (Button) findViewById(R.id.saturday_butt);
        domBtn = (Button) findViewById(R.id.sunday_butt);

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
        Thread t = new Thread() {
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("restaurants/" + id);
                myRef.addValueEventListener(EditRestaurantProfile.this);
            }
        };
        t.start();
    }

    private void saveRestaurant(final Restaurant restaurant){
        Thread t = new Thread() {
            public void run() {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("restaurants/" + restaurant.getRestaurantId());
                setRestaurantInfo(restaurant);
                myRef.setValue(restaurant, EditRestaurantProfile.this);
            }
        };
        t.start();

    }

    private void setRestaurantInfo(Restaurant restaurant){
        if(this.restaurant != null){
            restaurant.setDescription(edit_description.getText().toString());
            restaurant.setRestaurantName(edit_name.getText().toString());
            restaurant.setAddress(edit_address.getText().toString());
            restaurant.setEmail(edit_email.getText().toString());
            restaurant.setPhone(edit_phone.getText().toString());

            ArrayList<String> timeTable = new ArrayList<String>();
            timeTable.add(lunBtn.getText().toString());
            timeTable.add(marBtn.getText().toString());
            timeTable.add(merBtn.getText().toString());
            timeTable.add(gioBtn.getText().toString());
            timeTable.add(venBtn.getText().toString());
            timeTable.add(sabBtn.getText().toString());
            timeTable.add(domBtn.getText().toString());
            restaurant.setTimeTable(timeTable);

            restaurant.setReservations(resSwitch.isChecked());
            restaurant.setBancomat(bancomatSwitch.isChecked());
            restaurant.setCreditCard(creditSwitch.isChecked());
            restaurant.setMusic(musicSwitch.isChecked());
            restaurant.setParking(parkSwitch.isChecked());
            restaurant.setSeatsOutside(seatsSwitch.isChecked());
            restaurant.setWifi(wifiSwitch.isChecked());
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Restaurant r = dataSnapshot.getValue(Restaurant.class);
        if(r != null){

            this.restaurant = r;

            if (edit_name != null){ edit_name.setText(r.getRestaurantName()); }
            if (edit_address != null){ edit_address.setText(r.getAddress()); }
            if (edit_phone != null){ edit_phone.setText(r.getPhone()); }
            if (edit_email != null){ edit_email.setText(r.getEmail()); }
            if (edit_description != null){ edit_description.setText(r.getDescription()); }

            if (lunBtn != null){ lunBtn.setText(r.getTimeTable().get(0)); }
            if (marBtn != null){ marBtn.setText(r.getTimeTable().get(1)); }
            if (merBtn != null){ merBtn.setText(r.getTimeTable().get(2)); }
            if (gioBtn != null){ gioBtn.setText(r.getTimeTable().get(3)); }
            if (venBtn != null){ venBtn.setText(r.getTimeTable().get(4)); }
            if (sabBtn != null){ sabBtn.setText(r.getTimeTable().get(5)); }
            if (domBtn != null){ domBtn.setText(r.getTimeTable().get(6)); }

            if (resSwitch != null) { resSwitch.setChecked(r.isReservations()); };
            if (wifiSwitch != null) { wifiSwitch.setChecked(r.isWifi()); };
            if (seatsSwitch != null) { seatsSwitch.setChecked(r.isSeatsOutside()); };
            if (creditSwitch != null) { creditSwitch.setChecked(r.isCreditCard()); };
            if (bancomatSwitch != null) { bancomatSwitch.setChecked(r.isBancomat()); };
            if (musicSwitch != null) { musicSwitch.setChecked(r.isMusic()); };
            if (parkSwitch != null) { parkSwitch.setChecked(r.isParking()); };
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if(databaseError == null) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), MainActivityManager.class);
            startActivity(intent);

            finish();
        }
    }
}