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

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.DishTypeConverter;
import it.polito.mad_lab4.firebase_manager.FirebaseGetMenuByTypeManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetOfferListManager;
import it.polito.mad_lab4.newData.reservation.ReservedDish;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.newData.restaurant.Offer;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.reservation.CheckoutOrderActivity;

public class FoodOrderActivity extends BaseActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArrayList<ArrayList<ReservedDish>> lists; //order: offer, main, second, dessert , other
    private TextView dateTextView, timeTextView, seatsTextView, nameTextView;
    private String date, time, weekday, restaurantName;
    private int seatsNumber;
    private FloatingActionButton doneFab;
    private String restaurantID, restaurantAddress;
    private Restaurant restaurant;
    private FirebaseGetMenuByTypeManager firebaseGetMenuByTypeManager;
    private FirebaseGetOfferListManager firebaseGetOfferListManager;
    private String currentUserId;

    private  ReservedDish newReservedDish(Dish dish) {
        //TODO Reserved Dish ID ??
        ReservedDish rd = new ReservedDish();
        rd.setName(dish.getDishName());
        rd.setIsOffer(false);
        rd.setPrice(dish.getPrice());
        rd.setQuantity(0);
        return rd;
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

        setVisibilityAlert(false);
        invalidateOptionsMenu();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                date= null;
                time=null;
                weekday=null;
                restaurantName=null;
                restaurantID=null;
                seatsNumber=0;


            } else {
                date= extras.getString("date");
                time= extras.getString("time");
                weekday= extras.getString("weekday");
                restaurantName= extras.getString("restaurantName");
                seatsNumber= extras.getInt("seats");
                restaurantID= extras.getString("restaurantId");
                currentUserId = extras.getString("userId");
                restaurantAddress= extras.getString("restaurantAddress");
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
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(restaurantID == null){
            return;
        }

        final ArrayList<Dish> dishes = new ArrayList<>();
        final ArrayList<Offer> offers = new ArrayList<>();

        Thread t = new Thread()
        {
            public void run() {

                firebaseGetMenuByTypeManager = new FirebaseGetMenuByTypeManager();
                firebaseGetMenuByTypeManager.getMenu(restaurantID, null, null);
                firebaseGetMenuByTypeManager.waitForResult();
                dishes.addAll(firebaseGetMenuByTypeManager.getResult());

                firebaseGetOfferListManager = new FirebaseGetOfferListManager();
                firebaseGetOfferListManager.getOffers(restaurantID);
                firebaseGetOfferListManager.waitForResult();
                offers.addAll(firebaseGetOfferListManager.getResult());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAdapter(dishes, offers);
                    }
                });

            }
        };

        t.start();
    }

    private void initAdapter(ArrayList<Dish> dishes, ArrayList<Offer> offers) {
        //Conversion from Offer to ReservedDish

        //initialize the lists
        lists = new ArrayList<ArrayList<ReservedDish>>();
        for(int i=0;i<5;i++)
            lists.add(new ArrayList<ReservedDish>());

        for(Offer offer : offers){
            ReservedDish rd = new ReservedDish();
            rd.setName(offer.getOfferName());
            rd.setIsOffer(true);
            rd.setPrice(offer.getPrice());
            rd.setQuantity(0);
            lists.get(0).add(rd);
        }

        //Conversion from Dish to ReservedDish
        for(Dish dish : dishes){
            lists.get(
                    DishTypeConverter.fromEnumToIndex(DishTypeConverter.fromStringToEnum(dish.getType())) + 1
                ).add(newReservedDish(dish));
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
                    i.putExtra("reservedDishes", (Serializable) reservedDishes );
                    /*i.putParcelableArrayListExtra("offers", lists.get(0) );
                    i.putParcelableArrayListExtra("main",lists.get(1) );
                    i.putParcelableArrayListExtra("second", lists.get(2));
                    i.putParcelableArrayListExtra("dessert", lists.get(3) );
                    i.putParcelableArrayListExtra("other",lists.get(4) );*/
                    i.putExtra("date", date);
                    i.putExtra("weekday", weekday);
                    i.putExtra("time", time);
                    i.putExtra("seats", seatsNumber);
                    i.putExtra("address", restaurantAddress);
                    i.putExtra("restaurantName", restaurantName);
                    i.putExtra("restaurantId", restaurantID);
                    i.putExtra("userId", currentUserId);
                    startActivity(i);
                }

            }
        });
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
