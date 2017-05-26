package com.hashstar.cocbases.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;

import com.hashstar.cocbases.data.ServiceUrls;
import com.hashstar.cocbases.network.ServerResponseHandler;
import com.hashstar.cocbases.network.WebService;
import com.hashstar.cocbases.utilities.Constants;
import com.hashstar.cocbases.utilities.StoreData;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by phoenix on 23/4/17.
 */
public class SendAuuthentication extends AsyncTask<JSONObject,JSONObject,JSONObject>{

    Activity activity;
    ProgressDialog progressDialog ;

    SendAuuthentication(Activity activity)
    {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading bases....");
        progressDialog.setTitle("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
      String  GOOGLEPLUS_USER_NAME= StoreData.LoadString(Constants.GOOGLEPLUS_USER_NAME,"",activity);
      String  GOOGLEPLUS_USER_EMAIL=StoreData.LoadString(Constants.GOOGLEPLUS_USER_EMAIL,"",activity);
      String  GOOGLEPLUS_PROFILEIMAGE_URL=StoreData.LoadString(Constants.GOOGLEPLUS_PROFILEIMAGE_URL, "",activity);
      String  GOOGLEPLUS_USER_ID=StoreData.LoadString(Constants.GOOGLEPLUS_USER_ID,"",activity);


      String  FACEBOOK_PROFILEIMAGE_URL=StoreData.LoadString(Constants.FACEBOOK_PROFILEIMAGE_URL,"",activity);
      String  FACEBOOK_USER_ID=StoreData.LoadString(Constants.FACEBOOK_USER_ID,"",activity);
      String  FACEBOOK_USER_BIRTHDATE=StoreData.LoadString(Constants.FACEBOOK_USER_BIRTHDATE,"",activity);
      String  FACEBOOK_USER_EMAIL=StoreData.LoadString(Constants.FACEBOOK_USER_EMAIL,"",activity);
      String  FACEBOOK_USER_NAME=StoreData.LoadString(Constants.FACEBOOK_USER_NAME,"",activity);
      String  UNIQUEID =   Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
      String  GCMID =   StoreData.LoadString(Constants.PROPERTY_REG_ID,"",activity);

        HashMap<String,String> hashMap = new HashMap();

        hashMap.put(Constants.GOOGLEPLUS_USER_NAME,GOOGLEPLUS_USER_NAME);
        hashMap.put(Constants.GOOGLEPLUS_USER_EMAIL,GOOGLEPLUS_USER_EMAIL);
        hashMap.put(Constants.GOOGLEPLUS_PROFILEIMAGE_URL,GOOGLEPLUS_PROFILEIMAGE_URL);
        hashMap.put(Constants.GOOGLEPLUS_USER_ID,GOOGLEPLUS_USER_ID);

        hashMap.put(Constants.FACEBOOK_PROFILEIMAGE_URL,FACEBOOK_PROFILEIMAGE_URL);
        hashMap.put(Constants.FACEBOOK_USER_ID,FACEBOOK_USER_ID);
        hashMap.put(Constants.FACEBOOK_USER_BIRTHDATE,FACEBOOK_USER_BIRTHDATE);
        hashMap.put(Constants.FACEBOOK_USER_EMAIL,FACEBOOK_USER_EMAIL);
        hashMap.put(Constants.FACEBOOK_USER_NAME,FACEBOOK_USER_NAME);
        hashMap.put(Constants.UNIQUEID,UNIQUEID);
        hashMap.put(Constants.GCMID,GCMID);

       JSONObject object =  WebService.sendServer(hashMap, ServiceUrls.CREATE_ENTRY,WebService.POST_REQUEST);

        return object;
    }
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        progressDialog.dismiss();
        if (ServerResponseHandler.handleResponse(activity, jsonObject, null)) {

            String GOOGLEPLUS_USER_NAME = StoreData.LoadString(Constants.GOOGLEPLUS_USER_NAME, "", activity);
            String GOOGLEPLUS_USER_EMAIL = StoreData.LoadString(Constants.GOOGLEPLUS_USER_EMAIL, "", activity);
            String GOOGLEPLUS_PROFILEIMAGE_URL = StoreData.LoadString(Constants.GOOGLEPLUS_PROFILEIMAGE_URL, "", activity);

            String FACEBOOK_PROFILEIMAGE_URL = StoreData.LoadString(Constants.FACEBOOK_PROFILEIMAGE_URL, "", activity);
            String FACEBOOK_USER_EMAIL = StoreData.LoadString(Constants.FACEBOOK_USER_EMAIL, "", activity);
            String FACEBOOK_USER_NAME = StoreData.LoadString(Constants.FACEBOOK_USER_NAME, "", activity);

            if (GOOGLEPLUS_USER_NAME.length() > 0) {
                StoreData.SaveString(Constants.NAME, GOOGLEPLUS_USER_NAME, activity);
                StoreData.SaveString(Constants.EMAIL, GOOGLEPLUS_USER_EMAIL, activity);
                StoreData.SaveString(Constants.IMGURL, GOOGLEPLUS_PROFILEIMAGE_URL, activity);
            } else if (FACEBOOK_USER_NAME.length() > 0) {
                StoreData.SaveString(Constants.NAME, FACEBOOK_USER_NAME, activity);
                StoreData.SaveString(Constants.EMAIL, FACEBOOK_USER_EMAIL, activity);
                StoreData.SaveString(Constants.IMGURL, FACEBOOK_PROFILEIMAGE_URL, activity);
            }

                Intent intent = new Intent(activity,MainActivity.class);
                activity.startActivity(intent);
                activity.finish();

        }


    }
}
