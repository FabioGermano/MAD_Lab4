package it.polito.mad_lab3.reservation;

import android.app.Activity;
import android.support.v4.app.ListFragment;

/**
 * Created by Giovanna on 27/04/2016.
 */
public class ReservationInterface extends ListFragment {

    OnHeadlineSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {

        public void onDateSelected(String dayOfTheWeek);
        public void onTypeSelected(boolean takeaway);
        public void onSeatsNumberSelected(int seats);
        public void onOrderCompleted(int x);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}