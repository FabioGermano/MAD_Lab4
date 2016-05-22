package it.polito.mad_lab4.manager.data.reservation;

import java.util.ArrayList;

/**
 * Created by f.germano on 12/04/2016.
 */
public class ReservationEntity {
    public ReservationEntity() {
        this.reservations = new ArrayList<Reservation>();
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
    }

    public ArrayList<Reservation> reservations;

    public ArrayList<Reservation> getReservationsByDateAndType(String date, ReservationType type){
        ArrayList<Reservation> list = new ArrayList<Reservation>();
        for ( Reservation r : reservations){
            if(r.getDate().equals(date) &&  ReservationTypeConverter.toString(type).equals(r.getStatus())){
                list.add(r);
            }
        }
        return list;
    }
}
