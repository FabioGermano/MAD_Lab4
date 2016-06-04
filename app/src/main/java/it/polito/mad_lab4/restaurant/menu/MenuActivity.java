package it.polito.mad_lab4.restaurant.menu;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;

public class MenuActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setActivityTitle("Menu");
        hideToolbarShadow(true);
        setToolbarColor();

        restaurantId = getIntent().getExtras().getString("restaurantId");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.menuViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
    }


    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MenuListFragment.newInstance(position, restaurantId);
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
