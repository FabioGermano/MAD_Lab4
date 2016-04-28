package it.polito.mad_lab3.reservation;

import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;

import ivb.com.materialstepper.progressMobileStepper;

public class ReservationActivityWithStepper extends progressMobileStepper {

    List<Class> stepperFragmentList = new ArrayList<>();

    @Override
    public void onStepperCompleted() {

        showCompletedDialog();
    }



    @Override
    public List<Class> init() {

        stepperFragmentList.add(CalendarFragment.class);
        stepperFragmentList.add(ChoiceFragment.class);
        stepperFragmentList.add(DishesFragment.class);

        return stepperFragmentList;
    }

    protected void showCompletedDialog(){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                ReservationActivityWithStepper.this);

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
