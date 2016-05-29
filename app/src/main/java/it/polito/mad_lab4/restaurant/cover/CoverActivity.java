package it.polito.mad_lab4.restaurant.cover;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.CustomViewPager;
import it.polito.mad_lab4.data.restaurant.Cover;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.restaurant.offer_prev.OfferItemPrevFragment;

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

        shapesIndicator[0] = (ImageView)findViewById(R.id.shape1);
        shapesIndicator[1] = (ImageView)findViewById(R.id.shape2);
        shapesIndicator[2] = (ImageView)findViewById(R.id.shape3);
        shapesIndicator[3] = (ImageView)findViewById(R.id.shape4);

        init(getIntent().getIntExtra("restaurantId", -1));

        initIndicator( this.covers.size());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (CustomViewPager) findViewById(R.id.coverPager);
        viewPager.setAdapter(mSectionsPagerAdapter);

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
            CoverFragment f = CoverFragment.newInstance(position, covers.get(position), covers.size());
            return f;
        }

        @Override
        public int getCount() {
            return covers.size();
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
