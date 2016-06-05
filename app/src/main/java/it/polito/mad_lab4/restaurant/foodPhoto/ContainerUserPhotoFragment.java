package it.polito.mad_lab4.restaurant.foodPhoto;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.firebase_manager.FirebaseGetUserPhotosManager;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.UserPhoto;
import it.polito.mad_lab4.restaurant.ChoosePhotoActivity;
import it.polito.mad_lab4.restaurant.gallery.PhotoGaleryActivity;

/**
 * Created by f.germano on 27/04/2016.
 */
public class ContainerUserPhotoFragment extends Fragment  {

    UserPhotoFragment[] userPhotoFragments = new UserPhotoFragment[4];
    private final int[] photoIds = {R.id.photo1, R.id.photo2, R.id.photo3, R.id.photo4};

    private Restaurant restaurant;
    private ArrayList<UserPhoto> userPhotos = new ArrayList<>();
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

        availablePhotosTV = (TextView)rootView.findViewById(R.id.availablePhotosTV);

        return rootView;
    }

    private void addPhotoClicked(){
        Intent i = new Intent(getContext(), ChoosePhotoActivity.class);
        Bundle b = new Bundle();
        b.putString("restaurantId", this.restaurant.getRestaurantId());
        i.putExtras(b);
        startActivity(i);
    }

    public void init(final Restaurant restaurant) {
        this.restaurant = restaurant;

        for(int i =0; i<4; i++) {
            userPhotoFragments[i].init(restaurant);
        }

        Thread t = new Thread()
        {
            public void run() {

                FirebaseGetUserPhotosManager firebaseGetUserPhotosManager = new FirebaseGetUserPhotosManager();
                firebaseGetUserPhotosManager.getPhotos(restaurant.getRestaurantId());
                firebaseGetUserPhotosManager.waitForResult();
                userPhotos.clear();
                userPhotos.addAll(firebaseGetUserPhotosManager.getResult());

                if(userPhotos == null){
                    Log.e("FirebaseGetUPMan.", "UserPhotos null obteined");
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPhotosNumber();
                        manageUserPhotos();
                    }
                });

            }
        };

        t.start();
    }

    private void setPhotosNumber(){
        if(this.userPhotos.size() == 0) {
            this.availablePhotosTV.setText(R.string.no_photos);
        }
        else{
            this.availablePhotosTV.setText(this.userPhotos.size()+" "+getResources().getString(R.string.available_photos));
        }
    }

    private void manageUserPhotos(){

        int n_photos = this.userPhotos.size();

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
                userPhotoFragments[i].setUserPhoto(this.userPhotos.get(i));
                userPhotoFragments[i].setImage();
            }
        }

        if(n_photos > 4){
            userPhotoFragments[3].setOpenGalleryOnClick(true);
        }
    }

    public void newPhoto() {
        addPhotoClicked();
    }
}
