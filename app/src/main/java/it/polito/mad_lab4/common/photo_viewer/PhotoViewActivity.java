package it.polito.mad_lab4.common.photo_viewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.user.User;

public class PhotoViewActivity  extends BaseActivity {

    private TouchImageView touchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);
        hideToolbar(true);
        hideToolbarShadow(true);

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

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
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