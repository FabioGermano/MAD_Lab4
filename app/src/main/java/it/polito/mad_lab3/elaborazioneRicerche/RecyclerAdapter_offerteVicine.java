package it.polito.mad_lab3.elaborazioneRicerche;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.polito.mad_lab3.R;

/**
 * Created by Euge on 10/05/2016.
 */
public class RecyclerAdapter_offerteVicine extends RecyclerView.Adapter<RecyclerAdapter_offerteVicine.MyViewHolder_offerteVicine>  {
    private ArrayList<String> lista_offerte_vicine;
    private LayoutInflater myInflater;
    private Context context;

    public RecyclerAdapter_offerteVicine(Context context, ArrayList<String> lista){
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
        String currentObj = lista_offerte_vicine.get(position);
        holder.setData(currentObj, position);
    }

    @Override
    public int getItemCount() {
        return this.lista_offerte_vicine.size();
    }

    public class MyViewHolder_offerteVicine extends RecyclerView.ViewHolder {
        private int position;
        private String current;
        private Context context;

        public MyViewHolder_offerteVicine(View itemView) {
            super(itemView);
            context = itemView.getContext();


        }

        public void setData(String currentObj, int position) {
            this.current = currentObj;
            this.position = position;

        }
    }
}
