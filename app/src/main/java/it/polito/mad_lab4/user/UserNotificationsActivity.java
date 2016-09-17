package it.polito.mad_lab4.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.alert.UserAlert;
import it.polito.mad_lab4.firebase_manager.FirebaseGetNotificationListManager;
import it.polito.mad_lab4.newData.user.Notification;

public class UserNotificationsActivity extends BaseActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseGetNotificationListManager firebaseGetNotificationListManager;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private String userId;
    private int countNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.title_activity_notifications));
        setVisibilityAlert(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        userId = UserAlert.userId;
        countNew = (int) getIntent().getExtras().getInt("countNew");

        UserAlert.resetAlertCount();
        //setAlertCount(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showProgressBar();

        Thread t = new Thread()
        {
            public void run() {

                firebaseGetNotificationListManager = new FirebaseGetNotificationListManager();
                firebaseGetNotificationListManager.getNotifications(userId);
                firebaseGetNotificationListManager.waitForResult();
                notifications.clear();
                notifications.addAll(firebaseGetNotificationListManager.getResult());

                Collections.reverse(notifications);

                if(notifications == null){
                    dismissProgressDialog();
                    Log.e("GetNotifications", "resturned null notifications");
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog();

                        mAdapter = new NotificationsAdapter(getBaseContext(), UserNotificationsActivity.this, notifications, countNew);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

            }
        };

        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(firebaseGetNotificationListManager != null) {
            firebaseGetNotificationListManager.terminate();
        }
    }
}
