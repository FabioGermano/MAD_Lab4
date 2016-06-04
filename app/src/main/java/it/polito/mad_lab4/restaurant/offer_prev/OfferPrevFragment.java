package it.polito.mad_lab4.restaurant.offer_prev;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
//import it.polito.mad_lab4.common.HorizontalListView;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.firebase_manager.FirebaseGetMenuByTypeManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferListManager;
import it.polito.mad_lab4.restaurant.menu.MenuListAdapter;
import it.polito.mad_lab4.restaurant.menu_prev.MenuListPrevFragment;

/**
 * Created by f.germano on 04/05/2016.
 */
public class OfferPrevFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String restaurantId;
    private ViewPager viewPager;
    private ArrayList<Offer> offers = new ArrayList<Offer>();

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

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void init(final String restaurantId){
        setRestaurantId(restaurantId);

        Thread t = new Thread()
        {
            public void run() {

                FirebaseGetOfferListManager firebaseGetOfferListManager = new FirebaseGetOfferListManager();
                firebaseGetOfferListManager.getOffers(restaurantId);
                firebaseGetOfferListManager.waitForResult();
                offers.addAll(firebaseGetOfferListManager.getResult());

                if(offers == null){
                    Log.e("returned null dishes", "resturned null dishes");
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setAdapter(mSectionsPagerAdapter);
                        viewPager.setClipToPadding(false);
                        viewPager.setPageMargin(Helper.dpToPx(getContext(), 15));
                        viewPager.setPadding(Helper.dpToPx(getContext(), 35), 0, Helper.dpToPx(getContext(), 35), 0);
                    }
                });

            }
        };

        t.start();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            OfferItemPrevFragment f = OfferItemPrevFragment.newInstance(position, offers );
            return f;
        }

        @Override
        public int getCount() {
            return offers.size();
        }
    }

}