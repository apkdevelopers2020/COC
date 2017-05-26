package com.hashstar.cocbases;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;

import com.bumptech.glide.request.target.ViewTarget;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

/**
 * Created by suhas on 19/4/17.
 */

public class Application extends MultiDexApplication {
    private static Application cocBaseApp;

    public static Application getInstance() {
        return cocBaseApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        ViewTarget.setTagId(R.id.glide_tag);

        MultiDex.install(this);
        cocBaseApp = this;
        printHashKey(this);

    }

    public static void printHashKey(Context pContext) {
        String TAG="APPLICATION";
        try {
            PackageManager pm = pContext.getPackageManager();

            PackageInfo info = pm.getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
