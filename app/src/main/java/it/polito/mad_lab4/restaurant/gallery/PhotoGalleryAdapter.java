package it.polito.mad_lab4.restaurant.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.newData.restaurant.UserPhoto;

/**
 * Created by f.germano on 28/04/2016.
 */
public class PhotoGalleryAdapter extends BaseAdapter {

    private List<UserPhoto> mItems = new ArrayList<UserPhoto>();
    private LayoutInflater mInflater;
    private PhotoGalleryListener photoGalleryListener = null;
    private Context context;

    public PhotoGalleryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setUserPhotos(List<UserPhoto> userPhotos){
        this.mItems = userPhotos;
    }

    public void setListener(PhotoGalleryListener listener){
        this.photoGalleryListener = listener;
    }

    private void photoClick(UserPhoto userPhoto) {
        this.photoGalleryListener.OnPhotoClick(userPhoto);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public UserPhoto getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
        //return mItems.get(i).getUserPhotoId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView picture;
        TextView name;

        final int _pos = position;

        if (v == null) {
            v = mInflater.inflate(R.layout.gallery_grid_item, parent, false);
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    photoClick(getItem(_pos));
                }
            });
        }

        picture = (ImageView) v.findViewById(R.id.galleryPhoto);
        name = (TextView) v.findViewById(R.id.galleryPhotoText);

        UserPhoto item = getItem(position);

        Glide.with(context).load(item.getThumbDownloadLink()).into(picture);

        if(item.getLikes() == 0){
            name.setText("Still no likes");
        }
        else{
            name.setText(String.valueOf(item.getLikes())+" likes");
        }

        return v;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}