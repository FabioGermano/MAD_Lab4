package it.polito.mad_lab4.manager.photo_viewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.manager.EditableBaseActivity;
import it.polito.mad_lab4.R;

public class PhotoViewActivity extends EditableBaseActivity {

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_manager);

        hideToolbar(true);
        hideToolbarShadow(true);
        setVisibilityAlert(false);
        invalidateOptionsMenu();
        InitializeFABButtons(false, true, false);

        touchImageView = (TouchImageView)findViewById(R.id.photoView);

        getBitmap(getIntent().getExtras());
        setDeleteVisibility(getIntent().getExtras());

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

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
