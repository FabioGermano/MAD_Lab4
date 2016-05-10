package it.polito.mad_lab3.elaborazioneRicerche;

import java.io.Serializable;

import it.polito.mad_lab3.data.restaurant.Offer;

/**
 * Created by Roby on 10/05/2016.
 */
public class Oggetto_offerteVicine implements Serializable{
    private Offer offerta;
    private int restaurantId;


    public Oggetto_offerteVicine(Offer o, int id){
        this.offerta = o;
        this.restaurantId = id;
    }

    public Offer getOffer(){ return offerta; }
    public int getId() { return restaurantId; }

}