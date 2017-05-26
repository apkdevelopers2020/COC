package com.hashstar.cocbases.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.floatingwidget.FloatingViewService;
import com.hashstar.cocbases.floatingwidget.ZoomableImageView;

import java.util.UUID;

public class FulllScreenImageView extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    String imageurl;
    MenuItem menuItem;
    ZoomableImageView touch;
    InterstitialAd mInterstitialAd;
    boolean isShown =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fulll_screen_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();


        if(intent!=null)
        {
            if(intent.hasExtra("imageurl"))
             imageurl = intent.getStringExtra("imageurl");
        }
       touch = (ZoomableImageView)findViewById(R.id.imageView);

        Glide.with(this).load(imageurl).asBitmap().placeholder(R.drawable.ic_photo_library_black_24dp).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                assert touch != null;
                touch.setImageBitmap(resource);
                if(menuItem!=null)
                 menuItem.setVisible(true);

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Check if the application has draw over other apps permission or not?
                //This permission is by default available for API<23. But for API > 23
                //you have to ask for the permission in runtime.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(FulllScreenImageView.this)) {

                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                } else {
                    initializeView();
                }



            }
        });
       mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_idhome));

        // [START create_interstitial_ad_listener]
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                // Ad received, ready to display
                // [START_EXCLUDE]
                // [END_EXCLUDE]
                // [END_EXCLUDE]

                if(!isShown) {
                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
                        isShown =true;
                    }
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                // See https://goo.gl/sCZj0H for possible error codes.
                Log.w("HOMEFRAG", "onAdFailedToLoad:" + i);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void initializeView() {
        Intent intent = new Intent(FulllScreenImageView.this, FloatingViewService.class);
        intent.putExtra("imageurl",imageurl);
        startService(intent);

        finish();
    }

    //2746
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available.",
                        Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_fullscreenimageview, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        menuItem = item;
        item.setVisible(false);

        if(item.getItemId()==R.id.exportimage)
        {
            saveImageToGallery(touch);
        }

        return super.onOptionsItemSelected(item);
    }
    private void saveImageToGallery(ImageView imageview){
        String timeStamp = UUID.randomUUID().toString().replaceAll("-", "");
        imageview.setDrawingCacheEnabled(true);
        Bitmap b = imageview.getDrawingCache();
        MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), b,timeStamp, "newclashbases");
    }
}
