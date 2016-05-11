package it.polito.mad_lab3.restaurant.cover;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.common.photo_viewer.TouchImageView;
import it.polito.mad_lab3.common.photo_viewer.TouchImageViewListener;
import it.polito.mad_lab3.data.restaurant.Cover;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.restaurant.ShowOfferActivity;

/**
 * Created by f.germano on 24/04/2016.
 */
public class CoverFragment extends Fragment implements TouchImageViewListener {

    private int restaurantId;
    private int sectionNumber;
    private Cover cover;

    public CoverFragment(){

    }

    public static CoverFragment newInstance(int sectionNumber, Cover cover) {
        CoverFragment fragment = new CoverFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setCover(cover);

        return fragment;
    }

    private void setCover(Cover cover) {
        this.cover = cover;
    }

    private void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    private void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_photo_view, container, false);

        TouchImageView touchImageView = (TouchImageView)rootView.findViewById(R.id.photoView);
        touchImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), Helper.getResourceByName(getContext(), cover.getResPhoto(), "drawable")));
        touchImageView.setListener(this);

        return rootView;
    }

    @Override
    public void OnScaleOutListener() {
        ((CoverActivity)getActivity()).viewPager.setPagingEnabled(true);
    }

    @Override
    public void OnScaleListener() {
        ((CoverActivity)getActivity()).viewPager.setPagingEnabled(false);
    }
}
