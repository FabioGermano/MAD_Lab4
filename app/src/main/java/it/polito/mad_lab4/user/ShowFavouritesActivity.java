package it.polito.mad_lab4.user;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import java.io.Console;
import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.reservation.Reservation;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseGetFavouritesListManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetRestaurantInfoManager;
import it.polito.mad_lab4.firebase_manager.FirebaseGetReviewsListManager;
import it.polito.mad_lab4.newData.restaurant.FavouritesRestaurantInfos;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.newData.restaurant.Review;
import it.polito.mad_lab4.reservation.user_history.ReservationsHistoryAdapter;
import it.polito.mad_lab4.restaurant.reviews.ReviewsListAdapter;

public class ShowFavouritesActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String userId;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Restaurant> favourites;
    private FirebaseGetFavouritesListManager firebaseGetFavouritesListManager;
    //private ArrayList<FavouritesRestaurantInfos> infos;
    private ArrayList<String[]> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favourites);

        setToolbarColor();
        setOnFavourites();

        setActivityTitle(getResources().getString(R.string.title_activity_show_favourites));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        userId= getIntent().getExtras().getString("userId");
        //userId = "7K4XwUDQzigPJFIWXaLl2TBosnf1";
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }
    @Override
    public void onResume() {
        super.onResume();


        //final ArrayList<FavouritesRestaurantInfos> infos = new ArrayList<>();
        final ArrayList<String[]> infos = new ArrayList<>();
        final ArrayList<String> favourites= new ArrayList<>();
        Thread t = new Thread()
        {
            public void run() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
showProgressBar();
                    }
                });

                firebaseGetFavouritesListManager = new FirebaseGetFavouritesListManager();
                firebaseGetFavouritesListManager.getFavourites(userId);
                firebaseGetFavouritesListManager.waitForResult();
                String[] fields={"restaurantName", "address", "city", "logoThumbDownloadLink", "totRanking", "numReviews"};

                favourites.addAll(firebaseGetFavouritesListManager.getResult());
                for(String restaurantId : favourites) {
                    final String[] result = new String[7];

                    for(int i=0;i<fields.length;i++){
                        FirebaseGetRestaurantInfoManager firebaseGetRestaurantInfoManager = new FirebaseGetRestaurantInfoManager();
                        firebaseGetRestaurantInfoManager.getRestaurantInfo(restaurantId, fields[i]);
                        firebaseGetRestaurantInfoManager.waitForResult();
                        result[i]= ((String) firebaseGetRestaurantInfoManager.getResult());
                        firebaseGetRestaurantInfoManager.terminate();
                    }
                    result[6]=restaurantId;
                    infos.add(result);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar();
                        initAdapter(infos);


                    }
                });

            }
        };

        t.start();
    }
    private void initAdapter(ArrayList<String[]> infos) {
        this.infos=infos;
        mAdapter = new FavouritesAdapter(getBaseContext(), infos);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //At this point the layout is complete and the
                //dimensions of recyclerView and any child views are known.
                System.out.println("------> in callback!!!");
                dismissProgressDialog();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        showProgressBar();
    }




}
