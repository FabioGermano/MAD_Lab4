package it.polito.mad_lab3.restaurant.menu;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import it.polito.mad_lab3.BaseActivity;
import it.polito.mad_lab3.R;
import it.polito.mad_lab3.common.photo_viewer.TouchImageView;
import it.polito.mad_lab3.data.restaurant.Dish;

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

    private void getDish(Bundle extras) {
        this.dish = (Dish)extras.getSerializable("dish");

        touchImageView.setImageDrawable(getResources().getDrawable(R.drawable.cibo4));
        dishNameText.setText(dish.getDishName());
        ratingBar.setRating(dish.getAvgRank());
        priceTV.setText(dish.getPrice()+"€");
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
