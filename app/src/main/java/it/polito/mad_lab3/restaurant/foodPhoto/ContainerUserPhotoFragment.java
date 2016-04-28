package it.polito.mad_lab3.restaurant.foodPhoto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.photo_viewer.PhotoDialog;
import it.polito.mad_lab3.common.photo_viewer.PhotoDialogListener;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.UserPhoto;
import it.polito.mad_lab3.reservation.ReservationActivity;
import it.polito.mad_lab3.restaurant.ChoosePhotoActivity;
import it.polito.mad_lab3.restaurant.gallery.PhotoGaleryActivity;

/**
 * Created by f.germano on 27/04/2016.
 */
public class ContainerUserPhotoFragment extends Fragment  {

    private final int PHOTO_CHOOSE = 9999;

    UserPhotoFragment[] userPhotoFragments = new UserPhotoFragment[4];
    private final int[] photoIds = {R.id.photo1, R.id.photo2, R.id.photo3, R.id.photo4};

    private Button addPhotoButton, testButton;
    private Restaurant restaurant;

    private RestaurantBL restaurantBL;

    private TextView availablePhotosTV;

    public ContainerUserPhotoFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.restaurant_userphoto_fragment, container, false);

        for(int i =0; i<4; i++) {
            userPhotoFragments[i] = (UserPhotoFragment) getChildFragmentManager().findFragmentById(photoIds[i]);
        }

        addPhotoButton = (Button) rootView.findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhotoClicked();
            }
        });

        testButton = (Button) rootView.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testButtonClicked();
            }
        });

        availablePhotosTV = (TextView)rootView.findViewById(R.id.availablePhotosTV);

        return rootView;
    }

    private void testButtonClicked() {
        Intent i = new Intent(getContext(), PhotoGaleryActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("userPhotos", this.restaurant.getUserPhotos());
        i.putExtras(b);
        startActivity(i);
    }

    private void addPhotoClicked(){
        Intent i = new Intent(getContext(), ChoosePhotoActivity.class);
        Bundle b = new Bundle();
        b.putInt("restaurantId", this.restaurant.getRestaurantId());
        b.putInt("newPhotoId", this.restaurantBL.getNewUserPhotoId(this.restaurant));
        i.putExtras(b);
        startActivityForResult(i, PHOTO_CHOOSE);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        for(int i =0; i<4; i++) {
            userPhotoFragments[i].setRestaurant(restaurant);
        }

        setPhotosNumber(restaurant);
        manageUserPhotos();
    }

    private void setPhotosNumber(Restaurant restaurant){
        if(this.restaurant.getUserPhotos().size() == 0) {
            this.availablePhotosTV.setText("No photos until now");
        }
        else{
            this.availablePhotosTV.setText(this.restaurant.getUserPhotos().size()+" available photos");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PHOTO_CHOOSE) {
            if(resultCode == Activity.RESULT_OK){
                UserPhoto pu = (UserPhoto)data.getSerializableExtra("UserPhoto");
                this.restaurant.getUserPhotos().add(pu);
                setPhotosNumber(this.restaurant);
                manageUserPhotos();
                this.restaurantBL.saveChanges();
            }
        }
    }

    private void manageUserPhotos(){

        int n_photos = this.restaurant.getUserPhotos().size();

        try {
            if(n_photos > 4) {
                userPhotoFragments[3].setRemainingNumber(n_photos-4);
                userPhotoFragments[3].setLatestLabel();
            }
        } catch (Exception e) {
            Log.d(e.getMessage(), e.getMessage());
        }

        for(int i = 0; i<4; i++){
            if(i < n_photos){
                userPhotoFragments[i].setImage(BitmapFactory.decodeFile(this.restaurant.getUserPhotos().get(i).getThumbPath()));
            }
        }

        if(n_photos > 4){
            userPhotoFragments[3].setOpenGalleryOnClick(true);
        }
    }

    public void setRestaurantBL(RestaurantBL restaurantBL){
        this.restaurantBL = restaurantBL;
    }
}
