package it.polito.mad_lab4.restaurant.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.firebase_manager.FirebaseGetMenuByTypeManager;

/**
 * Created by f.germano on 03/05/2016.
 */
public class MenuListFragment extends Fragment {

    private int sectionNumber;
    private ListView menuListView;
    private String restaurantId;

    public MenuListFragment(){}

    public static MenuListFragment newInstance(int sectionNumber, String restaurantId) {
        MenuListFragment fragment = new MenuListFragment();
        fragment.setSectionNumber(sectionNumber);
        fragment.setRestaurantId(restaurantId);
        
        return fragment;
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
                firebaseGetMenuByTypeManager.getMenu(restaurantId, dishType, null);
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
        menuListView.setAdapter(new MenuListAdapter(getContext(), dishesOfType));
    }

    private void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_list_fragment, container, false);

        menuListView = (ListView) rootView.findViewById(R.id.menuListView);

        return rootView;
    }

    private void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
}
