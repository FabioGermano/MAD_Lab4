package it.polito.mad_lab4.manager.photo_viewer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.soundcloud.android.crop.Crop;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import it.polito.mad_lab4.R;

/**
 * Created by f.germano on 07/04/2016.
 */
public class PhotoViewer extends Fragment  implements PhotoDialogListener {

    private ImageView imgPhoto;
    private ImageButton editButton;

    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private final int VIEW_PHOTO = 2;
    private int initialImage = -1, widthInDP, heightInDP;
    private boolean isBitmapSetted = false, isLogo = false, isEditable, isAddByClickOnImage;
    private String pictureImagePath;
    private boolean isPhotoClicked = false;

    private boolean cameraAllowed = false;
    private boolean storageAllowed = false;

    private Bitmap thumb, large;
    private String largePhotoDownloadLink;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public Bitmap getLarge() {
        return large;
    }

    public void setLarge(Bitmap large) {
        this.large = large;
    }

    public PhotoViewer()
    {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.isPhotoClicked = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.photo_viewer, container, false);

        this.imgPhoto = (ImageView)rootView.findViewById(R.id.epImgPhoto);
        setSizeInDP(this.widthInDP, this.heightInDP);
        setInitialImage(savedInstanceState);

        this.editButton = (ImageButton)rootView.findViewById(R.id.epEditButton);
        if(this.isEditable == false) {
            this.editButton.setVisibility(View.GONE);
        }

