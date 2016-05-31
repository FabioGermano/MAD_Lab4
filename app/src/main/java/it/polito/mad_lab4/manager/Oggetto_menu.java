package it.polito.mad_lab4.manager;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import it.polito.mad_lab4.newData.restaurant.Dish;

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

    public ArrayList<Dish> getDishListByIndex(int i){
        switch (i){
            case 0:
                return primi;
            case 1:
                return secondi;
            case 2:
                return dessert;
            case 3:
                return altro;
            default:
                return null;
        }
    }

    public void clearAll() {
        primi.clear();
        secondi.clear();
        dessert.clear();
        altro.clear();
    }
}