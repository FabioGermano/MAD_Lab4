package it.polito.mad_lab4.manager.reservation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservationType;
import it.polito.mad_lab4.newData.reservation.ReservationTypeConverter;

public final class ReservationFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<Reservation> reservations;
    private RecyclerAdapterReservations myAdapter;

    public ReservationFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ReservationFragment newInstance(int sectionNumber, ArrayList<Reservation> reservations) {
        ReservationFragment fragment = new ReservationFragment();
        fragment.setReservations(reservations);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reservations, container, false);
        RecyclerView rView = (RecyclerView) rootView.findViewById(R.id.recyclerView_reservations);

        myAdapter = new RecyclerAdapterReservations(getContext(), getReservations(), this);
        if(rView != null) {
            rView.setAdapter(myAdapter);

            LinearLayoutManager myLLM_vertical = new LinearLayoutManager(getActivity());
            myLLM_vertical.setOrientation(LinearLayoutManager.VERTICAL);
            rView.setLayoutManager(myLLM_vertical);

            rView.setItemAnimator(new DefaultItemAnimator());
        }

        return rootView;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void notifyItemRemoved(int adapterPosition) {
        this.myAdapter.notifyItemRemoved(adapterPosition);
    }

    public void moveReservationToNewState(int adapterPosition, ReservationType oldType, ReservationType newType) {
        ((ReservationsActivity)getActivity()).moveReservationToNewState(adapterPosition, oldType, newType);
    }

    public RecyclerAdapterReservations getAdapter(){
        return this.myAdapter;
    }

    public void setReservationAsVerified(int adapterPosition) {
        ((ReservationsActivity)getActivity()).setReservationAsVerified(adapterPosition);
    }
}
