package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.restaurant.menu_prev.MenuListPrevFragment;
import it.polito.mad_lab3.restaurant.menu_prev.MenuPrevFragment;
import ivb.com.materialstepper.stepperFragment;

/**
 * Created by Giovanna on 23/04/2016.
 */
public class DishesFragment extends stepperFragment {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public DishesFragment(){
    }

    @Override
    public boolean onNextButtonHandler() {
        return false;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dishes_fragment, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MenuListFragment.newInstance(position+1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "uno";
                case 1:
                    return "due";
                case 2:
                    return "tre";
            }
            return null;
        }
    }
}
