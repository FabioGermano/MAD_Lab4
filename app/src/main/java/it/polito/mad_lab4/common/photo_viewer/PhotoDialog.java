package it.polito.mad_lab4.common.photo_viewer;

/**
 * Created by f.germano on 27/04/2016.
 */
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad_lab4.R;

/**
 * Created by f.germano on 07/04/2016.
 */
public class PhotoDialog implements DialogInterface.OnClickListener{

    private final CharSequence[] items;
    private List<PhotoDialogListener> listeners = new ArrayList<PhotoDialogListener>();

    public PhotoDialog(Context context, boolean isPhotoSetted){

        List<String> values = new ArrayList<String>();

        values.add(context.getResources().getString(R.string.take_photo));
        values.add(context.getResources().getString(R.string.chose_library));
        if(isPhotoSetted)
        {
            values.add(context.getResources().getString(R.string.remove_photo));
        }

        items = values.toArray(new CharSequence[values.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.add_photo));
        builder.setItems(items, this);
        builder.show();
    }

    public void addListener(PhotoDialogListener listener)
    {
        this.listeners.add(listener);
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        if (items[item].equals(items[0])) {
            for(PhotoDialogListener i : listeners) i.OnCameraButtonPressed();
        } else if (items[item].equals(items[1])) {
            for(PhotoDialogListener i : listeners) i.OnGalleryButtonPressed();
        } else if (items[item].equals(items[2])) {
            for(PhotoDialogListener i : listeners) i.OnRemoveButtonListener();
        }
    }

}