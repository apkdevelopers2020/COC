package com.hashstar.cocbases.floatingwidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hashstar.cocbases.Application;
import com.hashstar.cocbases.utilities.StoreData;

import java.util.Random;

/**
 * Created by phoenix on 7/5/17.
 */
public  class UnlockButtonListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Here", "unlock I am here");


        if (intent.hasExtra("unlock")) {

                StoreData.SaveString("update", getSaltString(), Application.getInstance());

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