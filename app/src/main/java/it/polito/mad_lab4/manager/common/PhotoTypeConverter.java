package it.polito.mad_lab4.manager.common;

/**
 * Created by f.germano on 11/04/2016.
 */
public final class PhotoTypeConverter
{
    public static String toString(PhotoType photoType)
    {
        if(photoType == PhotoType.PROFILE){
            return "profile";
        }
        else if(photoType == PhotoType.MENU){
            return "menu";
        }
        else if(photoType == PhotoType.OFFER){
            return "offer";
        }
        else if(photoType == PhotoType.TEST)
        {
            return "test";
        }

        return null;
    }
}
