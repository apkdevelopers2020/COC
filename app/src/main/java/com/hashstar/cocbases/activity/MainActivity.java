package com.hashstar.cocbases.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.plus.PlusOneButton;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.fragment.HomeFragment;
import com.hashstar.cocbases.fragment.PhotosFragment;
import com.hashstar.cocbases.fragment.SettingsFragment;
import com.hashstar.cocbases.other.CircleTransform;
import com.hashstar.cocbases.utilities.Constants;
import com.hashstar.cocbases.utilities.StoreData;

//import com.hashstar.cocbases.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity {



    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    // urls to load navigation header background image
    // and profile image
    private static final int urlNavHeaderBg = R.drawable.navheader;
    private static  String urlProfileImg = "";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    PlusOneButton.OnPlusOneClickListener onPlusOneClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);



        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "New Clash Bases");
                    String sAux = "WORRIED.   !!?\n" +
                            "Want to update the COC base !!!! \n" +
                            "But you are out of time?\n" +
                            "Here we are with a innovative solution for it. Just polish ur old skills of drawing diagrams with tracepapers and u r there with a new base within few minutes.\n" +
                            "Notable features:\n" +
                            "1) Base tracepaper\n" +
                            "2) Import from our collection\n" +
                            "3) import from your collection\n" +
                            "4)Export our collection.\n" +
                            "       Waiting for ur helpful responses which will make the COC game a better place.\n" +
                            "\n" +
                            "Here is the tutorial for it.\n" +
                            "\n" +
                            "https://youtu.be/KhHtb7x8bsI\n" +
                            "\n" +
                            "Download from GOOGLE PLAY STORE--\n" +
                            "https://play.google.com/store/apps/details?id=com.hashstar.cocbases";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.hashstar.cocbases \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Choose One"));
                } catch(Exception e) {
                    //e.toString();
                    Crashlytics.logException(e); e.printStackTrace();
                }

            }
        });



        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        if(!checkWriteExternalPermission())
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
        }

         onPlusOneClickListener =  new PlusOneButton.OnPlusOneClickListener() {
            @Override
            public void onPlusOneClick(Intent intent) {

            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
//        mPbutton_standard.initialize(APPURL,onPlusOneClickListener);



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
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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


    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(StoreData.LoadString(Constants.NAME,"Anonymous",getApplicationContext()));
        txtWebsite.setText(StoreData.LoadString(Constants.EMAIL,"anonymous",getApplicationContext()));
        urlProfileImg = StoreData.LoadString(Constants.IMGURL,"",getApplicationContext());
        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .placeholder(R.drawable.anonymous_profile_pic)
                .error(R.drawable.anonymous_profile_pic)
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 2:
                // movies fragment
               /* MoviesFragment moviesFragment = new MoviesFragment();
                return moviesFragment;*/

            case 3:
                // notifications fragment
     /*           NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;*/
            SettingsFragment settingsFragment = new SettingsFragment();
            return settingsFragment;

            case 4:
                // settings fragment
                /*SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;*/
            default: {
                return new HomeFragment();

            }
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_movies:
                        Intent intent = new Intent(getApplicationContext(),TutorialsActivity.class);
                        //TODO put VideoID
                        intent.putExtra("VIDEO_ID","KhHtb7x8bsI");
                        startActivity(intent);
                        break;
           /*         case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;*/
                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                    {
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                }}

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("res",""+requestCode+resultCode);
    }
}
