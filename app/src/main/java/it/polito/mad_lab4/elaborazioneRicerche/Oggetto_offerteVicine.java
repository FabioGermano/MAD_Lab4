package it.polito.mad_lab4.elaborazioneRicerche;

import java.io.Serializable;

import it.polito.mad_lab4.newData.other.RestaurantPosition;
import it.polito.mad_lab4.newData.restaurant.Offer;

/**
 * Created by Roby on 10/05/2016.
 */
public class Oggetto_offerteVicine implements Serializable{
    private Offer offerta;
    private RestaurantPosition restaurantPosition;

    private String markerAssociato = null;
    private boolean isNew = false;


    public Oggetto_offerteVicine(){
    }

    public void setOfferta(Offer offerta) {
        this.offerta = offerta;
    }

    public Offer getOfferta(){ return offerta; }

    public String getMarkerAssociato(){
        return this.markerAssociato;
    }

    public void setMarkerAssociato(String arg){
        this.markerAssociato = arg;
    }

    public void setNew(){
        this.isNew = true;
    }

    public boolean isNew(){
        return this.isNew;
    }

    public RestaurantPosition getRestaurantPosition() {
        return restaurantPosition;
    }

    public void setRestaurantPosition(RestaurantPosition restaurantPosition) {
        this.restaurantPosition = restaurantPosition;
    }
}