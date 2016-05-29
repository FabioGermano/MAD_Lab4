package it.polito.mad_lab4.restaurant.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.bl.UserBL;
import it.polito.mad_lab4.common.UserSession;
import it.polito.mad_lab4.common.photo_viewer.TouchImageView;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.restaurant.UserPhoto;
import it.polito.mad_lab4.data.user.User;

public class GalleryPhotoViewActivity extends BaseActivity {

    private Restaurant restaurant;
    private TouchImageView touchImageView;
    private TextView galleryPhotoText;
    private ImageButton likeButton;
    private UserPhoto userPhoto;
    private User user;

    private void getUserPhoto(Bundle extras) {
        userPhoto = (UserPhoto)extras.getSerializable("userPhoto");
        galleryPhotoText.setText(userPhoto.getDescription());

        if(UserSession.userId != null) {
            user = UserBL.getUserById(getApplicationContext(), UserSession.userId);

            if (!UserBL.checkUserPhotoLike(user, this.restaurant.getRestaurantId(), userPhoto.getId())) {
                this.likeButton.setColorFilter(Color.WHITE);
            } else {
                this.likeButton.setColorFilter(getResources().getColor(R.color.themeColor));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_photo_view);
        hideToolbar(true);
        hideToolbarShadow(true);

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

        if(UserSession.userId == null){
            likeButton.setVisibility(View.GONE);
        }

        getBitmap(getIntent().getExtras());
        setDeleteVisibility(getIntent().getExtras());

        getUserPhoto(getIntent().getExtras());
    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }



    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void likeButtonPressed() {
        if(!UserBL.checkUserPhotoLike(this.user, this.restaurant.getRestaurantId(), this.userPhoto.getId())) {

            RestaurantBL.addNewLikeToUserPhoto(this.restaurant, this.userPhoto.getId());
            UserBL.addUserPhotoLike(this.user, this.restaurant.getRestaurantId(), this.userPhoto.getId());

            this.likeButton.setColorFilter(getResources().getColor(R.color.themeColor));
        }
        else
        {
            RestaurantBL.removeLikeToUserPhoto(this.restaurant, this.userPhoto.getId());
            UserBL.removeUserPhotoLike(this.user, this.restaurant.getRestaurantId(), this.userPhoto.getId());

            this.likeButton.setColorFilter(Color.WHITE);
        }
    }

    private void getRestaurant(Bundle extras) {
        this.restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), extras.getInt("restaurantId"));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RestaurantBL.saveChanges(getApplicationContext());
        UserBL.saveChanges(getApplicationContext());
    }
}
