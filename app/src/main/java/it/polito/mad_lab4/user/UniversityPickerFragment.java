package it.polito.mad_lab4.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

import it.polito.mad_lab4.R;

/**
 * Created by Giovanna on 28/05/2016.
 */
public class UniversityPickerFragment extends DialogFragment {

    ArrayList<String> data;

    public static UniversityPickerFragment newInstance( ArrayList<String> data) {
        UniversityPickerFragment f = new UniversityPickerFragment();
        f.setData(data);
        return f;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };
        String[] items;
        items = new String[4];
        items[0]="Pippo";
        items[1]="Pluto";
        items[2]="Pallino";
        items[3]="Pallina";
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(inflater.inflate(R.layout.dialog_pick_university, null));
        builder.setSingleChoiceItems(items, 0,listener);
        builder.setMessage(R.string.pick_your_university)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

}