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

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.restaurant.UserPhoto;
import it.polito.mad_lab4.restaurant.ChoosePhotoActivity;
import it.polito.mad_lab4.restaurant.gallery.PhotoGaleryActivity;

/**
 * Created by f.germano on 27/04/2016.
 */
public class ContainerUserPhotoFragment extends Fragment  {

    private final int PHOTO_CHOOSE = 9999;

    UserPhotoFragment[] userPhotoFragments = new UserPhotoFragment[4];
    private final int[] photoIds = {R.id.photo1, R.id.photo2, R.id.photo3, R.id.photo4};

    private Button addPhotoButton, testButton;
    private Restaurant restaurant;

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

        /*addPhotoButton = (Button) rootView.findViewById(R.id.addPhotoButton);
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhotoClicked();
            }
        });
        if(UserSession.userId == null){
            addPhotoButton.setVisibility(View.GONE);
        }

        testButton = (Button) rootView.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testButtonClicked();
            }
        });*/

        availablePhotosTV = (TextView)rootView.findViewById(R.id.availablePhotosTV);

        return rootView;
    }

    private void testButtonClicked() {
        Intent i = new Intent(getContext(), PhotoGaleryActivity.class);
        Bundle b = new Bundle();
        b.putInt("restaurantId", this.restaurant.getRestaurantId());
        i.putExtras(b);
        startActivity(i);
    }

    private void addPhotoClicked(){
        Intent i = new Intent(getContext(), ChoosePhotoActivity.class);
        Bundle b = new Bundle();
        b.putInt("restaurantId", this.restaurant.getRestaurantId());
        b.putInt("newPhotoId", RestaurantBL.getNewUserPhotoId(this.restaurant));
        i.putExtras(b);
        startActivityForResult(i, PHOTO_CHOOSE);
    }

    public void init(Restaurant restaurant) {
        this.restaurant = restaurant;

        for(int i =0; i<4; i++) {
            userPhotoFragments[i].init(restaurant);
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
                RestaurantBL.saveChanges(getContext());
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
                userPhotoFragments[i].setUserPhoto(this.restaurant.getUserPhotos().get(i));
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
