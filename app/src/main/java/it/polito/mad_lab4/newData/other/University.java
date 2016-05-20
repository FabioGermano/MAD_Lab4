package it.polito.mad_lab4.newData.other;

/**
 * Created by f.germano on 20/05/2016.
 */
public class University {
    private String universityId; //not mapped in firebase
    private String nomeFacolta;
    private Object position; //to be defined

    public University(){

    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getNomeFacolta() {
        return nomeFacolta;
    }

    public void setNomeFacolta(String nomeFacolta) {
        this.nomeFacolta = nomeFacolta;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }
}
