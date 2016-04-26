package it.polito.mad_lab3.reservation;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad_lab3.R;
import ivb.com.materialstepper.progressMobileStepper;

public class ReservationActivity extends progressMobileStepper {

    List<Class> stepperFragmentList = new ArrayList<>();

    @Override
    public void onStepperCompleted() {

        showCompletedDialog();
    }



    @Override
    public List<Class> init() {

        stepperFragmentList.add(CalendarFragment.class);
        stepperFragmentList.add(PeopleFragment.class);
        stepperFragmentList.add(DishesFragment.class);

        return stepperFragmentList;
    }

    protected void showCompletedDialog(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                ReservationActivity.this);

        // set title
        alertDialogBuilder.setTitle("Hooray");
        alertDialogBuilder
                .setMessage("We've completed the stepper")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
