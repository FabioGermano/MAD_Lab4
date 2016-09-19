package it.polito.mad_lab4.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.polito.mad_lab4.R;

/**
 * Created by Giovanna on 02/05/2016.
 */
public class Helper {

    public static void setRatingBarColor(Context context, RatingBar ratingBar, float rank){

        /*
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        if(rank <= 1.5){
            stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.bad),
                    PorterDuff.Mode.SRC_ATOP);
        } else if(rank <= 3.5){
            stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.medium),
                    PorterDuff.Mode.SRC_ATOP);
        } else {
            stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.good),
                    PorterDuff.Mode.SRC_ATOP);
        }*/

        Drawable stars = ratingBar.getProgressDrawable();
        if(rank <= 1.5){
            DrawableCompat.setTint(stars, context.getResources().getColor(R.color.bad));
        } else if(rank <= 3.5){
            DrawableCompat.setTint(stars, context.getResources().getColor(R.color.medium));
        } else {
            DrawableCompat.setTint(stars, context.getResources().getColor(R.color.good));
        }


    }

    public static String fromBoolToString(Context context, boolean b){
        if (b)
            return context.getResources().getString(R.string.yes);
        else
            return context.getResources().getString(R.string.no);
    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static void dialNumber (Context context, String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void findOnGoogleMaps(Context context, String address, String city) {
        String map = "http://maps.google.co.in/maps?q=" + address+","+ city;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static int getResourceByName(Context context, String aString, String type) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, type, packageName);
        return resId;
    }

    public static String formatDate(Context context, String weekday, String date){

        String str = new String();
        int yearEnd = date.indexOf("-");
        int monthEnd = date.indexOf("-", yearEnd+1);
        String month= date.substring(yearEnd+1, monthEnd);
        month = intToMonthString(context, Integer.parseInt(month));

        str = weekday+" "+date.substring(monthEnd+1,date.length())+" "+month;
        return str;
    }

    public static String formatDateWithYear(Context context, String weekday, String date){

        String str = new String();
        int yearEnd = date.indexOf("-");
        int monthEnd = date.indexOf("-", yearEnd+1);
        String month= date.substring(yearEnd+1, monthEnd);
        //month = intToMonthString(context, Integer.parseInt(month));

        str= date.substring(monthEnd+1,date.length())+"/"+month+"/"+date.substring(0,yearEnd);
        //str = weekday+" "+date.substring(monthEnd+1,date.length())+" "+month;
        return str;
    }

    public static int fromCalendarOrderToMyOrder(int weekday){
        switch (weekday){
            case 1:
                return 6;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 2;
            case 5:
                return 3;
            case 6:
                return 4;
            case 7:
                return 5;
            default:
                return -1;
        }
    }
    public static int fromMyOrderToCalendarWeekOrder(int weekday){
        switch (weekday){
            case 0: //monday
                return 2;
            case 1: //tuesday
                return 3;
            case 2: //wed
                return 4;
            case 3:
                return 5;
            case 4:
                return 6;
            case 5:
                return 7;
            case 6:
                return 1;
            default:
                return -1;
        }
    }
    public static String intToWeekString (Context context, int weekday){
        switch (weekday){
            case 1:
                return context.getResources().getString(R.string.sunday);

            case 2:
                return context.getResources().getString(R.string.monday);

            case 3:
                return context.getResources().getString(R.string.tuesday);

            case 4:
                return context.getResources().getString(R.string.wednesday);

            case 5:
                return context.getResources().getString(R.string.thursday);

            case 6:
                return context.getResources().getString(R.string.friday);

            case 7:
                return context.getResources().getString(R.string.saturday);
            default:
                return null;
        }
    }

    public static int[] formatRange(String tmp){

        //modificata la stringa di controllo in "chiuso". Prima era "closed" ma su firebase abbiamo scritto in italiano.
        //Questo generava un eccezione
        if(tmp == "" || tmp.toLowerCase().equals("chiuso")){
            return null;
        }

        System.out.println("ORARIO: " + tmp);

        int[] orario = new int[4];
        int n1  = tmp.indexOf(":");
        int n2 = tmp.indexOf(" - ");
        int n3 = tmp.indexOf(":", n2);

        orario[0] = Integer.parseInt(tmp.substring(0, n1)); // opening hour
        orario[1] = Integer.parseInt(tmp.substring(n1+1, n2)); // opening minutes
        orario[2] = Integer.parseInt(tmp.substring(n2+3, n3)); //closing hour
        orario[3] = Integer.parseInt(tmp.substring(n3+1, tmp.length())); //closing minutes

        return orario;
    }
    public static String intToMonthString (Context context, int month){
        switch (month){
            case 1:
                return context.getResources().getString(R.string.jenuary);

            case 2:
                return context.getResources().getString(R.string.february);

            case 3:
                return context.getResources().getString(R.string.march);

            case 4:
                return context.getResources().getString(R.string.april);

            case 5:
                return context.getResources().getString(R.string.may);

            case 6:
                return context.getResources().getString(R.string.june);

            case 7:
                return context.getResources().getString(R.string.july);

            case 8:
                return context.getResources().getString(R.string.ausgust);

            case 9:
                return context.getResources().getString(R.string.september);

            case 10:
                return context.getResources().getString(R.string.october);

            case 11:
                return context.getResources().getString(R.string.november);

            case 12:
                return context.getResources().getString(R.string.december);
            default:
                return null;
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


}
