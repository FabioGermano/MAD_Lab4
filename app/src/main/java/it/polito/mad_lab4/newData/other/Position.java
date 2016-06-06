package it.polito.mad_lab4.newData.other;

import java.io.Serializable;

/**
 * Created by Euge on 06/06/2016.
 */
public class Position implements Serializable {

    private double latitudine;
    private double longitudine;

    public double getLatitudine(){
        return latitudine;
    }

    public double getLongitudine(){
        return longitudine;
    }

    public void setLatitudine(double lat){
        this.latitudine = lat;
    }

    public void setLongitudine(double lon){
        this.longitudine = lon;
    }
}
