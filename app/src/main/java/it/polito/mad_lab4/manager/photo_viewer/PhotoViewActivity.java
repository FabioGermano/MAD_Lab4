package it.polito.mad_lab4.manager.photo_viewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import it.polito.mad_lab4.manager.EditableBaseActivity;
import it.polito.mad_lab4.R;

public class PhotoViewActivity extends EditableBaseActivity {

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetSaveButtonVisibility(false);
        SetCalendarButtonVisibility(false);
        SetAlertButtonVisibility(false);
        hideToolbar(true);
        hideShadow(true);

        setContentView(R.layout.activity_photo_view_manager);

        InitializeFABButtons(false, true, false);

        touchImageView = (TouchImageView)findViewById(R.id.photoView);

        getBitmap(getIntent().getExtras());
        setDeleteVisibility(getIntent().getExtras());

    }

    private void setDeleteVisibility(Bundle savedInstanceState)
    {
        boolean editable = savedInstanceState.getBoolean("isEditable");
        SetDeleteFABVisibility(editable);
    }

    private void getBitmap(Bundle savedInstanceState)
    {
        String largePhotoDownloadLink = savedInstanceState.getString("largePhotoDownloadLink");
        String path = savedInstanceState.getString("photoPath");

        if(largePhotoDownloadLink == null && path != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            touchImageView.setImageBitmap(bitmap);
        }
        else if(largePhotoDownloadLink != null){
            Glide.with(this).load(largePhotoDownloadLink).into(touchImageView);
        }
    }

    @Override
    protected void OnSaveButtonPressed() {

    }

    @Override
    protected void OnAlertButtonPressed() {

    }

    @Override
    protected void OnCalendarButtonPressed() {

    }

    @Override
    protected void OnBackButtonPressed() {
        finish();
    }

    @Override
    protected void OnDeleteButtonPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("toBeDeteted",true);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    protected void OnEditButtonPressed() {

    }

    @Override
    protected void OnAddButtonPressed() {

    }
}
