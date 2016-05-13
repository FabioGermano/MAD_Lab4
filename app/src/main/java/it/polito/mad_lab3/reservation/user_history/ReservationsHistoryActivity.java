package it.polito.mad_lab3.reservation.user_history;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.UserBL;
import it.polito.mad_lab3.common.UserSession;
import it.polito.mad_lab3.data.reservation.Reservation;
import it.polito.mad_lab3.data.user.User;

/**
 * Created by Giovanna on 12/05/2016.
 */
public class ReservationsHistoryActivity extends BaseActivity {

    private User user;
    private ListView listView;
    private ArrayList<Reservation> reservations;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservations_history);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.your_reservations));

        user = UserBL.getUserById(getApplicationContext(), UserSession.userId);
        reservations = user.getReservations();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ReservationsHistoryAdapter(this, this, reservations);
        mRecyclerView.setAdapter(mAdapter);

        //listView = (ListView) findViewById(R.id.listView);
        //listView.setAdapter(new ReservationsHistoryAdapter(this ,this, reservations ));

    }

    @Override
    protected User controlloLogin() {
        return null;
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
