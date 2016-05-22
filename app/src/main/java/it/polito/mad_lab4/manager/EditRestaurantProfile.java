package it.polito.mad_lab4.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.manager.common.PhotoManager;
import it.polito.mad_lab4.manager.common.PhotoType;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewerListener;

public class EditRestaurantProfile extends BaseActivity implements PhotoViewerListener {

    private String logoThumbPath;
    private String[] coversThumbPath, coversLargePath;
    private PhotoManager logoManager;
    private PhotoManager[] coverManagers;
    private PhotoViewer logoPhotoViewer;
    private PhotoViewer[] coversPhotoViewer;

    private final String id_logo_photo = "logo_photo";
    private final String[] ids_cover_photo = new String[]{
            "cover_photo_1",
            "cover_photo_2",
            "cover_photo_3",
            "cover_photo_4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(true);
        SetCalendarButtonVisibility(false);

        setContentView(R.layout.activity_edit_restaurant_profile);

        setToolbarColor();

        setTitleTextView(getResources().getString(R.string.title_activity_edit_restaurant_profile));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        // Construct the data source


        this.coversThumbPath = new String[4];
        this.coversLargePath = new String[4];

        readData();

        initializePhotoManagement();
    }

    private void initializePhotoManagement()
    {
        logoPhotoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.logo_fragment);
        this.coversPhotoViewer = new PhotoViewer[4];
        this.coversPhotoViewer[0] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment1);
        this.coversPhotoViewer[1] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment2);
        this.coversPhotoViewer[2] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment3);
        this.coversPhotoViewer[3] = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.cover_fragment4);

        logoManager = new PhotoManager(getApplicationContext(), PhotoType.PROFILE, this.logoThumbPath, null);
        coverManagers = new PhotoManager[4];
        for(int i = 0; i<4; i++) {
            coverManagers[i] = new PhotoManager(getApplicationContext(), PhotoType.PROFILE, this.coversThumbPath[i], this.coversLargePath[i]);
        }

        logoPhotoViewer.setThumbBitmap(BitmapFactory.decodeFile(logoManager.getThumb(id_logo_photo)));
        for(int i = 0; i<4; i++) {
            this.coversPhotoViewer[i].setThumbBitmap(BitmapFactory.decodeFile(coverManagers[i].getThumb(ids_cover_photo[i])));
        }
    }

    private void readData(){
        try{
            GestioneDB DB = new GestioneDB();
            String profilo = DB.leggiDB(this, "db_profilo");

            JSONObject jsonRootObject = new JSONObject(profilo);

            JSONArray jsonArray = jsonRootObject.optJSONArray("profilo");

            JSONObject info = jsonArray.getJSONObject(0);
            JSONObject orari = jsonArray.getJSONObject(1);
            JSONObject logo = jsonArray.getJSONObject(2);
            JSONObject covers = jsonArray.getJSONObject(3);
            JSONObject features = jsonArray.getJSONObject(4);


            String name = info.optString("nome");
            String address = info.optString("indirizzo");
            String phone = info.optString("telefono");
            String email = info.optString("email");
            String description = info.optString("descrizione");

            // read logo path
            this.logoThumbPath = logo.optString("Logo_thumb");
            if(this.logoThumbPath.compareTo("null") == 0){
                this.logoThumbPath = null;
            }
            //this.logoLargePath = jsonObject.optString("logoLargePath");
            //if(this.logoLargePath.equals("")){
            //    this.logoLargePath = null;
            //}

            // read cover path
            for(int i = 0; i<4; i++){
                this.coversThumbPath[i] = covers.optString("coversThumbPath_"+i);
                if(this.coversThumbPath[i].equals("null")){
                    this.coversThumbPath[i] = null;
                }
            }
            for(int i = 0; i<4; i++){
                this.coversLargePath[i] = covers.optString("coversLargePath_"+i);
                if(this.coversLargePath[i].equals("null")){
                    this.coversLargePath[i] = null;
                }
            }

            String lun = getResources().getString(R.string.monday).toUpperCase();
            if (orari.optString("lun").compareTo("null") != 0){
                lun += "\n" + orari.optString("lun");
            }
            String mar = getResources().getString(R.string.tuesday).toUpperCase();
            if (orari.optString("mar").compareTo("null") != 0){
                mar += "\n" + orari.optString("mar");
            }
            String mer = getResources().getString(R.string.wednesday).toUpperCase();
            if (orari.optString("mer").compareTo("null") != 0){
                mer += "\n" + orari.optString("mer");
            }
            String gio = getResources().getString(R.string.thursday).toUpperCase();
            if (orari.optString("gio").compareTo("null") != 0){
                gio += "\n" + orari.optString("gio");
            }
            String ven = getResources().getString(R.string.friday).toUpperCase();
            if (orari.optString("ven").compareTo("null") != 0){
                ven += "\n" + orari.optString("ven");
            }
            String sab = getResources().getString(R.string.saturday).toUpperCase();
            if (orari.optString("sab").compareTo("null") != 0){
                sab += "\n" + orari.optString("sab");
            }
            String dom = getResources().getString(R.string.sunday).toUpperCase();
            if (orari.optString("dom").compareTo("null") != 0){
                dom += "\n" + orari.optString("dom");
            }

            boolean res = features.optBoolean("reservations");
            boolean wifi = features.optBoolean("wifi");
            boolean seats = features.optBoolean("seats");
            boolean card = features.optBoolean("creditCard");
            boolean bancomat = features.optBoolean("bancomat");
            boolean music = features.optBoolean("music");
            boolean park = features.optBoolean("parking");


            EditText edit_name = (EditText) findViewById(R.id.edit_name);
            EditText edit_address = (EditText) findViewById(R.id.edit_address);
            EditText edit_phone = (EditText) findViewById(R.id.edit_phone);
            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            EditText edit_description = (EditText) findViewById(R.id.edit_description);

            Button lunBtn = (Button) findViewById(R.id.monday_butt);
            Button marBtn = (Button) findViewById(R.id.tuesday_butt);
            Button merBtn = (Button) findViewById(R.id.wednesday_butt);
            Button gioBtn = (Button) findViewById(R.id.thursday_butt);
            Button venBtn = (Button) findViewById(R.id.friday_butt);
            Button sabBtn = (Button) findViewById(R.id.saturday_butt);
            Button domBtn = (Button) findViewById(R.id.sunday_butt);

            Switch resSwitch = (Switch) findViewById(R.id.reservation);
            Switch wifiSwitch = (Switch) findViewById(R.id.wifi);
            Switch seatsSwitch = (Switch) findViewById(R.id.seats);
            Switch creditSwitch = (Switch) findViewById(R.id.creditcard);
            Switch bancomatSwitch = (Switch) findViewById(R.id.bancomat);
            Switch musicSwitch = (Switch) findViewById(R.id.music);
            Switch parkSwitch = (Switch) findViewById(R.id.parking);




            if (edit_name != null){ edit_name.setText(name); }
            if (edit_address != null){ edit_address.setText(address); }
            if (edit_phone != null){ edit_phone.setText(phone); }
            if (edit_email != null){ edit_email.setText(email); }
            if (edit_description != null){ edit_description.setText(description); }

            if (lunBtn != null){ lunBtn.setText(lun); }
            if (marBtn != null){ marBtn.setText(mar); }
            if (merBtn != null){ merBtn.setText(mer); }
            if (gioBtn != null){ gioBtn.setText(gio); }
            if (venBtn != null){ venBtn.setText(ven); }
            if (sabBtn != null){ sabBtn.setText(sab); }
            if (domBtn != null){ domBtn.setText(dom); }

            if (resSwitch != null) { resSwitch.setChecked(res); };
            if (wifiSwitch != null) { wifiSwitch.setChecked(wifi); };
            if (seatsSwitch != null) { seatsSwitch.setChecked(seats); };
            if (creditSwitch != null) { creditSwitch.setChecked(card); };
            if (bancomatSwitch != null) { bancomatSwitch.setChecked(bancomat); };
            if (musicSwitch != null) { musicSwitch.setChecked(music); };
            if (parkSwitch != null) { parkSwitch.setChecked(park); };


        }
        catch(Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
            System.out.println("Eccezione: "+e.getMessage());
        }
    }

    private boolean saveInfo(){

        try {
            String name, address, phone, email, description, tmp;
            String lun, mar, mer, gio, ven, sab, dom;

            EditText edit_name = (EditText) findViewById(R.id.edit_name);
            EditText edit_address = (EditText) findViewById(R.id.edit_address);
            EditText edit_phone = (EditText) findViewById(R.id.edit_phone);
            EditText edit_email = (EditText) findViewById(R.id.edit_email);
            EditText edit_description = (EditText) findViewById(R.id.edit_description);

            Button lunBtn = (Button) findViewById(R.id.monday_butt);
            Button marBtn = (Button) findViewById(R.id.tuesday_butt);
            Button merBtn = (Button) findViewById(R.id.wednesday_butt);
            Button gioBtn = (Button) findViewById(R.id.thursday_butt);
            Button venBtn = (Button) findViewById(R.id.friday_butt);
            Button sabBtn = (Button) findViewById(R.id.saturday_butt);
            Button domBtn = (Button) findViewById(R.id.sunday_butt);

            Switch resSwitch = (Switch) findViewById(R.id.reservation);
            Switch wifiSwitch = (Switch) findViewById(R.id.wifi);
            Switch seatsSwitch = (Switch) findViewById(R.id.seats);
            Switch creditSwitch = (Switch) findViewById(R.id.creditcard);
            Switch bancomatSwitch = (Switch) findViewById(R.id.bancomat);
            Switch musicSwitch = (Switch) findViewById(R.id.music);
            Switch parkSwitch = (Switch) findViewById(R.id.parking);


            /* ##################################
                 Lettura campi dalla schermata
               ##################################
             */
            if (lunBtn != null){
                tmp = lunBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.monday).toUpperCase()) == 0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    lun = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (marBtn != null){
                tmp = marBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.tuesday).toUpperCase()) == 0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    mar = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }


            if (merBtn != null) {
                tmp = merBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.wednesday).toUpperCase()) == 0) {
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                } else
                    mer = tmp.substring(tmp.indexOf("\n") + 1, tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (gioBtn != null){
                tmp = gioBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.thursday).toUpperCase()) == 0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    gio = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (venBtn != null){
                tmp = venBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.friday).toUpperCase()) == 0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    ven = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (sabBtn != null){
                tmp = sabBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.saturday).toUpperCase()) == 0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    sab = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (domBtn != null){
                tmp = domBtn.getText().toString();
                if (tmp.compareTo(getResources().getString(R.string.sunday).toUpperCase()) == 0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                else
                    dom = tmp.substring(tmp.indexOf("\n")+1,tmp.length());
            }
            else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
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

            /* ##################################
                    Gestione Database
               ##################################
             */
            GestioneDB DB = new GestioneDB();
            String profilo = DB.leggiDB(this, "db_profilo");

            JSONObject jsonRootObject = new JSONObject(profilo);
            JSONArray jsonArray = jsonRootObject.getJSONArray("profilo");
            JSONObject info = jsonArray.getJSONObject(0);
            JSONObject orari = jsonArray.getJSONObject(1);
            JSONObject logo = jsonArray.getJSONObject(2);
            JSONObject covers = jsonArray.getJSONObject(3);
            JSONObject features = jsonArray.getJSONObject(4);

            info.put("nome", name);
            info.put("indirizzo", address);
            info.put("telefono", phone);
            info.put("email", email);
            info.put("descrizione", description);

            //save logo paths
            if(this.logoThumbPath != null)
            {
                logo.put("Logo_thumb", this.logoThumbPath);
            }
            // if(this.logoLargePath != null)
            //{
            //    jsonObject.put("logoLargePath", this.logoLargePath);
            //}

            //save cover path
            for(int i = 0; i<4; i++){
                if(this.coversThumbPath[i] != null) {
                    covers.put("coversThumbPath_" + i, this.coversThumbPath[i]);
                }
            }
            for(int i = 0; i<4; i++){
                if(this.coversLargePath[i] != null) {
                    covers.put("coversLargePath_" + i, this.coversLargePath[i]);
                }
            }

            orari.put("lun", lun);
            orari.put("mar", mar);
            orari.put("mer", mer);
            orari.put("gio", gio);
            orari.put("ven", ven);
            orari.put("sab", sab);
            orari.put("dom", dom);
            features.put("reservations", resSwitch.isChecked());
            features.put("wifi", wifiSwitch.isChecked());
            features.put("seats", seatsSwitch.isChecked());
            features.put("creditCard", creditSwitch.isChecked());
            features.put("bancomat", bancomatSwitch.isChecked());
            features.put("music", musicSwitch.isChecked());
            features.put("parking", parkSwitch.isChecked());


            DB.updateDB(this, jsonRootObject, "db_profilo");
            return true;

        }
        catch (Exception e){
            //errore
            printAlert(getResources().getString(R.string.exceptionError));
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
        this.logoThumbPath = this.logoManager.commitThumb(id_logo_photo);
        //this.logoLargePath = this.photoManager.commitLarge(id_logo_photo);

        for(int i = 0; i<4; i++){
            this.coversThumbPath[i] = this.coverManagers[i].commitThumb(ids_cover_photo[i]);
            this.coversLargePath[i] = this.coverManagers[i].commitLarge(ids_cover_photo[i]);
        }
    }

    @Override
    protected void OnSaveButtonPressed() {

        commitPhotos();
        boolean ris = saveInfo();

        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            this.logoManager.destroy(id_logo_photo);
            for(int i = 0; i < 4; i++){
                this.coverManagers[i].destroy(ids_cover_photo[i]);
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void OnAlertButtonPressed() {

    }

    @Override
    protected void OnCalendarButtonPressed() {

    }

    @Override
    protected void OnBackButtonPressed() {

    }

    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {
        if(fragmentId == R.id.logo_fragment)
        {
            this.logoManager.saveThumb(thumb, id_logo_photo);
            //this.photoManager.saveLarge(large, id_logo_photo);
        }
        else if(fragmentId == R.id.cover_fragment1)
        {
            this.coverManagers[0].saveThumb(thumb, ids_cover_photo[0]);
            this.coverManagers[0].saveLarge(large, ids_cover_photo[0]);
        }
        else if(fragmentId == R.id.cover_fragment2)
        {
            this.coverManagers[1].saveThumb(thumb, ids_cover_photo[1]);
            this.coverManagers[1].saveLarge(large, ids_cover_photo[1]);
        }
        else if(fragmentId == R.id.cover_fragment3)
        {
            this.coverManagers[2].saveThumb(thumb, ids_cover_photo[2]);
            this.coverManagers[2].saveLarge(large, ids_cover_photo[2]);
        }
        else if(fragmentId == R.id.cover_fragment4)
        {
            this.coverManagers[3].saveThumb(thumb, ids_cover_photo[3]);
            this.coverManagers[3].saveLarge(large, ids_cover_photo[3]);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        //if(fragmentId == R.id.logo_fragment) {
        //    return BitmapFactory.decodeFile(this.photoManager.getLarge(id_logo_photo));
        //}
        if(fragmentId == R.id.cover_fragment1)
        {
            return BitmapFactory.decodeFile(this.coverManagers[0].getLarge(ids_cover_photo[0]));
        }
        else if(fragmentId == R.id.cover_fragment2)
        {
            return BitmapFactory.decodeFile(this.coverManagers[1].getLarge(ids_cover_photo[1]));
        }
        else if(fragmentId == R.id.cover_fragment3)
        {
            return BitmapFactory.decodeFile(this.coverManagers[2].getLarge(ids_cover_photo[2]));
        }
        else if(fragmentId == R.id.cover_fragment4)
        {
            return BitmapFactory.decodeFile(this.coverManagers[3].getLarge(ids_cover_photo[3]));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if(fragmentId == R.id.logo_fragment) {
            this.logoManager.removeThumb(id_logo_photo);
        }
        else if(fragmentId == R.id.cover_fragment1)
        {
            this.coverManagers[0].removeThumb(ids_cover_photo[0]);
        }
        else if(fragmentId == R.id.cover_fragment2)
        {
            this.coverManagers[1].removeThumb(ids_cover_photo[1]);
        }
        else if(fragmentId == R.id.cover_fragment3)
        {
            this.coverManagers[2].removeThumb(ids_cover_photo[2]);
        }
        else if(fragmentId == R.id.cover_fragment4)
        {
            this.coverManagers[3].removeThumb(ids_cover_photo[3]);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.logoManager.destroy(id_logo_photo);
        for(int i = 0; i < 4; i++){
            this.coverManagers[i].destroy(ids_cover_photo[i]);
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