package it.polito.mad_lab3.elaborazioneRicerche;

import java.io.Serializable;

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
    private type type;
    private int id;

    public enum type{
        RISTORANTE,
        PATTO
    }

    public Oggetto_risultatoRicerca(int id, String name, String via, String path, int costo, float valutazione, type type){
        this.name = name;
        this.place = via;
        this.image_path=path;
        this.cost = costo;
        this.valutazione = valutazione;
        this.type = type;
        this.id = id;
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

    public type getType(){
        return this.type;
    }

    public String getFasciaPrezzo(){
        if(this.cost != -1){
            if(this.cost < 5)
                return "€";
            if (this.cost >= 5 && this.cost < 10)
                return "€";
            if (this.cost >= 10 && this.cost < 15)
                return "€€";
            if (this.cost >= 15 && this.cost < 20)
                return "€€";
            if (this.cost >= 20 && this.cost < 25)
                return "€€€";
        }
        return "€€€";
    }

    public int getId(){
        return this.id;
    }
}
