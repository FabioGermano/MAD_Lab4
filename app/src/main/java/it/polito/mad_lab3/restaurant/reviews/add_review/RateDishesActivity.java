package it.polito.mad_lab3.restaurant.reviews.add_review;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.RestaurantBL;
import it.polito.mad_lab3.data.reservation.ReservedDish;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.restaurant.DishType;
import it.polito.mad_lab3.data.restaurant.Offer;
import it.polito.mad_lab3.data.restaurant.Restaurant;
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
    private int restaurantId=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_dishes);

        if (isLargeDevice(getBaseContext())) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        hideToolbar(true);
        hideToolbarShadow(true);

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
                getBaseContext(), data );
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_menu);
        tabLayout.setupWithViewPager(mViewPager);
        doneFab = (FloatingActionButton) findViewById(R.id.done);

        doneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int j=0;j<5;j++) {
                    for (ReviewFood r : data.get(j)) {
                        //Log.w("RF", String.valueOf(r.getRating()));
                        if(r.getRating()>=0) {
                            switch (j) {
                                case 0:
                                    //offers
                                    restaurant.getOffers().get(r.getPosition()).setNumRanks(restaurant.getOffers().get(r.getPosition()).getNumRanks()+1);
                                    restaurant.getOffers().get(r.getPosition()).setSumRank(restaurant.getOffers().get(r.getPosition()).getSumRank()+r.getRating());
                                    break;
                                case 1:
                                    restaurant.getDishesOfCategory(DishType.MainCourses).get(r.getPosition()).setNumRanks(restaurant.getDishesOfCategory(DishType.MainCourses).get(r.getPosition()).getNumRanks()+1);
                                    restaurant.getDishesOfCategory(DishType.MainCourses).get(r.getPosition()).setSumRank(restaurant.getDishesOfCategory(DishType.MainCourses).get(r.getPosition()).getSumRank()+r.getRating());

                                    break;
                                case 2:
                                    restaurant.getDishesOfCategory(DishType.SecondCourses).get(r.getPosition()).setNumRanks(restaurant.getDishesOfCategory(DishType.SecondCourses).get(r.getPosition()).getNumRanks()+1);
                                    restaurant.getDishesOfCategory(DishType.SecondCourses).get(r.getPosition()).setSumRank(restaurant.getDishesOfCategory(DishType.SecondCourses).get(r.getPosition()).getSumRank()+r.getRating());


                                    break;
                                case 3:
                                    restaurant.getDishesOfCategory(DishType.Dessert).get(r.getPosition()).setNumRanks(restaurant.getDishesOfCategory(DishType.Dessert).get(r.getPosition()).getNumRanks()+1);
                                    restaurant.getDishesOfCategory(DishType.Dessert).get(r.getPosition()).setSumRank(restaurant.getDishesOfCategory(DishType.Dessert).get(r.getPosition()).getSumRank()+r.getRating());

                                    break;
                                case 4:
                                    restaurant.getDishesOfCategory(DishType.Other).get(r.getPosition()).setNumRanks(restaurant.getDishesOfCategory(DishType.Other).get(r.getPosition()).getNumRanks()+1);
                                    restaurant.getDishesOfCategory(DishType.Other).get(r.getPosition()).setSumRank(restaurant.getDishesOfCategory(DishType.Other).get(r.getPosition()).getSumRank()+r.getRating());

                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                //TODO insert review and restart from main activity

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
