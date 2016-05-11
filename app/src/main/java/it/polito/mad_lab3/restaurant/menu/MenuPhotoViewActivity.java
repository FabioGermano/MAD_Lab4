package it.polito.mad_lab3.restaurant.menu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.Helper;
import it.polito.mad_lab3.common.photo_viewer.TouchImageView;
import it.polito.mad_lab3.data.restaurant.Dish;
import it.polito.mad_lab3.data.user.User;

public class MenuPhotoViewActivity extends BaseActivity {

    private Dish dish;
    private TouchImageView touchImageView;
    private TextView dishNameText;
    private RatingBar ratingBar;
    private TextView priceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_photo_view);

        hideToolbar(true);
        hideToolbarShadow(true);

        touchImageView = (TouchImageView)findViewById(R.id.photoView);
        dishNameText = (TextView)findViewById(R.id.dishNameText);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        priceTV = (TextView)findViewById(R.id.priceTV);

        getDish(getIntent().getExtras());
    }

    @Override
    protected void filterButton() {

    }

    private void getDish(Bundle extras) {
        this.dish = (Dish)extras.getSerializable("dish");

        if(dish.getLargePath() != null) {
            touchImageView.setImageBitmap(BitmapFactory.decodeFile(dish.getThumbPath()));
        }
        else if(dish.getResPhoto() != null) {
            int imgRes = Helper.getResourceByName(getApplicationContext(), dish.getResPhoto(), "drawable");
            if (imgRes != 0) {
                touchImageView.setImageResource(imgRes);
            }
        }

        dishNameText.setText(dish.getDishName());
        ratingBar.setRating(dish.getAvgRank());
        priceTV.setText(dish.getPrice()+"€");

        Helper.setRatingBarColor(getApplicationContext(),
                ratingBar,
                dish.getAvgRank());
    }

    @Override
    protected User controlloLogin() {
        return new User(null, null, -1);
    }

    @Override
    protected void ModificaProfilo() {

    }

    @Override
    protected void ShowPrenotazioni() {

    }

    private void setDeleteVisibility(Bundle savedInstanceState)
    {
        boolean editable = savedInstanceState.getBoolean("isEditable");
    }
}
