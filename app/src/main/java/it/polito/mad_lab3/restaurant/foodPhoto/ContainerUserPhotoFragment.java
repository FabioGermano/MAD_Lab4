package it.polito.mad_lab3.restaurant.foodPhoto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.photo_viewer.PhotoDialog;
import it.polito.mad_lab3.common.photo_viewer.PhotoDialogListener;
import it.polito.mad_lab3.restaurant.ChoosePhotoActivity;

/**
 * Created by f.germano on 27/04/2016.
 */
public class ContainerUserPhotoFragment extends Fragment  {


    UserPhotoFragment[] userPhotoFragments = new UserPhotoFragment[4];
    private final int[] photoIds = {R.id.photo1, R.id.photo2, R.id.photo3, R.id.photo4};

    Button addPhotoButton;

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

        return rootView;
    }

    private void addPhotoClicked(){
        Intent i = new Intent(getContext(), ChoosePhotoActivity.class);
        startActivity(i);
    }
}
