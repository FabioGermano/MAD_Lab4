package it.polito.mad_lab3.reservation;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.bl.UserBL;
import it.polito.mad_lab3.data.reservation.Reservation;
import it.polito.mad_lab3.data.user.User;

/**
 * Created by Giovanna on 12/05/2016.
 */
public class ReservationsHistoryActivity extends BaseActivity {

    private User user;
    private ListView listView;
    private ArrayList<Reservation> reservations;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservations_history);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.your_reservations));

        user = UserBL.getUserById(getApplicationContext(),1);
        reservations = user.getReservations();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ReservationsHistoryAdapter(getApplicationContext(),reservations ));

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
