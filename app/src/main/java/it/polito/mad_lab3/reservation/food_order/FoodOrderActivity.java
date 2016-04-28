package it.polito.mad_lab3.reservation.food_order;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.Dish;

public class FoodOrderActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<ArrayList<Dish>> lists = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        ArrayList<Dish> offers = new ArrayList<>();
        ArrayList<Dish> main = new ArrayList<>();
        ArrayList<Dish> second = new ArrayList<>();
        ArrayList<Dish> dessert = new ArrayList<>();
        ArrayList<Dish> others = new ArrayList<>();

        main.add(new Dish("Pizza", 0,0,5, null, null, null));
        main.add(new Dish("Menu kebab", 0,0,6 ,null, null, null));

        second.add(new Dish("Pollo", 0,0, 5,null, null, null));

        lists.add(offers);
        lists.add(main);
        lists.add(second);
        lists.add(dessert);
        lists.add(others);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);


    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Context context;

        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            return ListOrderFragment.newInstance(position, getBaseContext(), lists.get(position));
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return getResources().getString(R.string.offers);
                case 1:
                    return getResources().getString(R.string.first);
                case 2:
                    return getResources().getString(R.string.second);
                case 3:
                    return getResources().getString(R.string.dessert);
                case 4:
                    return getResources().getString(R.string.other);
            }
            return null;
        }
    }
}
