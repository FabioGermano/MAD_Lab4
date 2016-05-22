package it.polito.mad_lab4.manager.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by f.germano on 11/04/2016.
 */
public class PhotoManager {

    private PhotoType photoType;
    private String thumbPath, largePath; //saved
    private boolean isPhotoModified;
    private String baseTmpPath, baseSavedPath;
    private Context context;

    public PhotoManager(Context context, PhotoType photoType, String thumbPath, String largePath)
    {
        this.photoType = photoType;
        this.thumbPath = thumbPath;
        this.largePath = largePath;
        this.context = context;

        this.baseSavedPath = Environment.getExternalStorageDirectory().toString() + "/.photos/saved/" + PhotoTypeConverter.toString(photoType);
        this.baseTmpPath = Environment.getExternalStorageDirectory().toString() + "/.photos/tmp/" + PhotoTypeConverter.toString(photoType);

        File file = new File(this.baseTmpPath);
        file.mkdirs();
        file = new File(this.baseSavedPath);
        file.mkdirs();
    }

    private String fileExists(boolean isTmp, boolean isThumb, String id)
    {
        String path;

        if(isTmp){
            path = baseTmpPath + "/" + id;
        }
        else{
            path = baseSavedPath + "/" + id;
        }

        if(isThumb){
            path += "_thumb.jpg";
        }
        else
        {
            path += "_large.jpg";
        }

        File file = new File(path);
        if(file.exists())
            return path;
        else
            return null;
    }

    public String getThumb(String id)
    {
        String pathTmp = fileExists(true, true, id);
        if(pathTmp != null)
        {
            return pathTmp;
        }

        if(thumbPath != null && !this.isPhotoModified)
        {
            return thumbPath;
        }

        return null; // non c'e'
    }

    public void saveThumb(Bitmap thumb, String id){
        this.isPhotoModified = true;

        OutputStream fOut = null;
        File file = new File(this.baseTmpPath, id+"_thumb.jpg");

        try {
            fOut = new FileOutputStream(file);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(),file.getName());
        } catch (FileNotFoundException e) {
            Log.d(e.getMessage(), e.getMessage(), e);
        } catch (IOException e) {
            Log.d(e.getMessage(), e.getMessage(), e);
        }
    }

    public String getLarge(String id)
    {
        String pathTmp = fileExists(true, false, id);
        if(pathTmp != null)
        {
            return pathTmp;
        }

        if(largePath != null && !this.isPhotoModified)
        {
            return largePath;
        }

        return null; // non c'e'
    }

    public void saveLarge(Bitmap large, String id){
        this.isPhotoModified = true;

        OutputStream fOut = null;
        File file = new File(this.baseTmpPath, id+"_large.jpg");
        try {
            fOut = new FileOutputStream(file);
            large.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(),file.getName());
        } catch (FileNotFoundException e) {
            Log.d(e.getMessage(), e.getMessage(), e);
        } catch (IOException e) {
            Log.d(e.getMessage(), e.getMessage(), e);
        }
    }

    public void removeThumb(String id)
    {
        this.isPhotoModified = true;

        String path = fileExists(true, true, id);
        if(path != null) {
            File file = new File(path);
            file.delete();
        }
    }

    public void removeLarge(String id)
    {
        this.isPhotoModified = true;

        String path = fileExists(true, false, id);
        if(path != null) {
            File file = new File(path);
            file.delete();
        }
    }

    public void destroy(String id)
    {
        String pathTmp = fileExists(true, true, id);
        if(pathTmp != null)
        {
            File file = new File(pathTmp);
            file.delete();
        }
        pathTmp = fileExists(true, false, id);
        if(pathTmp != null)
        {
            File file = new File(pathTmp);
            file.delete();
        }
    }

    public String commitThumb(String id)
    {
        if(this.isPhotoModified)
        {
            if(fileExists(true, true, id) != null) {
                OutputStream fOut = null;
                File file = new File(this.baseSavedPath, id + "_thumb.jpg");
                Bitmap tmpThumb = BitmapFactory.decodeFile(getThumb(id));

                try {
                    fOut = new FileOutputStream(file);
                    tmpThumb.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    return file.getAbsolutePath();
                    //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(),file.getName());
                } catch (FileNotFoundException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                } catch (IOException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                }

                return null;
            }
            else
            {
                String path = fileExists(false, true, id);
                if(path != null) {
                    File file = new File(path);
                    file.delete();
                }
            }
        }

        return null;
    }

    public String commitLarge(String id)
    {
        if(this.isPhotoModified)
        {
            if(fileExists(true, false, id) != null) {
                OutputStream fOut = null;
                File file = new File(this.baseSavedPath, id + "_large.jpg");
                Bitmap tmpThumb = BitmapFactory.decodeFile(getLarge(id));

                try {
                    fOut = new FileOutputStream(file);
                    tmpThumb.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    return file.getAbsolutePath();
                    //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(),file.getName());
                } catch (FileNotFoundException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                } catch (IOException e) {
                    Log.d(e.getMessage(), e.getMessage(), e);
                }

                return null;
            }
            else
            {
                String path = fileExists(false, false, id);
                if(path != null) {
                    File file = new File(path);
                    file.delete();
                }
            }
        }

        return null;
    }
}
