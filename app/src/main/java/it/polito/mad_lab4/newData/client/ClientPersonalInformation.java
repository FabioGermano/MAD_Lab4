package it.polito.mad_lab4.newData.client;

import java.io.Serializable;

/**
 * Created by Euge on 28/05/2016.
 */
public class ClientPersonalInformation implements Serializable {
    private String name;
    private String tipoUser; // eg teacher , phd student, student ...
    private String phoneNumber;
    private String gender;
    private String universityId;
    private String email;
    private String bio;
    private String avatarDownloadLink;

    public ClientPersonalInformation(){
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
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

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String id) {
        this.universityId = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail(){ return this.email; }

    public void setEmail(String email) {this.email = email;}

    public String getBio(){ return this.bio; }

    public void setBio (String bio) {this.bio = bio;}

    public String getAvatarDownloadLink() {
        return avatarDownloadLink;
    }

    public void setAvatarDownloadLink(String link){
        this.avatarDownloadLink = link;
    }
}