        this.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButtonPressed();
            }
        });
        if(this.isAddByClickOnImage) {
            this.editButton.setVisibility(View.GONE);
        }

        this.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoPressed();
            }
        });

        return rootView;
    }

    private void setInitialImage(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            Bitmap thumb = savedInstanceState.getParcelable("thumbImage");
            if(thumb != null)
            {
                this.imgPhoto.setImageBitmap(thumb);
                SetIsBitmapSetted(true);
            }
        }
        else
        {
            // consider attributes
            if(this.initialImage != -1)
            {
                this.imgPhoto.setImageResource(this.initialImage);
            }
        }
    }

    /**
     * Parse attributes during inflation from a view hierarchy into the arguments we handle.
     */
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PhotoViewer);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.PhotoViewer_initialBackground:
                    this.initialImage = a.getResourceId(attr, -1);
                    break;
                case R.styleable.PhotoViewer_isLogo:
                    this.isLogo = a.getBoolean(attr, false);
                    break;
                case R.styleable.PhotoViewer_heightInDP:
                    this.heightInDP = a.getInt(attr, 200);
                    break;
                case R.styleable.PhotoViewer_widthInDP:
                    this.widthInDP = a.getInt(attr, 200);
                    break;
                case R.styleable.PhotoViewer_isEditable:
                    this.isEditable = a.getBoolean(attr, false);
                    break;
                case R.styleable.PhotoViewer_addImageMode:
                    String value = a.getString(attr);
                    if(value.equals("clickOnButton"))
                    {
                        this.isAddByClickOnImage = false;
                    }
                    else if (value.equals("clickOnImage"))
                    {
                        this.isAddByClickOnImage = true;
                    }
                    else
                    {
                        this.isAddByClickOnImage = false;
                    }
                    break;
            }
        }

        a.recycle();
    }

    private void editButtonPressed()
    {
        PhotoDialog dialog = new PhotoDialog(getContext(), this.isBitmapSetted);
        dialog.addListener(this);
    }

    private void photoPressed()
    {
        if(this.isAddByClickOnImage && !this.isBitmapSetted)
        {
            editButtonPressed(); // add a photo that actually doas not yet exist
        }
        else if(!this.isLogo && this.isBitmapSetted && !this.isPhotoClicked)
        {
            if(largePhotoDownloadLink == null) {
                this.isPhotoClicked = true;

                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut = null;
                File file = new File(path, "image-tran.jpg");
                try {
                    fOut = new FileOutputStream(file);

                    large.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                    fOut.flush();
                    fOut.close();

                    Intent intent = new Intent(getActivity(), it.polito.mad_lab4.manager.photo_viewer.PhotoViewActivity.class);
                    intent.putExtra("photoPath", file.getAbsolutePath());
                    intent.putExtra("isEditable", this.isEditable);

                    startActivityForResult(intent, VIEW_PHOTO);

                } catch (FileNotFoundException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                } catch (IOException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                }
            }
            else{
                Intent intent = new Intent(getActivity(), it.polito.mad_lab4.manager.photo_viewer.PhotoViewActivity.class);
                intent.putExtra("largePhotoDownloadLink", largePhotoDownloadLink);
                intent.putExtra("isEditable", this.isEditable);

                startActivityForResult(intent, VIEW_PHOTO);
            }
        }
    }

    /**
     * To be called in order to set the bitmap (to be intended as a thumb one - of little dimensions) to the ImageView.
     *
     * @param bitmap
     */
    public void setThumbBitmap(Bitmap bitmap) {
        if(bitmap == null && this.initialImage != -1)
        {
            this.imgPhoto.setImageResource(this.initialImage);
        }
        else
        {
            this.imgPhoto.setImageBitmap(bitmap);
            SetIsBitmapSetted(true);
        }
    }

    /**
     * To be called in order to set the bitmap (to be intended as a thumb one - of little dimensions) to the ImageView
     * given the download image link
     *
     * @param url
     */
    public void setThumbBitmapByURI(String url) {
        SetIsBitmapSetted(true);
        Glide.with(this).load(url).into(this.imgPhoto);
    }

    public void setSizeInDP(int DP_width, int DP_height)
    {
        final float scale = getResources().getDisplayMetrics().density;
        int dpWidthInPx  = (int) (DP_width * scale);
        int dpHeightInPx = (int) (DP_height * scale);
        this.imgPhoto.getLayoutParams().width = dpWidthInPx;
        this.imgPhoto.getLayoutParams().height = dpHeightInPx;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 6709 && resultCode == Activity.RESULT_OK){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(data));
                thumb = resizeBitmap(bitmap, 196);
                if(!this.isLogo) {
                    large = resizeBitmap(bitmap, 1024);
                    //bitmap.recycle();
                    this.setThumbBitmap(thumb);
                }
                else{
                    this.setThumbBitmap(thumb);
                }

                this.largePhotoDownloadLink = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == VIEW_PHOTO && resultCode == Activity.RESULT_OK){
            boolean toBeDeleted = data.getBooleanExtra("toBeDeteted", false);
            if(toBeDeleted) {
                SetIsBitmapSetted(false);
                this.imgPhoto.setImageResource(initialImage);
            }
            return;
        }

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            File imgFile = new  File(pictureImagePath);
            if(imgFile.exists()){
                Uri destination = Uri.fromFile(new File(getActivity().getExternalCacheDir(), "cropped"));
                Crop.of(Uri.parse( imgFile.toURI().toString()), destination).asSquare().start(getActivity(), this);

                this.largePhotoDownloadLink = null;
            }
        }
        else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Uri destination = Uri.fromFile(new File(getActivity().getExternalCacheDir(), "cropped"));
            Crop.of(imageUri, destination).asSquare().start(getActivity(), this);

            this.largePhotoDownloadLink = null;
        }
    }

    private Bitmap resizeBitmap(Bitmap bitMap, int new_size)
    {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        int scale = 1;

        while (width / scale >= new_size && height / scale >= new_size)
            scale *= 2;

        width = width / scale;
        height = height / scale;

        return Bitmap.createScaledBitmap(bitMap, width, height, true);
    }

    /* dialog management */
    @Override
    public void OnCameraButtonPressed() {
        manageCameraForLargePhoto();
    }

    private void manageCameraForLargePhoto()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkCameraPermission();
        else
            cameraAllowed = true;

        if (cameraAllowed) {
            String imageFileName = "temp.jpg";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
            File file = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }
    }

    @Override
    public void OnGalleryButtonPressed() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            checkStoragePermission();
        else
            storageAllowed = true;

        if (storageAllowed) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FILE);
        }

    }

    @Override
    public void OnRemoveButtonListener() {
        SetIsBitmapSetted(false);
        this.imgPhoto.setImageResource(initialImage);
    }
    /* end dialog management */

    @Override
    public void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if(this.imgPhoto.getDrawable() instanceof BitmapDrawable ){
            outState.putParcelable("thumbImage", ((BitmapDrawable) this.imgPhoto.getDrawable()).getBitmap());
        }
        else {
            outState.putParcelable("thumbImage", ((GlideBitmapDrawable) this.imgPhoto.getDrawable().getCurrent()).getBitmap());
        }
    }

    private void SetIsBitmapSetted(boolean setted)
    {
        this.isBitmapSetted = setted;

        if(this.isAddByClickOnImage)
        {
            if(!setted)
            {
                this.editButton.setVisibility(View.GONE);
            }
            else
            {
                this.editButton.setVisibility(View.VISIBLE);
            }
        }
    }
    private void checkCameraPermission(){
        int camera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (camera != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        }
        else
            cameraAllowed = true;
    }

    private void checkStoragePermission(){
        int storage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storage != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        else
            storageAllowed = true;

    }

    public boolean isImageTobeSetted(){
        return !this.isBitmapSetted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    cameraAllowed = true;

                } else {
                    cameraAllowed = false;
                }
                break;
            }
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    storageAllowed = true;
                } else {
                    storageAllowed = false;
                }
                break;
            }
        }
    }

    public void setLargePhotoDownloadLink(String largePhotoDownloadLink) {
        this.largePhotoDownloadLink = largePhotoDownloadLink;
    }
}
