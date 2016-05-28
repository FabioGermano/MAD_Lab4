package it.polito.mad_lab4.data.client;

import java.io.Serializable;

/**
 * Created by Euge on 28/05/2016.
 */
public class ClientPersonalInformation implements Serializable {
    private String phone;
    private String clientType;  // student, prof, etc...

    // info per visualizzare la mappa di default
    private String university;
}
