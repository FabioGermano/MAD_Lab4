package it.polito.mad_lab3.restaurant.reviews.add_review;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.MainActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.bl.UserBL;
import it.polito.mad_lab3.data.reservation.ReservedDish;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.DishType;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.Review;
import it.polito.mad_lab3.data.restaurant.ReviewFood;
import it.polito.mad_lab3.data.user.User;

/**
 * Created by Giovanna on 09/05/2016.
 */
public class RateDishesActivity extends BaseActivity{

    private ViewPager mViewPager;
    private FloatingActionButton doneFab;
    private Restaurant restaurant;
    ArrayList<ArrayList<ReviewFood>> data;
    private ArrayList<ArrayList<ReservedDish>> lists; //order: offer, main, second, dessert , other
    private int restaurantId=-1;
    private String review;
    private float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_dishes);

        if (isLargeDevice(getApplicationContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);

        //retrieve values from intent extras
        this.restaurantId = getIntent().getExtras().getInt("restaurantId");
        this.review = getIntent().getExtras().getString("review");
        this.rating = getIntent().getExtras().getFloat("rating");

        restaurant = RestaurantBL.getRestaurantById(getApplicationContext(), restaurantId);

        ArrayList<Dish> main = restaurant.getDishesOfCategory(DishType.MainCourses);
        ArrayList<Dish> second = restaurant.getDishesOfCategory(DishType.SecondCourses);
        ArrayList<Dish> dessert = restaurant.getDishesOfCategory(DishType.Dessert);
        ArrayList<Dish> other = restaurant.getDishesOfCategory(DishType.Other);
        ArrayList<Offer> offers = restaurant.getOffers();

        ArrayList<ReviewFood> off= new ArrayList<>();
        ArrayList<ReviewFood> m= new ArrayList<>();
        ArrayList<ReviewFood> s= new ArrayList<>();
        ArrayList<ReviewFood> d= new ArrayList<>();
        ArrayList<ReviewFood> oth= new ArrayList<>();
        int i=0;

        for(Offer offer: offers){
            off.add(new ReviewFood(offer,i,0,-1));
            i++;
        }
        i=0;
        for(Dish dish : main){
            m.add(new ReviewFood(dish,i,1,-1));
            i++;
        }
        i=0;
        for(Dish dish : second){
            s.add(new ReviewFood(dish,i,2,-1));
            i++;
        }
        i=0;
        for(Dish dish : dessert){
            d.add(new ReviewFood(dish,i,3,-1));
            i++;
        }
        i=0;
        for(Dish dish : other){
            oth.add(new ReviewFood(dish,i,4,-1));
            i++;
        }
        data = new ArrayList<>();
        data.add(off);
        data.add(m);
        data.add(s);
        data.add(d);
        data.add(oth);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                getApplicationContext(), data );
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
        doneFab = (FloatingActionButton) findViewById(R.id.done);

        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int j=0;j<5;j++) {
                    RestaurantBL.updateDishesRating(restaurant, data.get(j),  j);
                }
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date today = new Date();
                User user = UserBL.getUserById(getApplicationContext(), 1);
                Review r = new Review(user.getName(), null, rating, df.format(today), review );
                RestaurantBL.addReview(restaurant, r);
                RestaurantBL.saveChanges(getApplicationContext());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.review_published), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

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
}
