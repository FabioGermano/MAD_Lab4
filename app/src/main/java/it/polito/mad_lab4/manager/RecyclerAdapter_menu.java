package it.polito.mad_lab4.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.bl.RestaurantBL;
import it.polito.mad_lab4.firebase_manager.FirebaseRemoveDishManager;
import it.polito.mad_lab4.newData.restaurant.Dish;
import it.polito.mad_lab4.data.restaurant.DishType;

/**
 * Created by Euge on 05/04/2016.
 */
public class RecyclerAdapter_menu extends RecyclerView.Adapter<RecyclerAdapter_menu.MyViewHolder>{
    private LayoutInflater myInflater;
    private DishType menu_type;
    //accesso veloce alla lista in esame ??
    private ArrayList<Dish> current_list;
    private boolean availability_mode;


    public RecyclerAdapter_menu(Context context, ArrayList<Dish> data, DishType type, boolean availability_mode){
        this.current_list = data;
        this.availability_mode=availability_mode;
        myInflater = LayoutInflater.from(context);
        this.menu_type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.riga_lista, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        holder.setListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Dish currentObj = current_list.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return current_list.size();

    }


    class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        private Dish current;
        private int position;
        private ImageView dish_img;
        private TextView dish_name;
        private TextView dish_price;
        private ImageButton dish_delete;
        private ImageButton dish_modify;
        private Switch dish_availability;
        private CardView cardView;
        private Context context;


        public MyViewHolder(View itemView) {
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

        public void setData(Dish currentObj, int position) {
            this.position = position;
            this.current = currentObj;
            if(dish_name != null)
                this.dish_name.setText(currentObj.getDishName());
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
            if (availability_mode)
                dish_availability.setOnClickListener(MyViewHolder.this);
            else {
                dish_delete.setOnClickListener(MyViewHolder.this);
                //dish_modify.setOnClickListener(MyViewHolder.this);
                cardView.setOnClickListener(MyViewHolder.this);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_delete_menu:
                    removeItem();
                    break;
                //case R.id.img_modify_menu:
                  //  modifyItem();
                    //break;
                case R.id.cardView:
                    modifyItem();
                    break;
                case R.id.switch1:
                    updateAvailability();
                    break;
            }
        }

        private void updateAvailability(){
            try {
                current_list.get(position).setTodayAvailable(dish_availability.isChecked());
            }
            catch (Exception e){
                System.out.println("Eccezione: " + e.getMessage());
            }


        }

        //rimuovo piatto
        private void removeItem(){
            try {
                FirebaseRemoveDishManager firebaseRemoveDishManager = new FirebaseRemoveDishManager();
                firebaseRemoveDishManager.removeDish("-KIrgaSxr9VhHllAjqmp", current_list.get(position).getDishId());
            } catch (Exception e){
                System.out.println("Eccezione: " + e.getMessage());
            }
        }

        //modifico piatto
        private void modifyItem(){

            Bundle b = new Bundle();
            b.putString("restaurantId", "-KIrgaSxr9VhHllAjqmp");
            b.putString("dishId",  current_list.get(position).getDishId());//current_list.get(position).getDishId());
            b.putBoolean("isEditing", true);
            Intent intent = new Intent(context, ModifyMenuDish.class);
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }
}
