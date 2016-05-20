package it.polito.mad_lab4.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.photo_manager.PhotoManager;
import it.polito.mad_lab4.common.photo_manager.PhotoType;
import it.polito.mad_lab4.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.common.photo_viewer.PhotoViewerListener;
import it.polito.mad_lab4.data.restaurant.UserPhoto;
import it.polito.mad_lab4.data.user.User;

public class ChoosePhotoActivity extends BaseActivity implements PhotoViewerListener{

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager photoManager;
    private PhotoViewer photoViewer;
    private FloatingActionButton sendButton;
    private EditText descriptionET;

    private int restaurantId, newPhotoId;
    private String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(  R.layout.activity_choose_photo);

        setActivityTitle(getResources().getString(R.string.add_userphoto_activity_title));
        setToolbarColor();

        sendButton = (FloatingActionButton)findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButtonClicked();
            }
        });

        descriptionET = (EditText)findViewById(R.id.descriptionET);

        photoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer);
        photoManager = new PhotoManager(getApplicationContext(), PhotoType.USERPHOTOS, this.imageThumb, this.imageLarge);

        restaurantId =  getIntent().getExtras().getInt("restaurantId");
        newPhotoId =  getIntent().getExtras().getInt("newPhotoId");

        ID = String.valueOf(restaurantId) + "_" + String.valueOf(newPhotoId);

    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }

    @Override
    protected void filterButton() {

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void sendButtonClicked() {
        if(!this.photoViewer.isPhotoSetted()){
            Toast.makeText(getApplicationContext(), "No photo choosen, please select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        commitPhotos();
        UserPhoto up = new UserPhoto(this.imageThumb, this.imageLarge, 0, descriptionET.getText().toString(), newPhotoId);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("UserPhoto",(Serializable) up);
        setResult(Activity.RESULT_OK, returnIntent);

        Toast.makeText(getApplicationContext(), "Your selected photo has been sent.", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {
        if (fragmentId == R.id.photoViewer){
            this.photoManager.saveThumb(thumb, ID);
            this.photoManager.saveLarge(large, ID);
        }
    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        if (fragmentId == R.id.photoViewer){
            return BitmapFactory.decodeFile(this.photoManager.getLarge(ID));
        }
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {
        if (fragmentId == R.id.photoViewer){
            this.photoManager.removeThumb(ID);
        }
    }

    private void commitPhotos() {
        this.imageThumb = this.photoManager.commitThumb(ID);
        this.imageLarge = this.photoManager.commitLarge(ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.photoManager.destroy(ID);
    }


}
