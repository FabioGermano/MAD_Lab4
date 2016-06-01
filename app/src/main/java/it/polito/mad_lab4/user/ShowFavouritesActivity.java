package it.polito.mad_lab4.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.reservation.Reservation;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.newData.restaurant.Restaurant;
import it.polito.mad_lab4.reservation.user_history.ReservationsHistoryAdapter;

public class ShowFavouritesActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Restaurant> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favourites);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.title_activity_show_favourites));
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        //TODO recuperare i dati dal db
        favourites= new ArrayList<>();
        Restaurant r= new Restaurant();
        r.setRestaurantName("Alma Ratina");
        r.setAddress("Via Dalmazia");
        r.setCity("Torino");
        r.setTotRanking(42);
        r.setNumReviews(16);
        favourites.add(r);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new FavouritesAdapter(getBaseContext(), favourites);
        mRecyclerView.setAdapter(mAdapter);
    }


}
