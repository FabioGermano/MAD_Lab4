package it.polito.mad_lab4.elaborazioneRicerche;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.polito.mad_lab4.R;
import it.polito.mad_lab4.restaurant.RestaurantActivity;

/**
 * Created by Euge on 29/04/2016.
 */
public class RecyclerAdapter_risultatoRicerca extends RecyclerView.Adapter<RecyclerAdapter_risultatoRicerca.MyViewHolder_risultatoRicerca> {

    private Context context;
    private ArrayList<Oggetto_risultatoRicerca> risultati_ricerca;
    private LayoutInflater myInflater;


    public RecyclerAdapter_risultatoRicerca(Context context, ArrayList<Oggetto_risultatoRicerca> list){
        this.context = context;
        this.risultati_ricerca = list;
        this.myInflater = LayoutInflater.from(context);
    }

    public void setNewArray(ArrayList<Oggetto_risultatoRicerca> listObj){
        this.risultati_ricerca = listObj;
    }

    @Override
    public MyViewHolder_risultatoRicerca onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.riga_lista_ricerche, parent, false);
        MyViewHolder_risultatoRicerca holder = new MyViewHolder_risultatoRicerca(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder_risultatoRicerca holder, int position) {
        Oggetto_risultatoRicerca currentObj = risultati_ricerca.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return risultati_ricerca.size();
    }

    @Override
    public void onViewRecycled (MyViewHolder_risultatoRicerca holder){
        holder.cancellaBitmap();
    }

    public class MyViewHolder_risultatoRicerca extends RecyclerView.ViewHolder {
        private int position;
        private Oggetto_risultatoRicerca current;
        private Context context;

        private TextView nome;
        private TextView descrizione;
        private TextView place;
        private TextView costo;
        private RatingBar valutazione;
        private ImageView imgView;

        private Bitmap bitmap;

        public MyViewHolder_risultatoRicerca(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    visualizzaRistorante();
                }
            });

            nome = (TextView) itemView.findViewById(R.id.name_ricerca);
            descrizione = (TextView) itemView.findViewById(R.id.desc_ricerca);
            costo = (TextView) itemView.findViewById(R.id.cost_ricerca);
            valutazione = (RatingBar) itemView.findViewById(R.id.rating_ricerca);
            imgView = (ImageView) itemView.findViewById(R.id.img_ricerca);
            place = (TextView) itemView.findViewById(R.id.place_ricerca);
        }

        public void setData(Oggetto_risultatoRicerca currentObj, int position) {
            this.current = currentObj;
            this.position = position;
            if(nome != null){
                nome.setText(currentObj.getName());
            }
            if(place != null){
                place.setText(currentObj.getPlace());
            }
            if(descrizione != null && currentObj.getDescrizione() != null){
                descrizione.setText(currentObj.getDescrizione());
            }
            if(costo != null && currentObj.getFasciaPrezzo() != null){
                costo.setText(currentObj.getFasciaPrezzo());
            }
            if(valutazione != null){
                float val = currentObj.getValutazione();
                Drawable stars = valutazione.getProgressDrawable();
                if(val <= 1.5){
                    DrawableCompat.setTint(stars, context.getResources().getColor(R.color.bad));
                } else if(val <= 3.5){
                    DrawableCompat.setTint(stars, context.getResources().getColor(R.color.medium));
                } else {
                    DrawableCompat.setTint(stars, context.getResources().getColor(R.color.good));
                }

                valutazione.setRating(val);
            }

            //carico foto
            if(imgView != null){
                String path = currentObj.getImage_path();
                if (path != null){
                    try {
                        Glide.with(context).load(path).into(imgView);
                    } catch (Exception e){
                        System.out.println("Errore creazione bitmap");  //debug
                    }
                }
            }
        }

        private void cancellaBitmap(){
            if(bitmap != null)
                bitmap.recycle();
        }

        private void visualizzaRistorante(){
            Bundle b = new Bundle();
            b.putString("restaurantId", current.getId());
            Intent intent = new Intent(context, RestaurantActivity.class);
            intent.putExtras(b);
            context.startActivity(intent);
        }

    }
}
