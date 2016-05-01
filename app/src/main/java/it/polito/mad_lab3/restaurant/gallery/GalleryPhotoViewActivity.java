package it.polito.mad_lab3.restaurant.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.bl.UserBL;
import it.polito.mad_lab3.common.photo_viewer.TouchImageView;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.UserPhoto;
import it.polito.mad_lab3.data.user.User;

public class GalleryPhotoViewActivity extends AppCompatActivity {

    private Restaurant restaurant;
    private TouchImageView touchImageView;
    private TextView galleryPhotoText;
    private ImageButton likeButton;
    private UserPhoto userPhoto;
    private User user;

    private void getUserPhoto(Bundle extras) {
        userPhoto = (UserPhoto)extras.getSerializable("userPhoto");
        galleryPhotoText.setText(userPhoto.getDescription());

        user = UserBL.getUserById(getBaseContext(), 1);

        if(!UserBL.checkUserPhotoLike(getBaseContext(), user, this.restaurant.getRestaurantId(), userPhoto.getId())){}
            //this.likeButton.setBackground(getDrawable(R.drawable.cibo1));
        else{}
            //this.likeButton.setBackground(getDrawable(R.drawable.cibo2));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_photo_view);

        getRestaurant(getIntent().getExtras());

        touchImageView = (TouchImageView)findViewById(R.id.photoView);
        galleryPhotoText = (TextView)findViewById(R.id.galleryPhotoText);
        likeButton = (ImageButton)findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButtonPressed();
            }
        });

        getBitmap(getIntent().getExtras());
        setDeleteVisibility(getIntent().getExtras());

        getUserPhoto(getIntent().getExtras());
    }

    private void likeButtonPressed() {
        RestaurantBL.addNewLikeToUserPhoto(this.restaurant, this.userPhoto.getId());
        UserBL.addUserPhotoLike(this.user, this.restaurant.getRestaurantId(), this.userPhoto.getId());

        RestaurantBL.saveChanges(getBaseContext());
        UserBL.saveChanges(getBaseContext());
    }

    private void getRestaurant(Bundle extras) {
        this.restaurant = RestaurantBL.getRestaurantById(getBaseContext(), extras.getInt("restaurantId"));
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
