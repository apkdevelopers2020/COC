package com.hashstar.cocbases.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StoreData {
    public static SharedPreferences SharedPref;
    public static SharedPreferences SharedPref_tutorial;
    static String savedValue;
    static Boolean tempBoolean;
    static int savedIntValue;

    public static void SaveString(String key, String value, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //
    public static String LoadString(String key, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        savedValue = SharedPref.getString(key, "");
        return savedValue;
    }

    public static String LoadString(String key, String defaultString, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        savedValue = SharedPref.getString(key, defaultString);
        return savedValue;
    }

    public static Boolean LoadBoolean(String key, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tempBoolean = SharedPref.getBoolean(key, false);
        return tempBoolean;
    }

    public static Boolean LoadBoolean(String key, boolean defaultValue, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tempBoolean = SharedPref.getBoolean(key, defaultValue);
        return tempBoolean;
    }

    public static void SaveBoolean(String key, Boolean value, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static void SaveInt(String key, int value, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void SaveTutorialStatus(boolean value, Context context) {
        SharedPref_tutorial = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = SharedPref.edit();
        editor.putBoolean("tutorial_Status", value);
        editor.apply();
    }

    public static boolean LoadtutorialStatus(Context context) {
        SharedPref_tutorial = PreferenceManager.getDefaultSharedPreferences(context);
        return SharedPref.getBoolean("tutorial_Status", false);
    }


    public static int LoadInt(String key, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        savedIntValue = SharedPref.getInt(key, 0);
        return savedIntValue;
    }

    public static int LoadInt(String key, int defaultValue, Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        savedIntValue = SharedPref.getInt(key, defaultValue);
        return savedIntValue;
    }

    public static void ClearAll(Context context) {
        SharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPref.edit().clear().apply();
    }


}
