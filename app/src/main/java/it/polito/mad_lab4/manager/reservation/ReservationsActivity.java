package it.polito.mad_lab4.manager.reservation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.polito.mad_lab4.data.user.User;
import it.polito.mad_lab4.manager.GestioneDB;
import it.polito.mad_lab4.R;
import it.polito.mad_lab4.manager.data.reservation.Reservation;
import it.polito.mad_lab4.manager.data.reservation.ReservationEntity;
import it.polito.mad_lab4.manager.data.reservation.ReservationType;
import it.polito.mad_lab4.manager.data.reservation.ReservationTypeConverter;

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
    private ReservationEntity res_entity;
    private ReservationFragment[] reservationFragments = new ReservationFragment[4];
    private String selectedDate = "2016-04-12";
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String datePicked;



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_reservations);
        setToolbarColor();
        hideToolbarShadow(true);
        setVisibilityCalendar(true);
        invalidateOptionsMenu();
        getReservations();

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        Date newDate = c.getTime();
        //set to show actual date
        selectedDate = dateFormatter.format(newDate.getTime());

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), res_entity);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected User controlloLogin() {
        return null;
    }

    void getReservations(){
        GestioneDB db = new GestioneDB();
        this.res_entity = db.getAllReservations(getApplicationContext());
    }


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
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void changeDate(String newDate){
        this.selectedDate = newDate;
        dateText.setText(newDate);
        int pos = 0;
        for(ReservationFragment rf : this.reservationFragments){
            if(rf != null){
                rf.getReservations().clear();
                rf.getReservations().addAll(res_entity.getReservationsByDateAndType(selectedDate, ReservationTypeConverter.fromTabPosition(pos)));
                rf.getAdapter().notifyDataSetChanged();
            }
            pos++;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ReservationEntity res_entity;

        public SectionsPagerAdapter(FragmentManager fm, ReservationEntity res_entity) {
            super(fm);
            this.res_entity = res_entity;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            reservationFragments[position] = ReservationFragment.newInstance(position + 1, res_entity.getReservationsByDateAndType(selectedDate, ReservationTypeConverter.fromTabPosition(position)));
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
            this.reservationFragments[arrayId].getAdapter().notifyItemChanged(position);
        }

    }

    private void updateDb()
    {
        GestioneDB db = new GestioneDB();
        db.UpdateReservations(getApplicationContext(), this.res_entity);
    }

    @Override
    public void onPause() {
        super.onPause();

        updateDb();
    }
}
