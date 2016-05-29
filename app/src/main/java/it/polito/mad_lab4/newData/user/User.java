package it.polito.mad_lab4.newData.user;

/**
 * Created by f.germano on 20/05/2016.
 */
public class User {
    private String userId; // not mapped in firebase
    private String userName;
    private String tipoUser; // eg teacher , phd student, student ...
    private String phoneNumber;
    private boolean isMale;
    private String email;
    private String facolta;
    private Object posizioneFacolta; //to be defined

    public User(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacolta() {
        return facolta;
    }

    public void setFacolta(String facolta) {
        this.facolta = facolta;
    }

    public Object getPosizioneFacolta() {
        return posizioneFacolta;
    }

    public void setPosizioneFacolta(Object posizioneFacolta) {
        this.posizioneFacolta = posizioneFacolta;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }
}
