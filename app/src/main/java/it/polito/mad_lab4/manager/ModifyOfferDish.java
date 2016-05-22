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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.restaurant.RestaurantEntity;
import it.polito.mad_lab4.manager.common.PhotoManager;
import it.polito.mad_lab4.manager.common.PhotoType;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewerListener;

/**
 * Created by Euge on 10/04/2016.
 */
public class ModifyOfferDish extends EditableBaseActivity implements PhotoViewerListener{

    private Offer offer = null;
    private ArrayList<Offer> offer_list = null;

    private int position = -1;
    private boolean newOffer = false;
    private int restaurantId, offerId;

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager imageManager;
    private PhotoViewer imageViewer;
    private String id_image;

    private int newID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetCalendarButtonVisibility(false);
        SetSaveButtonVisibility(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();

        readData();
        setToolbarColor();

        initializePhotoManagement();
    }


    private void initializePhotoManagement()
    {
        imageViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.imageOffer_modifyOffer);
        imageManager = new PhotoManager(getApplicationContext(), PhotoType.OFFER, this.imageThumb, this.imageLarge);

        id_image = "image_"+ offer.getOfferId();

        imageViewer.setThumbBitmap(BitmapFactory.decodeFile(imageManager.getThumb(id_image)));
    }

    private void readData(){
        try {
            boolean error = false;
            //recupero l'offerta da modificare
            Bundle extras = getIntent().getExtras();

            if (extras == null){
                //ERRORE! Da verificare
                this.finish();
            }
            else {

                restaurantId = extras.getInt("restaurantId");
                newOffer = !extras.getBoolean("isEditing");

                    if (newOffer){
                        //è una nuova offerta --> AGGIUNTA
                        /*
                            settaggio dinamico del titolo
                         */
                        setContentView(R.layout.activity_modify_offer);
                        setTitleTextView(getResources().getString(R.string.title_activity_new_offer));
                        offer = new Offer();
                        offer.setOfferId(RestaurantBL.getNewOfferId(RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId)));
                        extras.clear();
                        return;
                    }
                    else{
                        //è una modifica
                        /*
                            settaggio dinamico del titolo
                         */

                        setContentView(R.layout.activity_modify_offer);
                        setTitleTextView(getResources().getString(R.string.title_activity_edit_offer));

                        int offerId = extras.getInt("offerId");
                        extras.clear();

                            offer = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId).getOfferById(offerId);

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
                                editPrice.setText(String.valueOf(offer.getPrice()));
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

                            imageThumb = offer.getThumbPath();
                            imageLarge = offer.getLargePath();

                    }
            }
        } catch (Exception e){
            System.out.print("Eccezione: " + e.getMessage());
        }
    }


    private boolean saveInfo(){

        // aggiorno l'oggetto offerta con tutte le nuove informazioni e passo indietro, all'activity di modifica offerta principale, l'intera lista
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

            /*
                piccola modifica per integrità codice, l'oggetto lo modifico solo se ho letto
                correttamente tutti i campi, senza nessun errore.
                Altrimenti, teoricamente, posso riempire alcuni campi si e altri no (nulla di che)
             */
            String nomeO;
            int priceO;
            String notesO;

            /* ##################################
                 Lettura campi dalla schermata
               ##################################
             */
            if (editName != null) {
                nomeO = editName.getText().toString();
                if(nomeO.compareTo("")==0){
                    //campo vuoto
                    printAlert(getResources().getString(R.string.error_complete));
                    return false;
                }
                //else
                    //offer.setName(text);
            } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            if (editPrice != null) {
                String price =  editPrice.getText().toString();
                if (price.compareTo("") != 0) {
                    priceO = Integer.parseInt(price);
                    //offer.setCost(cost);
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
                //else
                    //offer.setNote(notes);
            } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            boolean[] days = new boolean[7];

            if (lunBtn != null){ days[0] = lunBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (marBtn != null){ days[1] = marBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (merBtn != null){ days[2] = merBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (gioBtn != null){ days[3] = gioBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (venBtn != null){ days[4] = venBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (sabBtn != null){ days[5] = sabBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }
            if (domBtn != null){ days[6] = domBtn.isChecked(); } else {
                //errore
                printAlert(getResources().getString(R.string.exceptionError));
                return false;
            }

            offer.setOfferName(nomeO);
            offer.setPrice(priceO);
            offer.setDetails(notesO);

            offer.setAvailableOn(days);
            offer.setThumbPath(imageThumb);
            offer.setLargePath(imageLarge);

            if(newOffer){
                RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId).getOffers().add(offer);
            }

            RestaurantBL.saveChanges(getApplicationContext());
            return true;

        } catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
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

    @Override
    protected void OnDeleteButtonPressed() {

    }

    @Override
    protected void OnEditButtonPressed() {

    }

    @Override
    protected void OnAddButtonPressed() {

    }

    private void commitPhotos() {
        this.imageThumb = this.imageManager.commitThumb(id_image);
        this.imageLarge = this.imageManager.commitLarge(id_image);
    }

    @Override
    protected void OnSaveButtonPressed() {

        commitPhotos();
        boolean ris = saveInfo();
        if(ris) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT);
            toast.show();

            this.imageManager.destroy(id_image);

            Bundle b = new Bundle();
            b.putSerializable("offer_list", offer_list);
            Intent intent = new Intent(getApplicationContext(), GestioneOfferte.class);
            intent.putExtras(b);
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
        if (fragmentId == R.id.imageOffer_modifyOffer){
            this.imageManager.saveThumb(thumb, id_image);
            this.imageManager.saveLarge(large, id_image);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        if (fragmentId == R.id.imageOffer_modifyOffer){
            return BitmapFactory.decodeFile(this.imageManager.getLarge(id_image));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if (fragmentId == R.id.imageOffer_modifyOffer){
            this.imageManager.removeThumb(id_image);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imageManager.destroy(id_image);
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
