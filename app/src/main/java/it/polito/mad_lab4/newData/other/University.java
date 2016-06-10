package it.polito.mad_lab4.newData.other;

/**
 * Created by f.germano on 20/05/2016.
 */
public class University {
    private String name;
    private String position;
                                    /* TODO da sostituire con la nuova posizione */
    private Position positionLatlng;

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

    public Position getPositionLatlng() {
        return positionLatlng;
    }

    public void setPositionLatlng(Position positionLatlng) {
        this.positionLatlng = positionLatlng;
    }
}
