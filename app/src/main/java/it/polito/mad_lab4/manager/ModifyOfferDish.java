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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveDishManager;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveOfferManager;
import it.polito.mad_lab4.firebase_manager.FirebaseUpdateAvgDishesAndOffers;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferManager;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;

/**
 * Created by Euge on 10/04/2016.
 */
public class ModifyOfferDish extends EditableBaseActivity {

    private Offer offer = new Offer();
    private ArrayList<Offer> offer_list = null;

    private int position = -1;
    private boolean newOffer = false;
    private String restaurantId, offerId;

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoViewer imageViewer;
    private String id_image;

    private FirebaseSaveOfferManager firebaseSaveOfferManager;
    private FirebaseGetOfferManager firebaseGetOfferManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_modify_offer);
        setVisibilityAlert(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        if(readData()){
            setActivityTitle(getResources().getString(R.string.title_activity_new_offer));
        }
        else{
            setActivityTitle(getResources().getString(R.string.title_activity_edit_offer));
        }
        setToolbarColor();
        setVisibilitySave(true);
        invalidateOptionsMenu();

        imageViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.imageOffer_modifyOffer);
    }


    private boolean readData(){ //return false if editing
        try {
            Bundle extras = getIntent().getExtras();

                restaurantId = extras.getString("restaurantId");
                boolean isEditing = extras.getBoolean("isEditing");
                offerId = extras.getString("offerId");

                    if (!isEditing){
                        //setTitleTextView(getResources().getString(R.string.title_activity_new_offer));
                        newOffer = true;
                        extras.clear();
                        return true;
                    }
                    else{
                        //setTitleTextView(getResources().getString(R.string.title_activity_edit_offer));

                        getOfferOnFirebase();
                    }

        } catch (Exception e){
            System.out.print("Eccezione: " + e.getMessage());
        }
        return false;
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

    private void getOfferOnFirebase() {
        new Thread()
        {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                firebaseGetOfferManager = new FirebaseGetOfferManager();
                firebaseGetOfferManager.getOffer(restaurantId, offerId);
                firebaseGetOfferManager.waitForResult();
                offer = firebaseGetOfferManager.getResult();

                if(offer == null){
                    Log.e("returned null offer", "resturned null offer");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setOffer();
                        dismissProgressDialog();
                    }
                });

            }
        }.start();
    }

    private void setOffer(){
        EditText editName = (EditText) findViewById(R.id.edit_offerName_modifyOffer);
        EditText editPrice = (EditText) findViewById(R.id.edit_offerPrice_modifyOffer);
        EditText editNotes = (EditText) findViewById(R.id.edit_offerNote_modifyOffer);

        ToggleButton lunBtn = (ToggleButton) findViewById(R.id.lun_Button);
        ToggleButton marBtn = (ToggleButton) findViewById(R.id.mar_Button);
        ToggleButton merBtn = (ToggleButton) findViewById(R.id.mer_Button);
        ToggleButton gioBtn = (ToggleButton) findViewById(R.id.gio_Button);
        ToggleButton venBtn = (ToggleButton) findViewById(R.id.ven_Button);
        ToggleButton sabBtn = (ToggleButton) findViewById(R.id.sab_Button);
        ToggleButton domBtn = (ToggleButton) findViewById(R.id.dom_Button);


        if (editName != null) {
            editName.setText(offer.getOfferName());
        }

        if (editPrice != null) {
            BigDecimal result;
            result= Helper.round(offer.getPrice(),2);
            editPrice.setText(String.valueOf(result));
        }

        if (editNotes != null){
            editNotes.setText(offer.getDetails());
        }

        if (lunBtn != null){ lunBtn.setChecked(offer.getAvailableOn().get(0)); }
        if (marBtn != null){ marBtn.setChecked(offer.getAvailableOn().get(1)); }
        if (merBtn != null){ merBtn.setChecked(offer.getAvailableOn().get(2)); }
        if (gioBtn != null){ gioBtn.setChecked(offer.getAvailableOn().get(3)); }
        if (venBtn != null){ venBtn.setChecked(offer.getAvailableOn().get(4)); }
        if (sabBtn != null){ sabBtn.setChecked(offer.getAvailableOn().get(5)); }
        if (domBtn != null){ domBtn.setChecked(offer.getAvailableOn().get(6)); }

        if(offer.getThumbDownloadLink() != null) {
            this.imageViewer.setThumbBitmapByURI(offer.getThumbDownloadLink());
            this.imageViewer.setLargePhotoDownloadLink(offer.getLargeDownloadLink());
        }
    }

    private boolean saveInfo(){
        try {
            EditText editName = (EditText) findViewById(R.id.edit_offerName_modifyOffer);
            EditText editPrice = (EditText) findViewById(R.id.edit_offerPrice_modifyOffer);
            EditText editNotes = (EditText) findViewById(R.id.edit_offerNote_modifyOffer);
            ToggleButton lunBtn = (ToggleButton) findViewById(R.id.lun_Button);
            ToggleButton marBtn = (ToggleButton) findViewById(R.id.mar_Button);
            ToggleButton merBtn = (ToggleButton) findViewById(R.id.mer_Button);
            ToggleButton gioBtn = (ToggleButton) findViewById(R.id.gio_Button);
            ToggleButton venBtn = (ToggleButton) findViewById(R.id.ven_Button);
            ToggleButton sabBtn = (ToggleButton) findViewById(R.id.sab_Button);
            ToggleButton domBtn = (ToggleButton) findViewById(R.id.dom_Button);

            String nomeO;
            float priceO;
            String notesO;

            if (editName != null) {
                nomeO = editName.getText().toString();
                if(nomeO.compareTo("")==0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
            } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (editPrice != null) {
                String price =  editPrice.getText().toString();
                if (price.compareTo("") != 0) {
                    priceO = Float.parseFloat(price);

                }
                else {
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

            if (editNotes != null) {
                notesO = editNotes.getText().toString();
                if(notesO.compareTo("")==0) {
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
            } else {
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            ArrayList<Boolean> days = new ArrayList<>();

            if (lunBtn != null){ days.add(lunBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (marBtn != null){ days.add(marBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (merBtn != null){ days.add(merBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (gioBtn != null){ days.add(gioBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (venBtn != null){ days.add(venBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (sabBtn != null){ days.add(sabBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (domBtn != null){ days.add(domBtn.isChecked()); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            offer.setOfferName(nomeO);

            if (!newOffer)
                offer.setLastPrice(offer.getPrice());

            offer.setPrice(priceO);
            offer.setDetails(notesO);

            offer.setAvailableOn(days);
            //offer.setThumbPath(imageThumb);
            //offer.setLargePath(imageLarge);

            saveOffer(restaurantId, newOffer);

            return true;

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            printAlert(getResources().getString(R.string.exceptionError));

            return false;
        }
    }

    private void saveOffer(final String restaurantId, final boolean newOffer) {
        new Thread()
        {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                firebaseSaveOfferManager = new FirebaseSaveOfferManager();
                firebaseSaveOfferManager.saveOffer(restaurantId,
                        newOffer,
                        offer,
                        !imageViewer.isImageTobeSetted(),
                        imageViewer.getThumb(),
                        imageViewer.getLarge());

                final boolean res = firebaseSaveOfferManager.waitForResult();

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
                boolean updateRequired = updateAvgDishesAndOffers.updateAvgDishesAndOffers(restaurantId, offer.getPrice(), offer.getLastPrice(), newOffer);
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
    protected void OnDeleteButtonPressed() {

    }

    @Override
    protected void OnEditButtonPressed() {

    }

    @Override
    protected void OnAddButtonPressed() {

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
