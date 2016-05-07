package it.polito.mad_lab3.restaurant.offer_prev;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.Helper;
//import it.polito.mad_lab3.common.HorizontalListView;
import it.polito.mad_lab3.data.restaurant.DishTypeConverter;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.restaurant.menu.MenuListAdapter;
import it.polito.mad_lab3.restaurant.menu_prev.MenuListPrevFragment;

/**
 * Created by f.germano on 04/05/2016.
 */
public class OfferPrevFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int restaurantId;
    private ViewPager viewPager;

    public OfferPrevFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.offer_prev_fragment_layout, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) rootView.findViewById(R.id.offersPrevPager);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void init(int restaurantId){
        setRestaurantId(restaurantId);

        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(Helper.dpToPx(getContext(), 15));
        viewPager.setPadding(Helper.dpToPx(getContext(), 35), 0, Helper.dpToPx(getContext(), 35), 0);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            OfferItemPrevFragment f = OfferItemPrevFragment.newInstance(position, restaurantId );
            return f;
        }

        @Override
        public int getCount() {
            return RestaurantBL.getRestaurantById(getContext(), restaurantId).getOffers().size();
        }
    }

}