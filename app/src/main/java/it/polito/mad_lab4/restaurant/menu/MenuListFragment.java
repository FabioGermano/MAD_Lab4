package it.polito.mad_lab4.restaurant.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.reservation.food_order.FoodOrderAdapter;

/**
 * Created by f.germano on 03/05/2016.
 */
public class MenuListFragment extends Fragment {

    private int sectionNumber;
    private ListView menuListView;
    private int restaurantId;

    public MenuListFragment(){}

    public static MenuListFragment newInstance(int sectionNumber, int restaurantId) {
        MenuListFragment fragment = new MenuListFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setRestaurantId(restaurantId);
        
        return fragment;
    }

    private void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_list_fragment, container, false);

        ArrayList<Dish> dishes = RestaurantBL.getRestaurantById(getContext(), this.restaurantId).
                getDishesOfCategory(DishTypeConverter.fromIndexToEnum(sectionNumber));

        menuListView = (ListView) rootView.findViewById(R.id.menuListView);

        menuListView.setAdapter(new MenuListAdapter(getContext(), dishes));


        return rootView;
    }

    private void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
}


//menuListView