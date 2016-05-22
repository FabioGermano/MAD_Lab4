package it.polito.mad_lab4.manager;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Euge on 02/04/2016.
 */
public class Oggetto_piatto implements Serializable {

    private String piatto_name = "";
    private String photo_thumb = null;
    private String photo_large = null;

    private int cost = 0;
    private type_enum type = null;
    private int id;
    public enum type_enum {PRIMI, SECONDI, DESSERT, ALTRO }
    private boolean availability;
    private boolean tmpAv;

    public Oggetto_piatto(String name, int cost, type_enum type){
        this.piatto_name = name;
        this.cost = cost;
        this.type = type;
        this.availability = true;
    }

    public void setId(int num){
        this.id = num;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String arg){
        this.piatto_name = arg;
    }

    public String getName(){
        return this.piatto_name;
    }

    public void setCost(int arg){
        this.cost = arg;
    }

    public int getCost(){
        return this.cost;
    }

    public void setPhoto(String thumb, String large){
        this.photo_thumb= thumb;
        this.photo_large = large;
    }

    public String[] getPhoto(){
        return new String[] {photo_thumb, photo_large};
    }

    public void setDishType(type_enum arg){
        this.type = arg;
    }

    public type_enum getDishType(){
        return this.type;
    }

    public boolean isAvailable() { return availability; }

    public void setAvailability(boolean arg){
        this.availability = arg;
    }

    public void setTmpAv(boolean av){
        this.tmpAv = av;
    }

    public boolean getTmpAv(){
        return tmpAv;
    }
}
