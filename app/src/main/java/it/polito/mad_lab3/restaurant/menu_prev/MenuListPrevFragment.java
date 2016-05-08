package it.polito.mad_lab3.restaurant.menu_prev;

import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.DishType;
import it.polito.mad_lab3.data.restaurant.DishTypeConverter;

/**
 * Created by f.germano on 24/04/2016.
 */
public class MenuListPrevFragment extends Fragment {

    private int restaurantId;
    private int sectionNumber;

    public MenuListPrevFragment(){

    }

    public static MenuListPrevFragment newInstance(int sectionNumber, int restaurantId) {
        MenuListPrevFragment fragment = new MenuListPrevFragment();
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

        View rootView = inflater.inflate(R.layout.menu_list_prev_fragment, container, false);
        LinearLayout parentLinearLayout = (LinearLayout)rootView.findViewById(R.id.menu_list_prev_parent_layout);

        DishType dishType = DishTypeConverter.fromIndexToEnum(this.sectionNumber);

        ArrayList<Dish> dishesOfType = RestaurantBL.getRestaurantById(getContext(), this.restaurantId).getDishesOfCategory(dishType);

        int max = 3, i = 0;
        for(Dish d : dishesOfType){
            if(i<3) {
                View viewToAdd = LayoutInflater.from(getContext()).inflate(R.layout.menu_list_prev_detail, null);
                ((TextView)viewToAdd.findViewById(R.id.dishNameTV)).setText(d.getDishName());
                ((RatingBar)viewToAdd.findViewById(R.id.ratingBar)).setRating(d.getAvgRank());
                ((TextView)viewToAdd.findViewById(R.id.numRanksTV)).setText("(" + String.valueOf(d.getNumRanks()) + ")");
                ((TextView)viewToAdd.findViewById(R.id.dishPriceTV)).setText(String.valueOf(d.getPrice())+"€");

                if(d.getThumbPath() != null) {
                    ((ImageView)viewToAdd.findViewById(R.id.dishPhotoIV)).setImageBitmap(BitmapFactory.decodeFile(d.getThumbPath()));
                }
                else if(d.getResPhoto() != null) {
                    int imgRes = Helper.getResourceByName(getContext(), d.getResPhoto(), "drawable");
                    if (imgRes != 0) {
                        ((ImageView)viewToAdd.findViewById(R.id.dishPhotoIV)).setImageResource(imgRes);
                    }
                }

                Helper.setRatingBarColor(getContext(),
                        (RatingBar)viewToAdd.findViewById(R.id.ratingBar),
                        d.getAvgRank());

                parentLinearLayout.addView(viewToAdd);
            }
            else
            {
                //TextView other = new TextView(getContext());
                //other.setText(getResources().getString(R.string.otherDishesAvailable));
                //parentLinearLayout.addView(other);

                /*
                View viewToAdd = LayoutInflater.from(getContext()).inflate(R.layout.menu_list_prev_detail, null);
                ((TextView)viewToAdd.findViewById(R.id.dishNameTV)).setText(d.getDishName());
                ((RatingBar)viewToAdd.findViewById(R.id.ratingBar)).setRating(d.getAvgRank());
                ((TextView)viewToAdd.findViewById(R.id.numRanksTV)).setText("(" + String.valueOf(d.getNumRanks()) + ")");
                ((TextView)viewToAdd.findViewById(R.id.dishPriceTV)).setText(String.valueOf(d.getPrice())+"€");
                ((ImageView)viewToAdd.findViewById(R.id.menu_prev_latest_over_IV)).setVisibility(View.VISIBLE);
                if(d.getThumbPath() != null){
                    ((ImageView)viewToAdd.findViewById(R.id.dishPhotoIV)).setImageBitmap(BitmapFactory.decodeFile(d.getThumbPath()));
                }

                parentLinearLayout.addView(viewToAdd);
                break;
                */
            }
            i++;
        }

        return rootView;
    }
}
