package it.polito.mad_lab4.manager.photo_viewer;

import android.graphics.Bitmap;

/**
 * Created by f.germano on 06/04/2016.
 */
public interface PhotoViewerListener {

    /**
     * Invoked when photo changes. If the "isLogo" attribute is setted to true, then the input argument
     * will be only "thumb" ("large" setted to null). Otherwise, both will have a value.
     *
     * @param thumb Reduced-size version of the photo. This one has to be setted in the ImageView control
     *              by calling the setThumbBitmap method.
     * @param large Original full size photo.
     */
    void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large);
    Bitmap OnPhotoViewerActivityStarting(int fragmentId);
    void OnPhotoRemoved(int fragmentId);
}
