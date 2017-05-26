package com.hashstar.cocbases.network;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 *
 * Created by suhas on 14/2/17.
 *
 */

public class WebService {

    public static final String REMOTE_URL = "http://coderminion.com";
    public static final String LOCAL_URL = "";
    //TODO change LOCAL TO REMOTE
    public static final String BASE_URL = REMOTE_URL;
    public static final String POST_REQUEST = "POST";
    public static final String GET_REQUEST = "GET";
    public static final String USERAGENT = "ANDROID";
    public static final String COOKIE_HEADERS = "ANDROID";


    public static JSONObject sendServer(HashMap<String, String> params, String serviceURL, String methodOfRequest) {


        String charset = "UTF-8";
        HttpURLConnection conn = null;
        DataOutputStream wr;
        StringBuilder result;
        URL urlObj;
        StringBuilder sbParams;
        String paramsString;
        int httpResponseCode = 0;
        serviceURL = BASE_URL + serviceURL;


        sbParams = new StringBuilder();
        try {
            int i = 0;
            for (String key : params.keySet()) {

                if (i != 0) {
                    sbParams.append("&");
                }
                try {
                    String param = params.get(key);
                    if (param == null)
                        param = "";

                    sbParams.append(key).append("=").append(URLEncoder.encode(param, charset));
                } catch (Exception e) {
                    Crashlytics.logException(e); e.printStackTrace();

                }
                i++;
            }

            if (methodOfRequest.equals(POST_REQUEST)) {
                //REQUESTED METHOD IS POST
                urlObj = new URL(serviceURL);
                Log.v("Test URL POST", serviceURL + sbParams);
                conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestProperty("Accept", charset);
                conn.setRequestProperty("User-Agent", USERAGENT);
                conn.setRequestProperty("Cookie", COOKIE_HEADERS);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                Log.i("URL:", serviceURL);

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();
            } else if (methodOfRequest.equals(GET_REQUEST)) {

                if (sbParams.length() != 0) {
                    serviceURL += "?" + sbParams.toString();    //****
                }
                urlObj = new URL(serviceURL);

                conn = (HttpURLConnection) urlObj.openConnection();
                Log.v("Test URL POST", serviceURL);
                conn.setRequestProperty("Accept", charset);
                conn.setRequestProperty("User-Agent", USERAGENT);
                conn.setRequestProperty("Cookie", COOKIE_HEADERS);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.connect();
            }

            Log.i("URL:", serviceURL);

            //Check responsecode

            httpResponseCode = conn.getResponseCode();
            String httpResonseString = conn.getResponseMessage();
            Log.v("Response: Code,String :", httpResponseCode + "  " + httpResonseString);
            Log.v("Method Req  ", methodOfRequest);


            if (httpResponseCode != HttpURLConnection.HTTP_OK) {
                Log.e("Error Stream", conn.getErrorStream().toString());
                String temp = "";
                if (httpResponseCode == 404)
                    temp = "{contentnotfound:true}";
                else if (httpResponseCode == 401)
                    temp = "{loginfeature:true}";
                else if (httpResponseCode == 500)
                    temp = "{internalservererr:true}";
                else if (httpResponseCode == 400)
                    temp = "{badrequest:true}";

                return new JSONObject(temp);
            }

            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            conn.disconnect();
            String res = result.toString();

            JSONObject object = new JSONObject();
            Object json = new JSONTokener(res).nextValue();
            if (json instanceof JSONObject) {
                //you have an object
                object = (JSONObject) json;
            }
            else if (json instanceof JSONArray)
            {
                JSONArray jsonArray = new JSONArray(res);
                object.put("response",jsonArray);
            }



            Log.e("RESPONSE FROM SERVER", result.toString());
            return object;

        } catch (Exception e1) {
            e1.printStackTrace();
            String temp = "";

            if (temp.length() == 0) {

                temp = CommonExceptionHandler(e1);

            }
            try {
                Log.v("message", temp);
                return new JSONObject(temp);

            } catch (Exception e) {
                Crashlytics.logException(e); e.printStackTrace();
                return null;
            }
        }
    }

    private static final String CommonExceptionHandler(Exception e1) {
        String temp;
        temp = "";
        if (e1 instanceof SocketTimeoutException) {
            temp = "{timeout:true}";
        } else if (e1 instanceof UnknownHostException) {
            temp = "{internet:false}";
        } else if (e1 instanceof java.io.FileNotFoundException) {
            temp = "{FileNotFound: true}";
        } else if (e1 instanceof java.net.ConnectException) {
            temp = "{connectionerror:true}";
        }
        return temp;
    }


}
