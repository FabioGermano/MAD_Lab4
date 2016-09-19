package it.polito.mad_lab4.data.reservation;


import android.content.Context;

import it.polito.mad_lab4.R;

/**
 * Created by f.germano on 11/04/2016.
 */
public final class ReservationTypeConverter
{

    public static ReservationType fromTabPosition(int position){
        switch (position) {
            case 0:
                return ReservationType.PENDING;
            case 1:
                return ReservationType.ACCEPTED;
            case 2:
                return ReservationType.DELETED;
            case 3:
                return ReservationType.REJECTED;
        }
        return  null;
    }

    public static int fromType(ReservationType type){
        switch (type) {
            case PENDING:
                return 0;
            case ACCEPTED:
                return 1;
            case DELETED:
                return 2;
            case REJECTED:
                return 3;
        }
        return -1;
    }

    public static String toString(ReservationType reservationType)
    {
        if(reservationType == ReservationType.PENDING){
            return "Pending";
        }
        else if(reservationType == ReservationType.ACCEPTED){
            return "Accepted";
        }
        else if(reservationType == ReservationType.DELETED){
            return "Deleted";
        }
        else if(reservationType == ReservationType.REJECTED)
        {
            return "Rejected";
        }

        return null;
    }

    public static String toStringGeneral(Context context, String reservationType)
    {
        if(reservationType.toUpperCase().equals(ReservationType.PENDING.toString().toUpperCase())){
            return context.getResources().getString(R.string.pending_singular);
        }
        else if(reservationType.toUpperCase().equals(ReservationType.ACCEPTED.toString().toUpperCase())){
            return context.getResources().getString(R.string.accepted_singular);
        }
        else if(reservationType.toUpperCase().equals(ReservationType.DELETED.toString().toUpperCase())){
            return context.getResources().getString(R.string.deleted_singular);
        }
        else  if(reservationType.toUpperCase().equals(ReservationType.REJECTED.toString().toUpperCase())){
            return context.getResources().getString(R.string.rejected_singular);
        }

        return null;
    }
}
