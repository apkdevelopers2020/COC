package com.hashstar.cocbases.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.hashstar.cocbases.adapter.TownHallGridItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by suhas on 11/4/17.
 */
public class UtilityMethods {

    public static Bitmap getBitmapFromUrl(String imgUrl) {

        InputStream in;
        try {
            URL url = new URL(imgUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.e("URLIMG", imgUrl);
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            Crashlytics.logException(e); e.printStackTrace();

        } catch (IOException e) {
            Crashlytics.logException(e); e.printStackTrace();
        }
        return null;

    }

    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(status)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }


    public static int extractDigits (String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return Integer.parseInt(builder.toString()) ;
    }

    public static int compare(TownHallGridItem t1, TownHallGridItem t2) {
        if(t1.getOrder() == t2.getOrder()){
            return t1.getName().compareTo(t2.getName());
        }else if(t1.getOrder() > t2.getOrder()){
            return 1;
        }else{
            return -1;
        }
    }
}
