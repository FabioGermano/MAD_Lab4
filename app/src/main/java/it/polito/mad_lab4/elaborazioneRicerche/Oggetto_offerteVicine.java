package it.polito.mad_lab4.elaborazioneRicerche;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import it.polito.mad_lab4.data.restaurant.Offer;
import it.polito.mad_lab4.newData.other.Position;

/**
 * Created by Roby on 10/05/2016.
 */
public class Oggetto_offerteVicine implements Serializable{
    private Offer offerta;

    private String restaurantId;
    private Position restaurantPosition;

    private String markerAssociato = null;
    private boolean isNew = false;


    public Oggetto_offerteVicine(Offer o, String id){
        this.offerta = o;
        this.restaurantId = id;
    }

    public Offer getOffer(){ return offerta; }
    public String getId() { return restaurantId; }

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

    public Position getRestaurantPosition(){
        return this.restaurantPosition;
    }

    public void setRestaurantPosition(Position position){
        this.restaurantPosition = position;
    }

}