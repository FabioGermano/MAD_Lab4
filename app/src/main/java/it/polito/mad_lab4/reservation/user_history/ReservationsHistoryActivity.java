package it.polito.mad_lab4.reservation.user_history;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.UserBL;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseGetReservationsManager;
import it.polito.mad_lab4.firebase_manager.FirebaseUserPhotoLikeManager;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.user.User;

/**
 * Created by Giovanna on 12/05/2016.
 */
public class ReservationsHistoryActivity extends BaseActivity {

    private ListView listView;
    private ArrayList<Reservation> reservations;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseUser currentUser;
    private FirebaseGetReservationsManager firebaseGetReservationsManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservations_history);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.your_reservations));
    }

    @Override
    protected void onResume() {
        super.onResume();

        showProgressBar();

        new Thread() {
            public void run() {
                FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                firebaseGetAuthInformation.waitForResult();
                currentUser = firebaseGetAuthInformation.getUser();
                if(currentUser != null) {
                    firebaseGetReservationsManager = new FirebaseGetReservationsManager();
                    firebaseGetReservationsManager.getReservations(currentUser.getUid(), null, null);
                    firebaseGetReservationsManager.waitForResult();
                    reservations = firebaseGetReservationsManager.getResult();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();

                            if(reservations == null){
                                Snackbar.make(findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                                        .show();
                                return;
                            }

                            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

                            mLayoutManager = new LinearLayoutManager(ReservationsHistoryActivity.this);
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            mAdapter = new ReservationsHistoryAdapter(ReservationsHistoryActivity.this, ReservationsHistoryActivity.this, reservations);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firebaseGetReservationsManager.terminate();
    }
}
