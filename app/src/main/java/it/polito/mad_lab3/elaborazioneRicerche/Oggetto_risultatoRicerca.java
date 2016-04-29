package it.polito.mad_lab3.elaborazioneRicerche;

/**
 * Created by Euge on 29/04/2016.
 */
public class Oggetto_risultatoRicerca {
    private String name ;
    private String descrizione;
    private String image_path;
    private String cost;
    private float valutazione;
    private type type;

    public enum type{
        RISTORANTE,
        PATTO
    }

    public Oggetto_risultatoRicerca(String name, String msg, String path, String costo, float valutazione, type type){
        this.name = name;
        this.descrizione = msg;
        this.image_path=path;
        this.cost = costo;
        this.valutazione = valutazione;
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public String getDescrizione(){
        return this.descrizione;
    }

    public String getImage_path(){
        return this.image_path;
    }

    public String getCost(){
        return this.cost;
    }

    public float getValutazione(){
        return this.valutazione;
    }

    public type getType(){
        return this.type;
    }
}
