package it.polito.mad_lab3.restaurant.offer_prev;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.DishType;
import it.polito.mad_lab3.data.restaurant.DishTypeConverter;

/**
 * Created by f.germano on 24/04/2016.
 */
public class OfferItemPrevFragment extends Fragment {

    private int restaurantId;
    private int sectionNumber;

    public OfferItemPrevFragment(){

    }

    public static OfferItemPrevFragment newInstance(int sectionNumber, int restaurantId) {
        OfferItemPrevFragment fragment = new OfferItemPrevFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setRestaurantId(restaurantId);

        return fragment;
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

        View rootView = inflater.inflate(R.layout.offer_prev_view, container, false);

        return rootView;
    }
}
