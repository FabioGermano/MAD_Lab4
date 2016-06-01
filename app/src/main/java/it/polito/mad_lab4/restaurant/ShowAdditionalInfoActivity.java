package it.polito.mad_lab4.restaurant;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.polito.mad_lab4.BaseActivity;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.common.Helper;
import it.polito.mad_lab4.data.restaurant.BasicInfo;
import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.data.user.User;

/**
 * Created by Giovanna on 07/05/2016.
 */
public class ShowAdditionalInfoActivity extends BaseActivity{

    private BasicInfo basicInfo;
    private TextView restaurantNameTextView, address, phoneNumber, email, description;
    private TextView mon, tue, wed, thu, fri, sat, sun;
    private TextView wifi, reservations, seatsOutside, parking, music, creditCard, bancomat;
    private ImageView logo;
    private String restaurantName;
    private LinearLayout send_email, call, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_additional_info);

        setToolbarColor();

        setActivityTitle(getResources().getString(R.string.additional_info));

        basicInfo= (BasicInfo) getIntent().getExtras().getSerializable("basicInfo");
        restaurantName = getIntent().getExtras().getString("restaurantName");
        logo = (ImageView) findViewById(R.id.logo);

        restaurantNameTextView = (TextView) findViewById(R.id.restaurant_name);
        address = (TextView) findViewById(R.id.address);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        email = (TextView) findViewById(R.id.email);
        description = (TextView) findViewById(R.id.description);
        call = (LinearLayout) findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.dialNumber(getApplicationContext(), basicInfo.getPhone());
            }
        });
        location = (LinearLayout) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.findOnGoogleMaps(getApplicationContext(), basicInfo.getAddress(), basicInfo.getCity());
            }
        });

        send_email = (LinearLayout) findViewById(R.id.send_email);
        send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(basicInfo.getEmail());
            }
        });

        ((ImageView)findViewById(R.id.mapIcon)).setColorFilter(Color.BLACK);
        ((ImageView)findViewById(R.id.phoneIcon)).setColorFilter(Color.BLACK);
        ((ImageView)findViewById(R.id.emailIcon)).setColorFilter(Color.BLACK);




        mon = (TextView) findViewById(R.id.monday);
        tue = (TextView) findViewById(R.id.tuesday);
        wed = (TextView) findViewById(R.id.wedenesday);
        thu = (TextView) findViewById(R.id.thursday);
        fri = (TextView) findViewById(R.id.friday);
        sat = (TextView) findViewById(R.id.saturday);
        sun = (TextView) findViewById(R.id.sunday);

        wifi = (TextView) findViewById(R.id.wifi);
        reservations = (TextView) findViewById(R.id.reservations);
        parking = (TextView) findViewById(R.id.parking);
        music = (TextView) findViewById(R.id.music);
        creditCard = (TextView) findViewById(R.id.creditcard);
        bancomat = (TextView) findViewById(R.id.bancomat);
        seatsOutside = (TextView) findViewById(R.id.seats_outside);

        restaurantNameTextView.setText(restaurantName);
        address.setText(basicInfo.getAddress()+ ", "+basicInfo.getCity());
        phoneNumber.setText(basicInfo.getPhone());
        email.setText(basicInfo.getEmail());
        description.setText(basicInfo.getDescription());

        TextView[] time = {mon, tue, wed, thu, fri, sat, sun};

        int i=0;
        for(String s : basicInfo.getTimeTable()){
            time[i].setText(s);
            i++;
        }

        wifi.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isWifi()));
        reservations.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isReservations()));
        music.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isMusic()));
        parking.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isParking()));
        creditCard.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isCreditCard()));
        bancomat.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isBancomat()));
        seatsOutside.setText(Helper.fromBoolToString(getApplicationContext(),basicInfo.isSeatsOutside()));

    }



    public void sendEmail(String contact) {
        Log.i("Send email", "");
        String[] TO = {contact};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_mail)));
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ShowAdditionalInfoActivity.this, getResources().getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
        }
    }

}