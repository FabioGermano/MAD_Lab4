package it.polito.mad_lab4.manager.reservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.firebase_manager.FirebaseGetAuthInformation;
import it.polito.mad_lab4.firebase_manager.FirebaseGetReservationsManager;
import it.polito.mad_lab4.manager.GestioneDB;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservationType;
import it.polito.mad_lab4.newData.reservation.ReservationTypeConverter;
import it.polito.mad_lab4.newData.reservation.ReservedDish;

public class ReservationsActivity extends it.polito.mad_lab4.BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ReservationFragment[] reservationFragments = new ReservationFragment[4];
    private String selectedDate;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String datePicked;
    private String restaurantId;
    private ArrayList<Reservation> reservations = new ArrayList<>();


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView dateText;
    private FirebaseUser currentUser;
    private FirebaseGetReservationsManager firebaseGetReservationsManager;
    private int currYear;
    private int currmonthOfYear;
    private int currdayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reservations);
        setToolbarColor();
        hideToolbarShadow(true);
        setVisibilityCalendar(true);
        invalidateOptionsMenu();

        restaurantId = (String) getIntent().getExtras().getString("restaurantId");

        dateFormatter = new SimpleDateFormat("yyyy-M-d");
        Calendar c = Calendar.getInstance();
        Date newDate = c.getTime();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(currYear == year && monthOfYear == currmonthOfYear && dayOfMonth == currdayOfMonth){
                    return;
                }
                currYear = year;
                currmonthOfYear = monthOfYear;
                currdayOfMonth = dayOfMonth;
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                datePicked= dateFormatter.format(newDate.getTime());
                changeDate(datePicked);
            }

        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(newDate.getTime());
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);

        setActivityTitle(getResources().getString(R.string.title_activity_reservations));
        dateText = (TextView) findViewById(R.id.date);
        dateText.setText(selectedDate);

        mViewPager = (ViewPager) findViewById(R.id.container);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        changeDate(dateFormatter.format(newDate.getTime()));
    }

    /*void getReservations(){
        GestioneDB db = new GestioneDB();
        this.res_entity = db.getAllReservations(getApplicationContext());
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_calendar:
                datePickerDialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void changeDate(final String newDate){
        this.selectedDate = newDate;
        dateText.setText(newDate);

        showProgressBar();

        new Thread() {
            public void run() {
                FirebaseGetAuthInformation firebaseGetAuthInformation = new FirebaseGetAuthInformation();
                firebaseGetAuthInformation.waitForResult();
                currentUser = firebaseGetAuthInformation.getUser();
                if(currentUser != null) {
                    firebaseGetReservationsManager = new FirebaseGetReservationsManager();
                    firebaseGetReservationsManager.getReservations(null, restaurantId, newDate);
                    firebaseGetReservationsManager.waitForResult();
                    reservations = new ArrayList<Reservation>();
                    reservations.addAll(firebaseGetReservationsManager.getResult());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();

                            if(reservations == null){
                                Snackbar.make(findViewById(android.R.id.content), "Connection error", Snackbar.LENGTH_LONG)
                                        .show();
                                return;
                            }

                            int pos = 0;
                            for(ReservationFragment rf : reservationFragments){
                                if(rf != null){
                                    rf.getReservations().clear();
                                    rf.getReservations().addAll(getReservationsByType(ReservationTypeConverter.fromTabPosition(pos)));
                                    rf.getAdapter().notifyDataSetChanged();
                                }
                                pos++;
                            }
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            reservationFragments[position] = ReservationFragment.newInstance(position + 1, getReservationsByType(ReservationTypeConverter.fromTabPosition(position)));
            return reservationFragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.pending);
                case 1:
                    return getResources().getString(R.string.accepted);
                case 2:
                    return getResources().getString(R.string.deleted);
                case 3:
                    return getResources().getString(R.string.rejected);
            }
            return null;
        }
    }

    public void moveReservationToNewState(int positionReservationOldList, ReservationType oldType, ReservationType newType){
        int arrayId = ReservationTypeConverter.fromType(newType);
        int oldArrayId = ReservationTypeConverter.fromType(oldType);
        ArrayList<Reservation> oldList = this.reservationFragments[oldArrayId].getReservations();

        if(this.reservationFragments[arrayId] != null){
            this.reservationFragments[arrayId].getReservations().add(oldList.get(positionReservationOldList));
            this.reservationFragments[arrayId].getAdapter().notifyItemInserted(this.reservationFragments[arrayId].getReservations().size()-1);
        }

        oldList.remove(positionReservationOldList);
        this.reservationFragments[oldArrayId].getAdapter().notifyItemRemoved(positionReservationOldList);
    }

    public void setReservationAsVerified(int position){
        int arrayId = ReservationTypeConverter.fromType(ReservationType.ACCEPTED);
        if(this.reservationFragments[arrayId] != null){
            this.reservationFragments[arrayId].getReservations().get(position).setIsVerified(true);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("reservations/"+this.reservationFragments[arrayId].getReservations().get(position).getReservationId()+"/isVerified");
            myRef.setValue(true);

            this.reservationFragments[arrayId].getAdapter().notifyItemChanged(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(firebaseGetReservationsManager != null){
            firebaseGetReservationsManager.terminate();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private ArrayList<Reservation> getReservationsByType(ReservationType reservationType){
        ArrayList<Reservation> res = new ArrayList<>();
        for(Reservation r : this.reservations){
            if(ReservationTypeConverter.toString(reservationType).equals(r.getStatus())){
                res.add(r);
            }
        }

        return res;
    }
}
