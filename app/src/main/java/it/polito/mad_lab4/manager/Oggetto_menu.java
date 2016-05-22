package it.polito.mad_lab4.manager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.data.restaurant.Dish;

public class Oggetto_menu implements Serializable {
    private ArrayList<Dish> primi = null;
    private ArrayList<Dish> secondi = null;
    private ArrayList<Dish> dessert = null;
    private ArrayList<Dish> altro = null;

    public Oggetto_menu(){
        this.primi = new ArrayList<>();
        this.secondi = new ArrayList<>();
        this.dessert = new ArrayList<>();
        this.altro = new ArrayList<>();
    }

    public ArrayList<Dish> getPrimi(){
        return this.primi;
    }

    public ArrayList<Dish> getSecondi(){
        return this.secondi;
    }

    public ArrayList<Dish> getDessert(){
        return this.dessert;
    }

    public ArrayList<Dish> getAltro(){
        return this.altro;
    }

    public void addPrimo(Dish obj){
        if(this.primi == null){
            this.primi = new ArrayList<>();
        }

        this.primi.add(obj);
    }

    public void addSecondo(Dish obj){
        if(this.secondi == null){
            this.secondi = new ArrayList<>();
        }

        this.secondi.add(obj);
    }

    public void addDessert(Dish obj){
        if(this.dessert == null){
            this.dessert = new ArrayList<>();
        }

        this.dessert.add(obj);
    }

    public void addAltro(Dish obj){
        if(this.altro == null){
            this.altro = new ArrayList<>();
        }

        this.altro.add(obj);
    }
}
/**
 * Created by Eugenio on 07/04/2016.
 */
/*public class Oggetto_menu implements Serializable {
    private ArrayList<Oggetto_piatto> primi = null;
    private ArrayList<Oggetto_piatto> secondi = null;
    private ArrayList<Oggetto_piatto> dessert = null;
    private ArrayList<Oggetto_piatto> altro = null;

    public Oggetto_menu(){
        this.primi = new ArrayList<>();
        this.secondi = new ArrayList<>();
        this.dessert = new ArrayList<>();
        this.altro = new ArrayList<>();
    }

    public ArrayList<Oggetto_piatto> getPrimi(){
        return this.primi;
    }

    public ArrayList<Oggetto_piatto> getSecondi(){
        return this.secondi;
    }

    public ArrayList<Oggetto_piatto> getDessert(){
        return this.dessert;
    }

    public ArrayList<Oggetto_piatto> getAltro(){
        return this.altro;
    }

    public void addPrimo(Oggetto_piatto obj){
        if(this.primi == null){
            this.primi = new ArrayList<>();
        }

        this.primi.add(obj);
    }

    public void addSecondo(Oggetto_piatto obj){
        if(this.secondi == null){
            this.secondi = new ArrayList<>();
        }

        this.secondi.add(obj);
    }

    public void addDessert(Oggetto_piatto obj){
        if(this.dessert == null){
            this.dessert = new ArrayList<>();
        }

        this.dessert.add(obj);
    }

    public void addAltro(Oggetto_piatto obj){
        if(this.altro == null){
            this.altro = new ArrayList<>();
        }

        this.altro.add(obj);
    }

    public int getNewId(){

        int max = 1;
        boolean trovato = false;

        int tot_length =getPrimi().size()+getSecondi().size()+getDessert().size()+getAltro().size();

        for (Oggetto_piatto o : primi) {
            if(o.getId() > max){
                max = o.getId();
            }
        }
        for (Oggetto_piatto o : secondi) {
            if(o.getId() > max){
                max = o.getId();
            }
        }
        for (Oggetto_piatto o : dessert) {
            if(o.getId() > max){
                max = o.getId();
            }
        }
        for (Oggetto_piatto o : altro) {
            if(o.getId() > max){
                max = o.getId();
            }
        }

        return max+1;
    }
}
*/