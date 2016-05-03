package it.polito.mad_lab3.restaurant;

import android.graphics.Bitmap;
import android.os.Bundle;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.photo_manager.PhotoManager;
import it.polito.mad_lab3.common.photo_manager.PhotoType;
import it.polito.mad_lab3.common.photo_viewer.PhotoViewer;
import it.polito.mad_lab3.common.photo_viewer.PhotoViewerListener;

/**
 * Created by Giovanna on 03/05/2016.
 */
public class ShowOfferActivity extends BaseActivity implements PhotoViewerListener{

    private String imageLarge = null;
    private String imageThumb = null;
    private PhotoManager photoManager;
    private PhotoViewer photoViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        setActivityTitle("Offer name");

        photoViewer = (PhotoViewer)getSupportFragmentManager().findFragmentById(R.id.photoViewer);
        photoManager = new PhotoManager(getApplicationContext(), PhotoType.OFFER, this.imageThumb, this.imageLarge);

    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    @Override
    public void OnPhotoChanged(int fragmentId, Bitmap thumb, Bitmap large) {

    }

    @Override
    public Bitmap OnPhotoViewerActivityStarting(int fragmentId) {
        return null;
    }

    @Override
    public void OnPhotoRemoved(int fragmentId) {

    }
}
