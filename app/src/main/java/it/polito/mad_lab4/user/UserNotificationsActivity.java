package it.polito.mad_lab4.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.data.user.Notification;
import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.newData.restaurant.Restaurant;

public class UserNotificationsActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.title_activity_notifications));
        setVisibilityAlert(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        //TODO recuperare i dati dal db
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Notification> notifications = new ArrayList<>();
        Notification n1 = new Notification("dsFds0", false , "Anna we can't wait to see you here!");
        n1.setAccepted(true);
        n1.setRestaurantName("Alma Latina");
        Notification n2 = new Notification("dsFddfs0", false , "Sorry Anna! We do not have available seats at that time! Hope to see you soon");
        n2.setAccepted(false);
        n2.setRestaurantName("Da Franceschino");
        Notification n3 = new Notification("dsFddfs0", true , "Only for today a new offer for you! Mozzarella from Puglia for your perfect caprese!");
        n3.setRestaurantName("Yokoshima");
        Notification n4 = new Notification("dsFddfs0", true , null);
        n4.setRestaurantName("Yokoshima");

        notifications.add(n1);
        notifications.add(n2);
        notifications.add(n3);
        notifications.add(n4);
        // specify an adapter (see also next example)
        mAdapter = new NotificationsAdapter(getBaseContext(), this, notifications);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected User controlloLogin() {
        return null;
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }
}
