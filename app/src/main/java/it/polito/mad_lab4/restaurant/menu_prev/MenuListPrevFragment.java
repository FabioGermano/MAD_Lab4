package it.polito.mad_lab4.restaurant.menu_prev;

import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.firebase_manager.FirebaseGetMenuByTypeManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;

/**
 * Created by f.germano on 24/04/2016.
 */
public class MenuListPrevFragment extends Fragment {

    private String restaurantId;
    private int sectionNumber;
    private LinearLayout parentLinearLayout;

    public MenuListPrevFragment(){

    }

    public static MenuListPrevFragment newInstance(int sectionNumber, String restaurantId) {
        MenuListPrevFragment fragment = new MenuListPrevFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setRestaurantId(restaurantId);

        return fragment;
    }

    private void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    private void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    @Override
    public void onResume() {
        super.onResume();

        final DishType dishType = DishTypeConverter.fromIndexToEnum(this.sectionNumber);
        final ArrayList<Dish> dishesOfType = new ArrayList<>();

        Thread t = new Thread()
        {
            public void run() {

                FirebaseGetMenuByTypeManager firebaseGetMenuByTypeManager = new FirebaseGetMenuByTypeManager();
                firebaseGetMenuByTypeManager.getMenu(restaurantId, dishType, 3);
                firebaseGetMenuByTypeManager.waitForResult();
                dishesOfType.addAll(firebaseGetMenuByTypeManager.getResult());

                if(dishesOfType == null){
                    Log.e("returned null dishes", "resturned null dishes");
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setData(dishesOfType);
                    }
                });

            }
        };

        t.start();
    }

    private void setData(ArrayList<Dish> dishesOfType) {

        parentLinearLayout.removeAllViews();

        for(Dish d : dishesOfType){

                View viewToAdd = LayoutInflater.from(getContext()).inflate(R.layout.menu_list_prev_detail, null);
                ((TextView)viewToAdd.findViewById(R.id.dishNameTV)).setText(d.getDishName());
                ((RatingBar)viewToAdd.findViewById(R.id.ratingBar)).setRating(d.getAvgRank());
                ((TextView)viewToAdd.findViewById(R.id.numRanksTV)).setText("(" + String.valueOf(d.getNumRanks()) + ")");
                ((TextView)viewToAdd.findViewById(R.id.dishPriceTV)).setText(String.valueOf(d.getPrice())+"€");

                if(d.getThumbDownloadLink() != null){
                    Glide.with(this).load(d.getThumbDownloadLink()).into((ImageView)viewToAdd.findViewById(R.id.dishPhotoIV));
                }

                Helper.setRatingBarColor(getContext(),
                        (RatingBar)viewToAdd.findViewById(R.id.ratingBar),
                        d.getAvgRank());

                parentLinearLayout.addView(viewToAdd);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_list_prev_fragment, container, false);
        parentLinearLayout = (LinearLayout)rootView.findViewById(R.id.menu_list_prev_parent_layout);

        return rootView;
    }
}
