package it.polito.mad_lab3.restaurant.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.UserPhoto;

public class PhotoGaleryActivity extends AppCompatActivity implements PhotoGalleryListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_galery);

        GridView gridView = (GridView)findViewById(R.id.galleryGridview);

        PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(this);
        adapter.setListener(this);

        adapter.setUserPhotos((ArrayList< UserPhoto >)getIntent().getExtras().getSerializable("userPhotos"));
        gridView.setAdapter(adapter);
    }

    @Override
    public void OnPhotoClick(UserPhoto userPHotoClicked) {
        Intent intent = new Intent(this, it.polito.mad_lab3.common.photo_viewer.PhotoViewActivity.class);
        intent.putExtra("photoPath", userPHotoClicked.getLargePath());
        intent.putExtra("isEditable", false);

        startActivity(intent);

    }
}
