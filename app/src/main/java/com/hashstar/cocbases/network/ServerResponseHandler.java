package com.hashstar.cocbases.network;

import android.app.Activity;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.utilities.UtilityMethods;

import org.json.JSONObject;

/**
 * Created by suhas on 20/4/17.
 */
public class ServerResponseHandler {

    public static Boolean handleResponse(Activity activity, JSONObject responseJSON,View view) {

        boolean boolResult;
        boolResult = jsonDataHandler(activity, responseJSON,view);
        return boolResult;
    }


    //Common Method for all responses by server :TO Show Alerts
    private static Boolean jsonDataHandler(Activity activity, JSONObject responseJSON,View view) {
        try {
            String message;
            message = "";
            String title;
            title = "";

            String[] excepArray = {
                    "return",
                    "timeout",
                    "internet",
                    "servererror",
                    "contentnotfound",
                    "loginfeature",
                    "badrequest",
                    "internalservererr",
                    "connectionerror"
            };

            int caseSelect;
            caseSelect = 1000;
            for (int i = 0; i < excepArray.length; i++) {
                if (responseJSON.has(excepArray[i])) {
                    caseSelect = i;
                    break;
                }
            }


            switch (caseSelect) {
                case 0:

                    message = "Our engineers are working quickly to resolve the issue.";
                    title = "Thank you for your patience.";
                    UtilityMethods.showAlertDialog(activity,title,message,true);

                    break;

                case 1:

                    message = "Timeout Error. Please retry.";
                    title = "Timeout";
                    UtilityMethods.showAlertDialog(activity,title,message,true);
                    break;
                case 2:

                    message = "Internet Connection Problem. You do not have an active internet connection.";
                    title = "Internet Connection";
                    UtilityMethods.showAlertDialog(activity,title,message,true);

                    break;

                case 3:

                    message = "Connection Error. Connection refused by server.";
                    title = "Connection Error.";
                    UtilityMethods.showAlertDialog(activity,title,message,true);
                    break;

                case 4:

                    message = activity.getResources().getString(R.string.contentnotfound);
                    title = "Page Not Found";
                    UtilityMethods.showAlertDialog(activity,title,message,true);
                    break;

                case 5:

                    message = activity.getResources().getString(R.string.unauthorized);
                    title = "Unauthorized";
                    UtilityMethods.showAlertDialog(activity,title,message,true);
                    break;

                case 6:

                    message = activity.getResources().getString(R.string.badrequest);
                    title = "Bad Request";
                    UtilityMethods.showAlertDialog(activity,title,message,true);

                    break;

                case 7:

                    message = activity.getResources().getString(R.string.serverexception);
                    title = "Server Problem";
                    UtilityMethods.showAlertDialog(activity,title,message,true);
                    break;

                case 8: //For coding exception

                    message = activity.getResources().getString(R.string.connectionerror);
                    title = "Connection Error";
                    UtilityMethods.showAlertDialog(activity,title,message,true);
                    break;

                default:

                        return true;


            }

/*            if (listView != null)
                listView.setAdapter(null);
            if (swipeLayout != null)
                swipeLayout.setRefreshing(false);*/
            return false;

        } catch (Exception e) {
            Crashlytics.logException(e); e.printStackTrace();
            return false;
        }
    }
}