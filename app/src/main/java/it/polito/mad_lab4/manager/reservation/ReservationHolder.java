package it.polito.mad_lab4.manager.reservation;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import junit.framework.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.reservation.Reservation;
import it.polito.mad_lab4.newData.reservation.ReservationType;
import it.polito.mad_lab4.newData.reservation.ReservationTypeConverter;
import it.polito.mad_lab4.newData.reservation.ReservedDish;

/**
 * Created by Giovanna on 12/04/2016.
 */
public class ReservationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context context;
    private Reservation reservation;
    private TextView username, type, time, userNote, yourNotes, ph_number;
    private LinearLayout offerListLayout, dishListLayout, acceptedChildFooter, expiredChildFooter, pendingChildFooter, verifiedChildFooter;
    private ImageButton expandeCollapseButton;
    private LinearLayout childLayout;
    private Button acceptButton, rejectButton, verifiedButton;
    private boolean state = true;
    private View containerView;
    private ReservationFragment containerFragment;
    private ArrayList<Reservation> reservations;

    public ReservationHolder(View v, Context context, ReservationFragment containerFragment, ArrayList<Reservation> reservations) {
        super(v);

        this.context = context;
        this.containerFragment = containerFragment;
        this.reservations = reservations;

        containerView = v;
        username = (TextView) v.findViewById(R.id.username);
        type = (TextView) v.findViewById(R.id.type);
        time = (TextView) v.findViewById(R.id.time);
        userNote = (TextView) v.findViewById(R.id.order_notes);
        yourNotes = (TextView) v.findViewById(R.id.yourNotes);
        ph_number = (TextView) v.findViewById(R.id.ph_number);

        childLayout = (LinearLayout) v.findViewById(R.id.childLayout);
        acceptButton = (Button) v.findViewById(R.id.acceptButton);
        rejectButton = (Button) v.findViewById(R.id.rejectButton);
        verifiedButton = (Button) v.findViewById(R.id.verifiedButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptClick(v);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectClick(v);
            }
        });

        verifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifiedClick(v);
            }
        });


        offerListLayout = (LinearLayout) v.findViewById(R.id.offerListLayout);
        dishListLayout = (LinearLayout) v.findViewById(R.id.dishListLayout);
        expandeCollapseButton = (ImageButton) v.findViewById(R.id.expandeCollapseRow);
        expandeCollapseButton.setOnClickListener(this);

        this.acceptedChildFooter = (LinearLayout) v.findViewById(R.id.acceptedChildFooter);
        this.expiredChildFooter = (LinearLayout) v.findViewById(R.id.expiredChildFooter);
        this.pendingChildFooter = (LinearLayout) v.findViewById(R.id.pendingChildFooter);
        this.verifiedChildFooter = (LinearLayout) v.findViewById(R.id.verifiedChildFooter);
    }

    public void setData(Reservation reservation) {
        this.reservation = reservation;
        type.setText(reservation.getType());
        username.setText(reservation.getUserName());
        time.setText(reservation.getTime());
        if(reservation.getNoteByUser() != null){
            userNote.setText(reservation.getNoteByUser());
        }
        if(reservation.getNoteByOwner() != null){
            yourNotes.setText(reservation.getNoteByOwner());
        }
        ph_number.setText(reservation.getPhone());

        manageFooterVisibility(reservation);

        ArrayList<ReservedDish> reservedDish = new ArrayList<>();
        ArrayList<ReservedDish> reservedOffers = new ArrayList<>();
        if(reservation.getReservedDishes() != null){
            reservedDish = reservation.getReservedDishesByType(false);
            reservedOffers = reservation.getReservedDishesByType(true);
        }

        if (reservation.getPlaces() == null || reservation.getPlaces() == "") {
            ((TextView) childLayout.findViewById(R.id.seats_number)).setVisibility(View.GONE);
            ((TextView) childLayout.findViewById(R.id.seats)).setVisibility(View.GONE);
        }
        else
            ((TextView) childLayout.findViewById(R.id.seats_number)).setText(reservation.getPlaces());

        if(reservation.getNoteByOwner() == null){
            yourNotes.setVisibility(View.GONE);
            ((TextView) childLayout.findViewById(R.id.your_notesView)).setVisibility(View.GONE);
        }

        if (reservedDish.size() > 0) {
            View child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText("Name");
            name.setTypeface(name.getTypeface(), Typeface.BOLD);
            TextView quantity = (TextView) child.findViewById(R.id.quantity);
            quantity.setText("Quantity");
            quantity.setTypeface(name.getTypeface(), Typeface.BOLD);
            this.dishListLayout.addView(child);

            for (ReservedDish rd : reservedDish) {
                child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
                name = (TextView) child.findViewById(R.id.food_name);
                name.setText(rd.getName());
                quantity = (TextView) child.findViewById(R.id.quantity);
                quantity.setText(String.valueOf(rd.getQuantity()));

                this.dishListLayout.addView(child);
            }
        } else {
            ((TextView) childLayout.findViewById(R.id.dishes)).setVisibility(View.GONE);
            ((LinearLayout) childLayout.findViewById(R.id.dishListLayout)).setVisibility(View.GONE);
        }

        if (reservedOffers.size() > 0) {
            View child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
            TextView name = (TextView) child.findViewById(R.id.food_name);
            name.setText("Name");
            name.setTypeface(name.getTypeface(), Typeface.BOLD);
            TextView quantity = (TextView) child.findViewById(R.id.quantity);
            quantity.setText("Quantity");
            quantity.setTypeface(name.getTypeface(), Typeface.BOLD);
            this.offerListLayout.addView(child);

            for (ReservedDish rd : reservedOffers) {
                child = LayoutInflater.from(context).inflate(R.layout.order_row, null);
                name = (TextView) child.findViewById(R.id.food_name);
                name.setText(rd.getName());
                quantity = (TextView) child.findViewById(R.id.quantity);
                quantity.setText(String.valueOf(rd.getQuantity()));

                this.offerListLayout.addView(child);
            }
        } else {
            ((TextView) childLayout.findViewById(R.id.offers)).setVisibility(View.GONE);
            ((LinearLayout) childLayout.findViewById(R.id.offerListLayout)).setVisibility(View.GONE);
        }
    }

    private void manageFooterVisibility(Reservation reservation) {
        if (reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.PENDING))) {
            this.pendingChildFooter.setVisibility(View.VISIBLE);
        }
        else if (reservation.getStatus().equals(ReservationTypeConverter.toString(ReservationType.ACCEPTED)))
        {
            if (reservation.getIsExpired()) {
                this.expiredChildFooter.setVisibility(View.VISIBLE);
            } else if (reservation.getIsVerified()) {
                this.verifiedChildFooter.setVisibility(View.VISIBLE);
            }
            else{
                this.acceptedChildFooter.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setBorder(View v)
    {
        //use a GradientDrawable with only one color set, to make it a solid color
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackgroundDrawable(border);
        } else {
            v.setBackground(border);
        }
    }

    private void expandOrCollapse(final View v,String exp_or_colpse, int height) {
        TranslateAnimation anim = null;
        if(exp_or_colpse.equals("expand"))
        {
            anim = new TranslateAnimation(0.0f, 0.0f, -height, 0.0f);
            anim.setDuration(150);
            v.setVisibility(View.VISIBLE);
        }
        else{
            anim = new TranslateAnimation(0.0f, 0.0f, 0.0f, -height);
            anim.setDuration(150);
            Animation.AnimationListener collapselistener= new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            };

            anim.setAnimationListener(collapselistener);
        }

        // To Collapse
        //

        anim.setDuration(200);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        v.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        if(!state){
            (containerView.findViewById(R.id.expandeCollapseRow)).setBackgroundResource(android.R.drawable.arrow_up_float);
            expandOrCollapse(childLayout, "collapse", childLayout.getHeight());
            //expandOrCollapse(containerView, "collapse", containerView.getHeight() - childLayout.getHeight());
            state = true;
        }
        else
        {
            (containerView.findViewById(R.id.expandeCollapseRow)).setBackgroundResource(android.R.drawable.arrow_down_float);
            expandOrCollapse(childLayout, "expand", childLayout.getHeight());
            //expandOrCollapse(containerView, "expand", containerView.getHeight() - childLayout.getHeight());
            state = false;
        }
    }

    private void acceptClick(View v) {
        this.reservation.setStatus(ReservationTypeConverter.toString(ReservationType.ACCEPTED));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations/"+this.reservation.getReservationId());
        this.reservation.setNotified(false);
        myRef.setValue(this.reservation);

        containerFragment.moveReservationToNewState(getAdapterPosition(), ReservationType.PENDING, ReservationType.ACCEPTED);
    }

    private void rejectClick(View v) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                setRestaurantNotes(userInput.getText());
                                setAsRejected();
                            }
                        })
                .setNegativeButton("Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    private void setAsRejected() {
        this.reservation.setStatus(ReservationTypeConverter.toString(ReservationType.REJECTED));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("reservations/"+this.reservation.getReservationId());
        this.reservation.setNotified(false);
        myRef.setValue(this.reservation);

        containerFragment.moveReservationToNewState(getAdapterPosition(), ReservationType.PENDING, ReservationType.REJECTED);
    }

    private void setRestaurantNotes(Editable text) {
        this.reservation.setNoteByOwner(text.toString());
    }

    private void verifiedClick(View v) {
        containerFragment.setReservationAsVerified(getAdapterPosition());
        //containerFragment.moveReservationToNewState(getAdapterPosition(), ReservationType.PENDING, ReservationType.REJECTED);
    }
}
