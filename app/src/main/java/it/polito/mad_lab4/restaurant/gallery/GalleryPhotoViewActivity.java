package it.polito.mad_lab4.restaurant.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.photo_viewer.TouchImageView;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseUserPhotoLikeManager;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.UserPhoto;
import it.polito.mad_lab4.data.user.User;

public class GalleryPhotoViewActivity extends BaseActivity implements ChildEventListener {

    private String restaurantId;
    private TouchImageView touchImageView;
    private TextView galleryPhotoText;
    private ImageButton likeButton;
    private UserPhoto userPhoto;
    private FirebaseUser currentUser;
    private boolean currentUserLikedPhoto;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery_photo_view);
        hideToolbar(true);
        hideToolbarShadow(true);
        setVisibilityAlert(false);
        invalidateOptionsMenu();

        touchImageView = (TouchImageView) findViewById(R.id.photoView);
        galleryPhotoText = (TextView) findViewById(R.id.galleryPhotoText);
        likeButton = (ImageButton) findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeButtonPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        userPhoto = (UserPhoto)extras.getSerializable("userPhoto");
        galleryPhotoText.setText(userPhoto.getDescription());

        restaurantId = extras.getString("restaurantId");

        Glide.with(this).load(userPhoto.getLargeDownloadLink()).into(touchImageView);

        likeButton.setVisibility(View.GONE);
        if(!extras.getBoolean("isManager") ) {
            new Thread() {
                public void run() {
                    FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                    firebaseGetAuthInformation.waitForResult();
                    currentUser = firebaseGetAuthInformation.getUser();

                    if (currentUser != null) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                likeButton.setVisibility(View.VISIBLE);
                            }
                        });

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        myRef = database.getReference("userphotolikes/" + userPhoto.getUserPhotoId() + "/" + currentUser.getUid());
                        myRef.addChildEventListener(GalleryPhotoViewActivity.this);
                    }
                }
            }.start();
        }
    }

    private void likeButtonPressed() {
        FirebaseUserPhotoLikeManager firebaseUserPhotoLikeManager = new FirebaseUserPhotoLikeManager();
        if(currentUserLikedPhoto){
            firebaseUserPhotoLikeManager.removeLike(userPhoto.getUserPhotoId(), currentUser.getUid(), restaurantId);
        }
        else
        {
            firebaseUserPhotoLikeManager.saveLike(userPhoto.getUserPhotoId(), currentUser.getUid(), restaurantId);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        likeButton.setColorFilter(getResources().getColor(R.color.themeColor));
        currentUserLikedPhoto = true;
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        likeButton.setColorFilter(getResources().getColor(R.color.themeColor));
        currentUserLikedPhoto = true;
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        likeButton.setColorFilter(Color.WHITE);
        currentUserLikedPhoto = false;
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(myRef != null){
            myRef.removeEventListener(this);
        }
    }
}
