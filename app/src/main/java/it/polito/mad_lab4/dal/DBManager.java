package it.polito.mad_lab4.dal;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.polito.mad_lab4.R;

/**
 * Created by f.germano on 26/04/2016.
 */
public final class DBManager {
    private static final String[] names = {
            DBToFilenameConverter.convert(DB.Restaurants),
            DBToFilenameConverter.convert(DB.Users)
    };

    private static final int[] raws = {
            R.raw.db_restaurant,
            R.raw.db_user
    };

    private static boolean dbCreated;

    private static boolean createDB(Context context){
        int ch;

        for(int i = 0; i<names.length; i++) {

            File file_restaurants = new File(context.getFilesDir(), names[i]);

            try {
                StringBuffer str;
                InputStream dbFile;
                FileOutputStream fos;

                if (!file_restaurants.exists()) {
                    str = new StringBuffer("");
                    dbFile = context.getResources().openRawResource(raws[i]);
                    fos = context.openFileOutput(names[i], Context.MODE_PRIVATE);

                    while ((ch = dbFile.read()) != -1) {
                        str.append((char) ch);
                    }
                    fos.write(str.toString().getBytes());
                    fos.close();
                    dbFile.close();
                    Log.i("DB", "***** DB MENU CREATO *****");
                } else {
                    Log.i("DB", "***** DB MENU CREATO *****");
                }

            } catch (FileNotFoundException e) {
                System.out.println("Eccezione: " + e.getMessage());
                return false;
            } catch (IOException e) {
                System.out.println("Eccezione: " + e.getMessage());
                return false;
            }
        }

        dbCreated = true;

        return true;
    }

    public static String readJSON(Context context, DB db){
        if(!dbCreated){
            createDB(context);
        }

        FileInputStream fis = null;

        try {
            fis = context.openFileInput(DBToFilenameConverter.convert(db));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder str = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            fis.close();

            return str.toString();

        } catch (FileNotFoundException e) {
            System.out.println("Eccezione:" + e.getMessage());
            return null;
        }
        catch (IOException e){
            System.out.println("Eccezione:" + e.getMessage());
            return null;
        }
    }

    public static <T> T deserializeToEntity(Context context, DB db, Class<T> classOfT){
        String json = readJSON(context, db);

        Gson gson = new GsonBuilder().serializeNulls().create();

        return gson.fromJson(json, classOfT);
    }

    public static boolean serializeEntity(Context context, DB db, Object entity){
        Gson gson = new GsonBuilder().serializeNulls().create();

        String jsonReservations = gson.toJson(entity);

        try {
            FileOutputStream fos = context.openFileOutput(DBToFilenameConverter.convert(db), Context.MODE_PRIVATE);
            fos.write(jsonReservations.getBytes());
            fos.close();
            return true;
        }
        catch (FileNotFoundException e) {
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
        catch (IOException e){
            System.out.println("Eccezione: "+e.getMessage());
            return false;
        }
    }
}
