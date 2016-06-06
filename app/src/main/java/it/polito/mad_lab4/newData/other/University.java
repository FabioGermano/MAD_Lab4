package it.polito.mad_lab4.newData.other;

/**
 * Created by f.germano on 20/05/2016.
 */
public class University {
    private String name;
    private String position;
                                    /* TODO da sostituire con la nuova posizione */
    //private Position position;

    public University(){

    }

    public String getName() {
        return name;
    }

    public void setName(String nomeFacolta) {
        this.name = nomeFacolta;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /*public Position getPosition(){
        return this.position;
    }

    public void setPosition(Position p){
        this.position = p;
    }*/
}
