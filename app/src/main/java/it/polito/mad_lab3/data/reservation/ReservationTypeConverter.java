package it.polito.mad_lab3.data.reservation;


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
}
