package it.polito.mad_lab4.restaurant.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.restaurant.UserPhoto;
import it.polito.mad_lab4.data.user.User;

public class PhotoGaleryActivity extends BaseActivity implements PhotoGalleryListener{

    private GridView gridView;
    private PhotoGalleryAdapter adapter;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_galery);

        hideToolbarShadow(true);
        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.gallery_activity_title));

        this.restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), getIntent().getExtras().getInt("restaurantId"));

        gridView = (GridView)findViewById(R.id.galleryGridview);

        adapter = new PhotoGalleryAdapter(this);
        adapter.setListener(this);

        adapter.setUserPhotos(restaurant.getUserPhotos());

        gridView.setAdapter(adapter);
    }


    @Override
    public void OnPhotoClick(UserPhoto userPHotoClicked) {
        Intent intent = new Intent(this, GalleryPhotoViewActivity.class);
        intent.putExtra("photoPath", userPHotoClicked.getLargePath());
        intent.putExtra("isEditable", false);
        intent.putExtra("userPhoto", userPHotoClicked);
        intent.putExtra("restaurantId", this.restaurant.getRestaurantId());

        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
        gridView.invalidateViews();
    }
}
