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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.firebase_manager.FirebaseSaveUserPhotoManager;
import it.polito.mad_lab4.manager.photo_viewer.PhotoViewer;
import it.polito.mad_lab4.newData.restaurant.UserPhoto;
import it.polito.mad_lab4.data.user.User;

public class ChoosePhotoActivity extends BaseActivity {


    private PhotoViewer photoViewer;
    private FloatingActionButton sendButton;
    private EditText descriptionET;

    private String restaurantId;
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

        restaurantId =  getIntent().getExtras().getString("restaurantId");
    }


    private void sendButtonClicked() {
        if(this.photoViewer.isImageTobeSetted()){
            Toast.makeText(getApplicationContext(), "No photo choosen, please select a photo", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread()
        {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                    }
                });

                FirebaseSaveUserPhotoManager firebaseSaveDishManager = new FirebaseSaveUserPhotoManager();
                firebaseSaveDishManager.saveUserPhoto(restaurantId, descriptionET.getText().toString(), photoViewer.getThumb(), photoViewer.getLarge());

                boolean res = firebaseSaveDishManager.waitForResult();

                if(!res){
                    Log.e("Error saving the userp.", "Error saving the userphoto");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dismissProgressDialog();

                        Toast.makeText(getApplicationContext(), "Your selected photo has been sent.", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                });

            }
        }.start();
    }

}
