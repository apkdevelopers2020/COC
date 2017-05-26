package com.hashstar.cocbases.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.utilities.Constants;
import com.hashstar.cocbases.utilities.StoreData;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends FragmentActivity implements GoogleApiClient.OnConnectionFailedListener {
    CallbackManager callbackManager;



    /*
    * Google Sign in
    * */
    //Signin button
    private SignInButton signInButton;

    //Signing Options
    private GoogleSignInOptions gso;

    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;

    /*
    * Google Sign in End
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        String name = StoreData.LoadString(Constants.NAME,"",getApplicationContext());

        if(name.equals("")) {
            setContentView(R.layout.activity_login);
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            TextView txt1 = (TextView)findViewById(R.id.coclogin);
            loginButton.setReadPermissions("");
            loginButton.setReadPermissions("email", "user_birthday");
            getLoginDetails(loginButton);
            Typeface tf = Typeface.createFromAsset(this.getAssets(),"Supercell-magic-webfont.ttf");

            txt1.setTypeface(tf);



        /*
        * Google Sign In Start
        * */
            //Initializing google signin option
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            //Initializing signinbutton
            signInButton = (SignInButton) findViewById(R.id.sign_in_button);
//        signInButton.setSize(SignInButton.SIZE_WIDE);
            signInButton.setScopes(gso.getScopeArray());

            //Initializing google api client
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(LoginActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });

        /*
        * Google Sign In End
        * */

            callbackManager = CallbackManager.Factory.create();

// Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.v("LoginActivity", response.toString());

                                    // Application code
                                    try {
                                        String email = object.getString("email");
                                        String id = object.getString("id");
                                        String name = object.getString("name");
                                        String birthdate = object.getString("birthday"); // mm/dd/yyyy form
                                        String profileImageUrl = "http://graph.facebook.com/" + id + "/picture?type=square";


                                        StoreData.SaveString(Constants.FACEBOOK_PROFILEIMAGE_URL, profileImageUrl, getApplicationContext());
                                        StoreData.SaveString(Constants.FACEBOOK_USER_ID, id, getApplicationContext());
                                        StoreData.SaveString(Constants.FACEBOOK_USER_BIRTHDATE, birthdate, getApplicationContext());
                                        StoreData.SaveString(Constants.FACEBOOK_USER_EMAIL, email, getApplicationContext());
                                        StoreData.SaveString(Constants.FACEBOOK_USER_NAME, name, getApplicationContext());

                                        new SendAuuthentication(LoginActivity.this).execute();
                                    } catch (JSONException e) {
                                        Crashlytics.logException(e); e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday");
                    request.setParameters(parameters);
                    request.executeAsync();


                }

                @Override
                public void onCancel() {
                    // App code
                    Toast.makeText(getApplicationContext(),"Can not login try again later",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Toast.makeText(getApplicationContext(),"Can not login try again later",Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

        if(!checkWriteExternalPermission())
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
        }


    }

    private boolean checkWriteExternalPermission()
    {

        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        int res = checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 22) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("Grant Storage Permission")
                            .setMessage("")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();
                } else
                    new android.support.v7.app.AlertDialog.Builder(this)
                            .setTitle("Grant Storage Permission From Settings")
                            .setMessage("")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create().show();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }

    }

    //This function will option signing intent
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    protected void getLoginDetails(final LoginButton login_button) {
// Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override

            public void onSuccess(LoginResult login_result) {
                getFBUserDetails(login_result.getAccessToken());
            }

            @Override

            public void onCancel() {

            }

            @Override

            public void onError(FacebookException exception) {

            }

        });
    }
    
    /**
     * Facebook User Details are fetched for every drawer open. Must be removed . suhas
     */
    public void getFBUserDetails(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.e("Details", response.toString());
                        Log.v("LoginActivity", response.toString());
                        // Application code
                        try {
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String birthdate = object.getString("birthday"); // mm/dd/yyyy form
                            String profileImageUrl = "http://graph.facebook.com/" + id + "/picture?type=square";


                            StoreData.SaveString(Constants.FACEBOOK_PROFILEIMAGE_URL, profileImageUrl, getApplicationContext());
                            StoreData.SaveString(Constants.FACEBOOK_USER_ID, id, getApplicationContext());
                            StoreData.SaveString(Constants.FACEBOOK_USER_BIRTHDATE, birthdate, getApplicationContext());
                            StoreData.SaveString(Constants.FACEBOOK_USER_EMAIL, email, getApplicationContext());
                            StoreData.SaveString(Constants.FACEBOOK_USER_NAME, name, getApplicationContext());

                            new SendAuuthentication(LoginActivity.this).execute();
                        } catch (JSONException e) {
                            Crashlytics.logException(e); e.printStackTrace();
                        }


                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
        request.executeAsync();



    }

    //After the signing we are calling this function
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();

            //Displaying name and email
            assert acct != null;
            Log.e("Name",acct.getDisplayName());
            Log.e("Email",acct.getEmail());
            Log.e("getDisplayName", acct.getDisplayName());
            Log.e("getFamilyName", acct.getFamilyName());
            Log.e("getId", acct.getId());

            StoreData.SaveString(Constants.GOOGLEPLUS_USER_NAME,acct.getDisplayName(),getApplicationContext());
            StoreData.SaveString(Constants.GOOGLEPLUS_USER_EMAIL,acct.getEmail(),getApplicationContext());
            StoreData.SaveString(Constants.GOOGLEPLUS_PROFILEIMAGE_URL, String.valueOf(acct.getPhotoUrl()),getApplicationContext());
            StoreData.SaveString(Constants.GOOGLEPLUS_USER_ID,acct.getId(),getApplicationContext());




            new SendAuuthentication(LoginActivity.this).execute();
        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getApplicationContext(),"Problem with your Google Play Services",Toast.LENGTH_LONG).show();

    }


}