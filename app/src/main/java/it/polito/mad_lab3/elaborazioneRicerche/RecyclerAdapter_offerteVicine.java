package it.polito.mad_lab3.elaborazioneRicerche;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.restaurant.RestaurantActivity;

/**
 * Created by Euge on 10/05/2016.
 */
public class RecyclerAdapter_offerteVicine extends RecyclerView.Adapter<RecyclerAdapter_offerteVicine.MyViewHolder_offerteVicine>  {
    private ArrayList<Oggetto_offerteVicine> lista_offerte_vicine;
    private LayoutInflater myInflater;
    private Context context;

    public RecyclerAdapter_offerteVicine(Context context, ArrayList<Oggetto_offerteVicine> lista){
        this.lista_offerte_vicine = lista;
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerAdapter_offerteVicine.MyViewHolder_offerteVicine onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = myInflater.inflate(R.layout.riga_lista_offerte_vicine, parent, false);
        MyViewHolder_offerteVicine holder = new MyViewHolder_offerteVicine(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_offerteVicine.MyViewHolder_offerteVicine holder, int position) {
        Oggetto_offerteVicine currentObj = lista_offerte_vicine.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return this.lista_offerte_vicine.size();
    }

    @Override
    public void onViewRecycled(MyViewHolder_offerteVicine holder) {
            holder.cancellaBitmap();
    }

    public class MyViewHolder_offerteVicine extends RecyclerView.ViewHolder {
        private int position;
        private Oggetto_offerteVicine current;
        private Context context;

        private TextView name;
        private TextView price;
        private TextView details;
        private RatingBar rating;
        private TextView numRatings;
        private ImageView image;

        private Bitmap bitmap;


        public MyViewHolder_offerteVicine(View itemView) {
            super(itemView);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    visualizzaRistorante();
                }
            });


            name = (TextView) itemView.findViewById(R.id.offerName);
            price = (TextView) itemView.findViewById(R.id.offerPrice);
            rating = (RatingBar) itemView.findViewById(R.id.offerRatingProgress);
            details = (TextView) itemView.findViewById(R.id.offerDetails);
            numRatings = (TextView) itemView.findViewById(R.id.offerNumRatings);
            image = (ImageView) itemView.findViewById(R.id.foodIV);
        }

        public void setData(Oggetto_offerteVicine currentObj, int position) {
            this.current = currentObj;
            this.position = position;

            if (this.name != null){
                this.name.setText(currentObj.getOffer().getOfferName());
            }
            if (this.price != null){
                this.price.setText(String.valueOf(currentObj.getOffer().getPrice()));
            }
            if (this.details != null){
                this.details.setText(currentObj.getOffer().getDetails());
            }
            if (this.rating != null){
                this.rating.setRating(currentObj.getOffer().getAvgRank());
            }

            if (this.numRatings != null){
                String n = "("+currentObj.getOffer().getNumRanks()+")";
                this.numRatings.setText(n);
            }

            if(this.image != null){
                String path = currentObj.getOffer().getLargePath();
                if (path != null){
                    try {
                        bitmap = BitmapFactory.decodeFile(path);
                        if(bitmap != null)
                            image.setImageBitmap(bitmap);
                    } catch (Exception e){
                        System.out.println("Errore creazione bitmap"); //debug
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
            b.putInt("restaurantId", (current.getId()));
            Intent intent = new Intent(context, RestaurantActivity.class);
            intent.putExtras(b);
            context.startActivity(intent);
        }
    }
}
