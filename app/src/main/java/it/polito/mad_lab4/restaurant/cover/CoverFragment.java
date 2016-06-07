package it.polito.mad_lab4.restaurant.cover;

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

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.common.photo_viewer.TouchImageView;
import it.polito.mad_lab4.common.photo_viewer.TouchImageViewListener;
import it.polito.mad_lab4.data.restaurant.Cover;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.restaurant.ShowOfferActivity;

/**
 * Created by f.germano on 24/04/2016.
 */
public class CoverFragment extends Fragment implements TouchImageViewListener {

    private int restaurantId;
    private int sectionNumber;
    private int size;
    private String cover;

    public CoverFragment(){

    }

    public static CoverFragment newInstance(int sectionNumber, String cover, int size) {
        CoverFragment fragment = new CoverFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setCover(cover);
        fragment.setSize(size);

        return fragment;
    }

    private void setSize(int size) {
        this.size = size;
    }

    private void setCover(String cover) {
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
        Glide.with(this).load(cover).into(touchImageView);
        //touchImageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), Helper.getResourceByName(getContext(), cover, "drawable")));
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
