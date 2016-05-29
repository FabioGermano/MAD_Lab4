package it.polito.mad_lab4.common;

import java.util.ArrayList;

import it.polito.mad_lab4.data.user.Notification;

/**
 * Created by f.germano on 11/05/2016.
 */
public class UserSession {

    /**
     *  If null, user is not logged.
     */
    public static Integer userId = null;

    public static ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public static void setNotifications(ArrayList<Notification> notifications) {
        UserSession.notifications = notifications;
    }

    public static ArrayList<Notification> notifications= new ArrayList<>();
}
