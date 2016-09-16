package it.polito.mad_lab4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactUsActivity extends  BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        setToolbarColor();

        setVisibilityAlert(false);
        setActivityTitle(getResources().getString(R.string.contactus_activity_title));

        invalidateOptionsMenu();
    }

}
