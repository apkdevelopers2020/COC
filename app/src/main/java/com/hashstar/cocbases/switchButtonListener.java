package com.hashstar.cocbases;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hashstar.cocbases.utilities.StoreData;

import java.util.Random;

public class switchButtonListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Here", "I am here");


        if (intent.hasExtra("close")) {
            Log.e("close", "close");
            String close = intent.getStringExtra("close");
            if (close.equals("close")) {
                StoreData.SaveString("stop", getSaltString(), Application.getInstance());
            }
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager notificationManager =
                    (NotificationManager) Application.getInstance().getSystemService(ns);

            notificationManager.cancel(1);
            notificationManager.cancel(2);
        }


    }


    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}