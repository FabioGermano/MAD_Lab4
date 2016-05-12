package it.polito.mad_lab3.restaurant.foodPhoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.Restaurant;
import it.polito.mad_lab3.data.restaurant.UserPhoto;
import it.polito.mad_lab3.restaurant.gallery.GalleryPhotoViewActivity;
import it.polito.mad_lab3.restaurant.gallery.PhotoGaleryActivity;

/**
 * Created by f.germano on 23/04/2016.
 */
public class UserPhotoFragment extends Fragment {
    private ImageView foodIV;
    private TextView likesTV;
    private RelativeLayout trasparentContainer;
    private boolean photoSetted, openGalleryOnClick;
    private Restaurant restaurant = null;
    private UserPhoto userPhoto;
    private boolean latest;

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

        this.foodIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPhotoClick();
                }
            });

        return rootView;
    }

    private void onPhotoClick() {
        if(this.openGalleryOnClick && this.restaurant != null){ // apro galleria
            Intent i = new Intent(getContext(), PhotoGaleryActivity.class);
            Bundle b = new Bundle();
            b.putInt("restaurantId", this.restaurant.getRestaurantId());
            i.putExtras(b);
            startActivity(i);
        }
        else if(!this.openGalleryOnClick && this.restaurant != null && this.userPhoto != null){ // apro dettaglio photo
            Intent intent = new Intent(getActivity().getApplicationContext(), GalleryPhotoViewActivity.class);
            intent.putExtra("photoPath", userPhoto.getLargePath());
            intent.putExtra("isEditable", false);
            intent.putExtra("userPhoto", userPhoto);
            intent.putExtra("restaurantId", this.restaurant.getRestaurantId());

            startActivity(intent);
        }
    }

    /**
     * Parse attributes during inflation from a view hierarchy into the arguments we handle.
     */
    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        /*TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.UserPhotoFragment);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.UserPhotoFragment_latest:
                    this.isLatest = a.getBoolean(attr, false);
                    break;
            }
        }*/
    }

    public void setImage(){
        Bitmap bitmap = BitmapFactory.decodeFile(this.userPhoto.getThumbPath());

        this.foodIV.setImageBitmap(bitmap);
        this.photoSetted = true;

        this.likesTV.setVisibility(View.VISIBLE);
    }

    @SuppressLint("NewApi") // to work on it
    public void setLatestLabel(){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)trasparentContainer.getLayoutParams();
            params.height = params.width;
            params.addRule(RelativeLayout.ALIGN_TOP, R.id.foodIV);
            params.removeRule(RelativeLayout.ALIGN_BOTTOM);
            trasparentContainer.setLayoutParams(params); //causes layout update

            trasparentContainer.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

            this.likesTV.setTextSize(40);
    }

    public void setRemainingNumber(int n) throws Exception {
        this.likesTV.setText("+" + String.valueOf(n));
        this.latest = true;
    }

    public void setLikes(int n) throws Exception {
        if(latest){
            return;
        }

        if(n == 0){
            this.likesTV.setText("Still no likes");
        }
        else{
            this.likesTV.setText(String.valueOf(n)+" likes");
        }
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

    public void setUserPhoto(UserPhoto userPhoto) {
        this.userPhoto = userPhoto;
        try {
            setLikes(userPhoto.getLikes());
        } catch (Exception e) {
            Log.d(e.getMessage(), e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(userPhoto != null){
            try {
                setLikes(userPhoto.getLikes());
            } catch (Exception e) {
                Log.d(e.getMessage(), e.getMessage());
            }
        }
    }
}
