package it.polito.mad_lab4.restaurant.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.UserPhoto;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUserPhotosManager;

public class PhotoGaleryActivity extends BaseActivity implements PhotoGalleryListener{

    private GridView gridView;
    private PhotoGalleryAdapter adapter;
    private String restaurantId;
    private ArrayList<UserPhoto> userPhotos = new ArrayList<>();
    private FirebaseGetUserPhotosManager firebaseGetUserPhotosManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_galery);

        hideToolbarShadow(true);
        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.gallery_activity_title));

        this.restaurantId = getIntent().getExtras().getString("restaurantId");

        gridView = (GridView)findViewById(R.id.galleryGridview);

        adapter = new PhotoGalleryAdapter(this);
        adapter.setListener(this);
        adapter.setContext(getApplicationContext());
    }



    @Override
    public void OnPhotoClick(UserPhoto userPHotoClicked) {

        Intent intent = new Intent(getApplicationContext(), GalleryPhotoViewActivity.class);
        intent.putExtra("isEditable", false);
        intent.putExtra("userPhoto", userPHotoClicked);
        intent.putExtra("restaurantId", restaurantId);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread t = new Thread()
        {
            public void run() {

                firebaseGetUserPhotosManager = new FirebaseGetUserPhotosManager();
                firebaseGetUserPhotosManager.getPhotos(restaurantId);
                firebaseGetUserPhotosManager.waitForResult();
                userPhotos.clear();
                userPhotos.addAll(firebaseGetUserPhotosManager.getResult());

                if(userPhotos == null){
                    Log.e("FirebaseGetUPMan.", "UserPhotos null obteined");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setUserPhotos(userPhotos);
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        gridView.invalidateViews();
                    }
                });

            }
        };

        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firebaseGetUserPhotosManager.terminate();
    }
}
