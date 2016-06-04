package it.polito.mad_lab4.restaurant.menu_prev;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad_lab4.R;

/**
 * Created by f.germano on 24/04/2016.
 */
public class MenuPrevFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String restaurantId;

    public MenuPrevFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.menu_prev_fragment_layout, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.menuPrevPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.menuPrevTabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MenuListPrevFragment f = MenuListPrevFragment.newInstance(position, restaurantId );
            return f;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.first);
                case 1:
                    return getResources().getString(R.string.second);
                case 2:
                    return getResources().getString(R.string.dessert);
                case 3:
                    return getResources().getString(R.string.other);
            }
            return null;
        }
    }
}
