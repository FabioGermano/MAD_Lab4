package it.polito.mad_lab3.common.photo_viewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import it.polito.mad_lab3.R;

public class PhotoViewActivity  extends AppCompatActivity {

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);

        touchImageView = (TouchImageView)findViewById(R.id.photoView);

        getBitmap(getIntent().getExtras());
        setDeleteVisibility(getIntent().getExtras());

    }

    private void setDeleteVisibility(Bundle savedInstanceState)
    {
        boolean editable = savedInstanceState.getBoolean("isEditable");
    }

    private void getBitmap(Bundle savedInstanceState)
    {
        String path = savedInstanceState.getString("photoPath");

        if(path != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            touchImageView.setImageBitmap(bitmap);
        }
    }
}