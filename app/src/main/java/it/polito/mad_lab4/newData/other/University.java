package it.polito.mad_lab4.newData.other;

/**
 * Created by f.germano on 20/05/2016.
 */
public class University {
   // private String universityId; //not mapped in firebase
    private String name;
    //private Object position; //to be defined
    private String position;

    public University(){

    }

    /*
    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String nomeFacolta) {
        this.name = nomeFacolta;
    }

    //public Object getPosition() {
    public String getPosition() {
        return position;
    }

    //public void setPosition(Object position) {
    public void setPosition(String position) {
        this.position = position;
    }
}
