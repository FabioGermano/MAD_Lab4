package it.polito.mad_lab3.restaurant.foodPhoto;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.restaurant.gallery.PhotoGaleryActivity;

/**
 * Created by f.germano on 23/04/2016.
 */
public class UserPhotoFragment extends Fragment {
    private ImageView foodIV;
    private TextView likesTV;
    private boolean isLatest;
    private RelativeLayout trasparentContainer;
    private boolean photoSetted, openGalleryOnClick;
    private Restaurant restaurant = null;

    public UserPhotoFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.food_photo_view, container, false);

        this.foodIV = (ImageView) rootView.findViewById(R.id.foodIV);
        this.likesTV = (TextView)rootView.findViewById(R.id.likeTV);
        this.trasparentContainer = (RelativeLayout)rootView.findViewById(R.id.trasparentContainer);

        this.foodIV.setImageResource(R.drawable.nothumb);
        this.likesTV.setVisibility(View.GONE);
        this.likesTV.setText("Still no likes");

        if(this.isLatest){
            this.foodIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLatestClick();
                }
            });
        }

        return rootView;
    }

    private void onLatestClick() {
        if(this.openGalleryOnClick && this.restaurant != null){
            Intent i = new Intent(getContext(), PhotoGaleryActivity.class);
            startActivity(i);
        }
    }

    /**
     * Parse attributes during inflation from a view hierarchy into the arguments we handle.
     */
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UserPhotoFragment);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.UserPhotoFragment_latest:
                    this.isLatest = a.getBoolean(attr, false);
                    break;
            }
        }
    }

    public void setImage(Bitmap bitmap){
        this.foodIV.setImageBitmap(bitmap);
        this.photoSetted = true;

        this.likesTV.setVisibility(View.VISIBLE);
    }

    public void setLatestLabel(){
        if(this.isLatest){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)trasparentContainer.getLayoutParams();
            params.height = params.width;
            params.addRule(RelativeLayout.ALIGN_TOP, R.id.foodIV);
            params.removeRule(RelativeLayout.ALIGN_BOTTOM);
            trasparentContainer.setLayoutParams(params); //causes layout update

            trasparentContainer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            this.likesTV.setTextSize(40);
        }
    }

    public void setRemainingNumber(int n) throws Exception {
        if(!this.isLatest){
            throw new Exception("The photo must be latest one.");
        }

        this.likesTV.setText("+" + String.valueOf(n));
    }

    public void setLikes(int n) throws Exception {
        if(this.isLatest){
            throw new Exception("The photo must not be latest one.");
        }

        this.likesTV.setText(String.valueOf(n) + " likes on this.");
    }

    public boolean isPhotoSetted(){
        return this.photoSetted;
    }

    public void setOpenGalleryOnClick(boolean b) {
        this.openGalleryOnClick = b;
    }

    public void init(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
