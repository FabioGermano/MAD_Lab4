package it.polito.mad_lab4.restaurant.menu;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.restaurant.menu_prev.MenuListPrevFragment;

public class MenuActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private int restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setActivityTitle("Menu");
        hideToolbarShadow(true);
        setToolbarColor();

        restaurantId = getIntent().getExtras().getInt("restaurantId");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.menuViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setClipToPadding(false);
        //mViewPager.setPadding(Helper.dpToPx(getBaseContext(), 15), 0, Helper.dpToPx(getBaseContext(), 15), 0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }



    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

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
            return DishTypeConverter.fromEnumToString(DishTypeConverter.fromIndexToEnum(position));
        }
    }
}
