package it.polito.mad_lab3.restaurant.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.UserPhoto;

public class PhotoGaleryActivity extends AppCompatActivity implements PhotoGalleryListener{


    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_galery);

        this.restaurant = RestaurantBL.getRestaurantById(getBaseContext(), getIntent().getExtras().getInt("restaurantId"));

        GridView gridView = (GridView)findViewById(R.id.galleryGridview);

        PhotoGalleryAdapter adapter = new PhotoGalleryAdapter(this);
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
}
