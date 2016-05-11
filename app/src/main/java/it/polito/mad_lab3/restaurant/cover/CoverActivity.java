package it.polito.mad_lab3.restaurant.cover;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.CustomViewPager;
import it.polito.mad_lab3.data.restaurant.Cover;
import it.polito.mad_lab3.data.user.User;
import it.polito.mad_lab3.restaurant.offer_prev.OfferItemPrevFragment;

public class CoverActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private int restaurantId;
    public CustomViewPager viewPager;
    private ImageView[] shapesIndicator = new ImageView[4];
    private ArrayList<Cover> covers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);

        hideToolbar(true);
        hideToolbarShadow(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (CustomViewPager) findViewById(R.id.coverPager);
        viewPager.setAdapter(mSectionsPagerAdapter);

        shapesIndicator[0] = (ImageView)findViewById(R.id.shape1);
        shapesIndicator[1] = (ImageView)findViewById(R.id.shape2);
        shapesIndicator[2] = (ImageView)findViewById(R.id.shape3);
        shapesIndicator[3] = (ImageView)findViewById(R.id.shape4);

        initIndicator(3);

        viewPager.setPagingEnabled(true);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                enableIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        init(1);
    }

    public void init(int restaurantId) {
        this.restaurantId = restaurantId;
        this.covers = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId)
                .getBasicInfo()
                .getCovers();
    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }

    @Override
    protected void filterButton() {

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CoverFragment f = CoverFragment.newInstance(position, covers.get(position) );
            return f;
        }

        @Override
        public int getCount() {
            return 3;//RestaurantBL.getRestaurantById(getContext(), restaurantId).getOffers().size();
        }

    }

    private void enableIndicator(int position) {
        shapesIndicator[position].setImageResource(R.drawable.circular_white_background);
        for(int i = 0; i<4; i++){
            if(i != position){
                shapesIndicator[i].setImageResource(R.drawable.circular_white_background_border);
            }
        }
    }

    private void initIndicator(int count) {
        for(int i = count; i<4; i++){
                shapesIndicator[i].setVisibility(View.GONE);
        }
    }
}
