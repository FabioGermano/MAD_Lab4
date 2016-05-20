package it.polito.mad_lab4.reservation.food_order;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.reservation.ReservedDish;
import it.polito.mad_lab4.data.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.restaurant.Restaurant;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.reservation.CheckoutOrderActivity;

public class FoodOrderActivity extends BaseActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<ArrayList<ReservedDish>> lists; //order: offer, main, second, dessert , other
    private TextView dateTextView, timeTextView, seatsTextView, nameTextView;
    private String date, time, weekday, restaurantName;
    private int seatsNumber;
    private FloatingActionButton doneFab;
    private int restaurantID=-1;
    private Restaurant restaurant;

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);



        if (isLargeDevice(getApplicationContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                date= null;
                time=null;
                weekday=null;
                restaurantName=null;
                restaurantID=-1;
                seatsNumber=0;


            } else {
                date= extras.getString("date");
                time= extras.getString("time");
                weekday= extras.getString("weekday");
                restaurantName= extras.getString("restaurantName");
                seatsNumber= extras.getInt("seats");
                restaurantID= extras.getInt("restaurantId");
            }
        }

        dateTextView = (TextView) findViewById(R.id.date) ;
        timeTextView = (TextView) findViewById(R.id.time) ;
        seatsTextView = (TextView) findViewById(R.id.seats) ;
        nameTextView = (TextView) findViewById(R.id.restaurant_name);

        if(date!=null && time!=null && restaurantName!=null){
            dateTextView.setText(Helper.formatDate(getApplicationContext(),weekday, date));
            timeTextView.setText(time);
            nameTextView.setText(restaurantName);
        }
        if(seatsNumber!=0){
            seatsTextView.setText(String.valueOf(seatsNumber)+" "+getResources().getString(R.string.seats_string));
        }
        else {
            seatsTextView.setVisibility(View.GONE);
        }

        useToolbar(false);

        restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantID);

        //initialize the lists
        lists = new ArrayList<ArrayList<ReservedDish>>();
        for(int i=0;i<5;i++)
            lists.add(new ArrayList<ReservedDish>());


        //retrieve all dishes
        ArrayList<Dish> main = restaurant.getDishesOfCategory(DishType.MainCourses);
        ArrayList<Dish> second = restaurant.getDishesOfCategory(DishType.SecondCourses);
        ArrayList<Dish> dessert = restaurant.getDishesOfCategory(DishType.Dessert);
        ArrayList<Dish> other = restaurant.getDishesOfCategory(DishType.Other);

        //retrieve all offers
        ArrayList<Offer> offers = restaurant.getOffers();

        //Conversion from Offer to ReservedDish
        for(Offer offer : offers){
            lists.get(0).add(new ReservedDish(offer.getOfferName(), true, 0,offer.getPrice()));
        }

        //Conversion from Dish to ReservedDish
        for(Dish dish : main){
            lists.get(1).add(new ReservedDish(dish.getDishName(), false, 0,dish.getPrice()));
        }
        for(Dish dish : second){
            lists.get(2).add(new ReservedDish(dish.getDishName(), false, 0,dish.getPrice()));
        }
        for(Dish dish : dessert){
            lists.get(3).add(new ReservedDish(dish.getDishName(), false, 0,dish.getPrice()));
        }
        for(Dish dish : other){
            lists.get(4).add(new ReservedDish(dish.getDishName(), false, 0,dish.getPrice()));
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
        doneFab = (FloatingActionButton) findViewById(R.id.done);
        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ReservedDish> reservedDishes= new ArrayList<ReservedDish>();
                for(int j=0;j<5;j++) {
                    for(ReservedDish rd : lists.get(j)){
                        if(rd.getQuantity()>0){
                            reservedDishes.add(rd);
                        }
                    }
                }
                if(reservedDishes.isEmpty()){
                    Toast.makeText(FoodOrderActivity.this, getResources().getString(R.string.no_item_in_order), Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(getApplicationContext(), CheckoutOrderActivity.class);
                    i.putParcelableArrayListExtra("reservedDishes", reservedDishes );
                    /*i.putParcelableArrayListExtra("offers", lists.get(0) );
                    i.putParcelableArrayListExtra("main",lists.get(1) );
                    i.putParcelableArrayListExtra("second", lists.get(2));
                    i.putParcelableArrayListExtra("dessert", lists.get(3) );
                    i.putParcelableArrayListExtra("other",lists.get(4) );*/
                    i.putExtra("date", date);
                    i.putExtra("weekday", weekday);
                    i.putExtra("time", time);
                    i.putExtra("seats", seatsNumber);
                    i.putExtra("restaurantName", restaurantName);
                    i.putExtra("restaurantId", restaurantID);
                    startActivity(i);
                }

            }
        });

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
