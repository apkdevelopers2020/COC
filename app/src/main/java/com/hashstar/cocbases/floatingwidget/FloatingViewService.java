package com.hashstar.cocbases.floatingwidget;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hashstar.cocbases.Application;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.activity.MainActivity;
import com.hashstar.cocbases.switchButtonListener;
import com.hashstar.cocbases.utilities.Constants;
import com.hashstar.cocbases.utilities.StoreData;

import java.io.File;


public class FloatingViewService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener{

    private WindowManager mWindowManager;
    private View mFloatingView;
    String imageurl;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams landscapeParams;
    WindowManager.LayoutParams nonTouchableParams;
    SharedPreferences prefs;
    LinearLayout android_task_bar;
    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
        final ZoomableImageView touch = (ZoomableImageView)mFloatingView.findViewById(R.id.zoomable);
        android_task_bar = (LinearLayout)mFloatingView.findViewById(R.id.android_task_bar);

       if(intent!=null)
       {
           if(intent.hasExtra("imageurl"))
           {
               StoreData.SaveString("source","imageurl",getApplicationContext());
               imageurl = intent.getStringExtra("imageurl");
               StoreData.SaveString(Constants.BASE_IMAGE_URL,imageurl,getApplicationContext());

               Glide.with(this).load(imageurl).asBitmap().placeholder(R.drawable.ic_photo_library_black_24dp).into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                       assert touch != null;
                       touch.setImageBitmap(resource);
                   }
               });

           }
           else if(intent.hasExtra("path"))
           {
              StoreData.SaveString("source","path",getApplicationContext());
              String path = intent.getStringExtra("path");
               StoreData.SaveString("imgpath",path,getApplicationContext());
               Glide.with(this).load(new File(path)).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                   .placeholder(R.drawable.ic_photo_library_black_24dp).into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                       assert touch != null;
                       touch.setImageBitmap(resource);
                   }
               });

           }
           else
           {
               Log.e("intent","nullin");
           }
       }
        else
       {
           Log.e("intent","null");
           String source =  StoreData.LoadString("source","imageurl",getApplicationContext());
           if(source.equals("imageurl"))
           {
               StoreData.SaveString("source","imageurl",getApplicationContext());
               imageurl = StoreData.LoadString(Constants.BASE_IMAGE_URL,"",getApplicationContext());
               StoreData.SaveString(Constants.BASE_IMAGE_URL,imageurl,getApplicationContext());

               Glide.with(this).load(imageurl).asBitmap().placeholder(R.drawable.ic_photo_library_black_24dp).into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                       assert touch != null;
                       touch.setImageBitmap(resource);
                   }
               });

           }
           else if(source.equals("path"))
           {
               StoreData.SaveString("source","path",getApplicationContext());
               String path = StoreData.LoadString("imgpath","",getApplicationContext());

               Glide.with(this).load(new File(path)).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE)
                       .placeholder(R.drawable.ic_photo_library_black_24dp).into(new SimpleTarget<Bitmap>() {
                   @Override
                   public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                       assert touch != null;
                       touch.setImageBitmap(resource);
                   }
               });

           }
       }
        startForeground(1,startNotification());

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);


        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;



        //Add the view to the window.
        landscapeParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        landscapeParams.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        landscapeParams.x = 0;
        landscapeParams.y = 100;

        //Add the view to the window.
        nonTouchableParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT|WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        nonTouchableParams.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        nonTouchableParams.x = 0;
        nonTouchableParams.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);


        //Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                mWindowManager.updateViewLayout(mFloatingView, params);
            }
        });

        //Set the close button
        ImageView lock = (ImageView) mFloatingView.findViewById(R.id.lock_button);
        lock.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                startNotification();
                startNotificationOther();
                android_task_bar.setVisibility(View.INVISIBLE);
                mWindowManager.updateViewLayout(mFloatingView, nonTouchableParams);
                mFloatingView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN|View.STATUS_BAR_HIDDEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


            }
        });

        //Set the close button
        ImageView expand = (ImageView) mFloatingView.findViewById(R.id.expand);
        expand.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                startNotification();
                startNotificationOther();
                mWindowManager.updateViewLayout(mFloatingView, landscapeParams);
                mFloatingView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



            }
        });

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {



                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            //remember the initial position.
                            initialX = params.x;
                            initialY = params.y;

                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_UP:
                            int Xdiff = (int) (event.getRawX() - initialTouchX);
                            int Ydiff = (int) (event.getRawY() - initialTouchY);

                            //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                            //So that is click event.
                            if (Xdiff < 10 && Ydiff < 10) {
                                if (isViewCollapsed()) {
                                    //When user clicks on the image view of the collapsed layout,
                                    //visibility of the collapsed layout will be changed to "View.GONE"
                                    //and expanded view will become visible.
                                    collapsedView.setVisibility(View.GONE);
                                    expandedView.setVisibility(View.VISIBLE);
                                }
                            }
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);

                            //Update the layout with new X & Y coordinate
                            mWindowManager.updateViewLayout(mFloatingView, params);
                            return true;
                    }
                return false;

            }
        });
    }


    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager =
                (NotificationManager) Application.getInstance().getSystemService(ns);
        notificationManager.cancel(1);
        notificationManager.cancel(2);
    }

    private Notification startNotification(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.logo, null,
                System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.mynotification);

        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notification.contentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags |= Notification.FLAG_NO_CLEAR;

        //this is the intent that is supposed to be called when the
        //button is clicked
        Intent switchIntent = new Intent(this, switchButtonListener.class);
        switchIntent.putExtra("close","close");
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(this, 0,
                switchIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.closeOnFlash,
                pendingSwitchIntent);

        notificationManager.notify(1, notification);

        return notification;
    }

    private void startNotificationOther(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.logo, null,
                System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.mynotificationtouchable);

        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notification.contentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags |= Notification.FLAG_NO_CLEAR;

        //this is the intent that is supposed to be called when the
        //button is clicked
        Intent unlock = new Intent(this, UnlockButtonListener.class);
        unlock.putExtra("unlock","unlock");
        PendingIntent unlockPendingIntent = PendingIntent.getBroadcast(this, 0,
                unlock, 0);
        notificationView.setOnClickPendingIntent(R.id.nloackview,
                unlockPendingIntent);

        notificationManager.notify(2, notification);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("update".equals(key)) {
            String paramstr = StoreData.LoadString(key, "1", getApplicationContext());

            mWindowManager.updateViewLayout(mFloatingView, params);
            android_task_bar.setVisibility(View.VISIBLE);
            mFloatingView.setSystemUiVisibility(View.STATUS_BAR_VISIBLE);
        } else if (key.equals("stop")) {
            stopSelf();
        }
    }
    }

