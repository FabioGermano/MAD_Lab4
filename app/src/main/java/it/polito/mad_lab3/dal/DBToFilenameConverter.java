package it.polito.mad_lab3.dal;

/**
 * Created by f.germano on 26/04/2016.
 */
public final class DBToFilenameConverter {
    public static String convert(DB db){
        switch (db){
            case Restaurants:
                return "db_restaurant";
            default:
                return null;
        }
    }
}
