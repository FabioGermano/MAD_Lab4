package it.polito.mad_lab4.user;

import android.app.Activity;
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

    ArrayList<String> data2;
    String[] data;
    boolean isSelected;
    int selection;

    boolean typeSelection;
    private OnSelectionListener mCallback;

    public static UniversityPickerFragment newInstance(String[] data, boolean typeSelection) {
        UniversityPickerFragment f = new UniversityPickerFragment();
        f.setData(data);
        f.setTypeSelection(typeSelection);
        return f;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChoiceSelectedListener");
        }
    }
    public interface OnSelectionListener {
        void updateUniversity(int university, String name);
        void updateType(int n, String type);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] items;
        // Get the layout inflater
        //LayoutInflater inflater = getActivity().getLayoutInflater();
        if(!typeSelection) {

            items = new String[4];
            items[0] = "Pippo";
            items[1] = "Pluto";
            items[2] = "Pallino";
            items[3] = "Pallina";
        }
        else {

            items = new String[3];
            items[0] = "Student";
            items[1] = "Professor";
            items[2] = "Administrative employee";
        }
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        //builder.setView(inflater.inflate(R.layout.dialog_pick_university, null));

            builder.setTitle(getResources().getString(R.string.pick_your_university))
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    selection=which;
            }
        });
        if(typeSelection) {
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mCallback.updateType(selection, items[selection]);
                }
            });
        }
        else {
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mCallback.updateUniversity(selection, items[selection]);
                }
            });
        }

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        selection=-1;
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public boolean isTypeSelection() {
        return typeSelection;
    }

    public void setTypeSelection(boolean typeSelection) {
        this.typeSelection = typeSelection;
    }

}