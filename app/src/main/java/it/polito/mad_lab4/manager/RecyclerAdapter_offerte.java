package it.polito.mad_lab4.manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by Euge on 08/04/2016.
 */
public class RecyclerAdapter_offerte extends RecyclerView.Adapter<RecyclerAdapter_offerte.MyViewHolder_offerta> {
    private ArrayList<Offer> lista_offerte;
    private LayoutInflater myInflater;
    private boolean availability_mode;

    public RecyclerAdapter_offerte(Context context, ArrayList<Offer> data, boolean availability_mode) {
        this.lista_offerte = data;
        myInflater = LayoutInflater.from(context);
        this.availability_mode = availability_mode;
    }

    @Override
    public MyViewHolder_offerta onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.riga_lista, parent, false);
        MyViewHolder_offerta holder = new MyViewHolder_offerta(v);
        holder.setListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder_offerta holder, int position) {
        Offer currentObj = lista_offerte.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return lista_offerte.size();
    }


    class MyViewHolder_offerta extends RecyclerView.ViewHolder implements View.OnClickListener {

        private int position;
        private Offer current;
        private ImageView dish_img;
        private TextView dish_name;
        private TextView dish_price;
        private ImageButton dish_delete;
        private ImageButton dish_modify;
        private Switch dish_availability;
        private Context context;
        private CardView cardView;

        public MyViewHolder_offerta(View itemView) {
            super(itemView);
            dish_img = (ImageView) itemView.findViewById(R.id.image_dish_menu);
            dish_name = (TextView) itemView.findViewById(R.id.dish_name_menu);
            dish_price = (TextView) itemView.findViewById(R.id.dish_price_menu);
            dish_delete = (ImageButton) itemView.findViewById(R.id.img_delete_menu);
            dish_modify = (ImageButton) itemView.findViewById(R.id.img_modify_menu);
            dish_availability = (Switch) itemView.findViewById(R.id.switch1);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            dish_modify.setVisibility(View.GONE);
            if(availability_mode){
                dish_delete.setVisibility(View.GONE);
                dish_modify.setVisibility(View.GONE);
                dish_availability.setVisibility(View.VISIBLE);

            }

            context = itemView.getContext();
        }

        public void setData(Offer currentObj, int position) {
            this.position = position;
            this.current = currentObj;
            if(dish_name != null)
                this.dish_name.setText(currentObj.getOfferName());
            if(dish_price != null) {
                String tmp = String.valueOf(currentObj.getPrice()) + " " + context.getResources().getString(R.string.money_value);
                this.dish_price.setText(tmp);
            }
            if(availability_mode){
                if(dish_availability != null){
                    dish_availability.setChecked(currentObj.getTodayAvailable());
                }
            }
            if(currentObj.getThumbDownloadLink() != null) {
                Glide.with(context).load(currentObj.getThumbDownloadLink()).into(dish_img);
            }

        }

        public void setListeners(){
            if(availability_mode){
                dish_availability.setOnClickListener(MyViewHolder_offerta.this);
            } else {
                dish_delete.setOnClickListener(MyViewHolder_offerta.this);
                //dish_modify.setOnClickListener(MyViewHolder.this);
                cardView.setOnClickListener(MyViewHolder_offerta.this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_delete_menu:
                    removeItem();
                    break;
                case R.id.cardView:
                    modifyItem();
                    break;

                case R.id.switch1:
                    updateAvailability();
                    break;
            }
        }

        private void updateAvailability(){
            lista_offerte.get(position).setTodayAvailable(dish_availability.isChecked());
        }

        //rimuovo offerta
        private void removeItem(){
            lista_offerte.remove(position);

            RestaurantBL.saveChanges(context);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, lista_offerte.size());
        }

        //modifico offerta
        private void modifyItem(){
            Bundle b = new Bundle();
            b.putString("restaurantId", "-KIrgaSxr9VhHllAjqmp");
            b.putString("offerId", lista_offerte.get(position).getOfferId());
            b.putBoolean("isEditing", true);

            Intent intent = new Intent(context, ModifyOfferDish.class);
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }
}
