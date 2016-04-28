package it.polito.mad_lab3.restaurant.gallery;

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

import java.util.ArrayList;
import java.util.List;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.UserPhoto;

/**
 * Created by f.germano on 28/04/2016.
 */
public class PhotoGalleryAdapter extends BaseAdapter {

    private List<UserPhoto> mItems = new ArrayList<UserPhoto>();
    private LayoutInflater mInflater;
    private PhotoGalleryListener photoGalleryListener = null;

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
    public long getItemId(int i) {
        return mItems.get(i).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView picture;
        TextView name;

        if (v == null) {
            v = mInflater.inflate(R.layout.gallery_grid_item, parent, false);
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    photoClick(getItem(position));
                }
            });
        }

        picture = (ImageView) v.findViewById(R.id.galleryPhoto);
        name = (TextView) v.findViewById(R.id.galleryPhotoText);

        UserPhoto item = getItem(position);

        picture.setImageBitmap(BitmapFactory.decodeFile(item.getThumbPath()));
        if(item.getLikes() == 0){
            name.setText("Still no likes");
        }
        else{
            name.setText(String.valueOf(item.getLikes())+" likes");
        }

        return v;
    }
}