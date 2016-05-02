package it.polito.mad_lab3.reservation.food_order;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.data.reservation.Dish;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.reservation.CheckoutOrder;

public class FoodOrderActivity extends BaseActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<ArrayList<Dish>> lists = new ArrayList<>();
    private TextView dateTextView, timeTextView, seatsTextView, totTextView;
    private String date, time, weekday;
    private int seatsNumber;
    private FloatingActionButton doneFab;
    private int restaurantID=-1;
    private Restaurant restaurant;
    //CollapsingToolbarLayout collapsingToolbarLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);

        restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), 1);

        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);
        //collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle("Order now");
        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                date= null;
                time=null;
                weekday=null;
                restaurantID=-1;
                seatsNumber=0;

            } else {
                date= extras.getString("date");
                time= extras.getString("time");
                weekday= extras.getString("weekday");
                seatsNumber= extras.getInt("seats");
                restaurantID= extras.getInt("restaurant");
            }
        } else {
            date= (String) savedInstanceState.getSerializable("date");
            time= (String) savedInstanceState.getSerializable("time");
            weekday= (String) savedInstanceState.getSerializable("weekday");
            seatsNumber= (Integer) savedInstanceState.getSerializable("seats");

        }


        totTextView = (TextView) findViewById(R.id.totale) ;
        dateTextView = (TextView) findViewById(R.id.date) ;
        timeTextView = (TextView) findViewById(R.id.time) ;
        seatsTextView = (TextView) findViewById(R.id.seats) ;

        if(date!=null && time!=null){
            dateTextView.setText(Helper.formatDate(getBaseContext(),weekday, date));
            timeTextView.setText(time);
        }
        if(seatsNumber!=0){
            seatsTextView.setText(String.valueOf(seatsNumber)+" "+getResources().getString(R.string.seats_string));
            //collapsingToolbarLayout.setTitle("Order for eating in");
        }
        else {
            seatsTextView.setVisibility(View.GONE);
            //collapsingToolbarLayout.setTitle("Order for take-away");
        }
        useToolbar(false);

        ArrayList<Dish> offers = new ArrayList<>();
        ArrayList<Dish> main = new ArrayList<>();
        ArrayList<Dish> second = new ArrayList<>();
        ArrayList<Dish> dessert = new ArrayList<>();
        ArrayList<Dish> others = new ArrayList<>();


        //Dish(String dishName, int quantity, float price, String type, boolean isOffer)
        main.add(new Dish("Pizza", 0 , 5, "main", false));
        main.add(new Dish("Pasta", 0 , 8, "main", false));
        second.add(new Dish("Pollo", 0 , 8.5f , "second", false));

        lists.add(offers);
        lists.add(main);
        lists.add(second);
        lists.add(dessert);
        lists.add(others);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
        doneFab = (FloatingActionButton) findViewById(R.id.done);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), CheckoutOrder.class);
                i.putParcelableArrayListExtra("offers", (ArrayList<? extends Parcelable>) lists.get(0));
                i.putParcelableArrayListExtra("main", (ArrayList<? extends Parcelable>) lists.get(1));
                i.putParcelableArrayListExtra("second", (ArrayList<? extends Parcelable>) lists.get(2));
                i.putParcelableArrayListExtra("dessert", (ArrayList<? extends Parcelable>) lists.get(3));
                i.putParcelableArrayListExtra("other", (ArrayList<? extends Parcelable>) lists.get(4));
                i.putExtra("date", date);
                i.putExtra("weekday", weekday);
                i.putExtra("time", time);
                i.putExtra("seats", seatsNumber);
                i.putExtra("restaurant", restaurantID);
                startActivity(i);

                /*for(ArrayList<Dish> list : lists){
                    for(Dish d : list){
                        Log.w("debug", d.getDishName()+" "+d.getType()+" "+d.getPrice()+" q: "+d.getQuantity());
                    }
                }*/
            }
        });

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

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
