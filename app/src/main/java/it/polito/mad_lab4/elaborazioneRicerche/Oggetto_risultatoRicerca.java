package it.polito.mad_lab4.elaborazioneRicerche;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Euge on 29/04/2016.
 */
public class Oggetto_risultatoRicerca implements Serializable {
    private String name ;
    private String descrizione;
    private String image_path;
    private int cost = -1;
    private String place;
    private float valutazione;
    private String id;
    private boolean takeAway = false;
    private boolean onPlace = false;

    public Oggetto_risultatoRicerca(String id, String name, String via, String path, int costo, float valutazione, boolean takeAway, boolean onPlace){
        this.name = name;
        this.place = via;
        this.image_path=path;
        this.cost = costo;
        this.valutazione = valutazione;
        this.id = id;
        this.takeAway = takeAway;
        this.onPlace = onPlace;
    }

    public boolean isOnPlace() {
        return onPlace;
    }

    public boolean isTakeAway() {
        return takeAway;
    }

    public String getName(){
        return this.name;
    }

    public String getDescrizione(){
        return this.descrizione;
    }

    public void setDescrizione(String msg){
        this.descrizione = msg;
    }

    public String getImage_path(){
        return this.image_path;
    }

    public int getCost(){
        return this.cost;
    }

    public float getValutazione(){
        return this.valutazione;
    }

    public String getPlace(){
        return this.place;
    }

    public void setPlace(String place){
        this.place = place;
    }

    public String getFasciaPrezzo(){
        if(this.cost != -1){
            if(this.cost < 10)
                return "€";
            if (this.cost >= 10 && this.cost < 20)
                return "€€";
            else
                return "€€€";
        }
        return null;
    }

    public String getId(){
        return this.id;
    }
}
