package com.hashstar.cocbases.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.fragment.OneFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class BasesCatsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    Intent intent;
    String tharray,townhallnode;

    private InterstitialAd mInterstitialAd;
    //    PlusOneButton mPbutton_standard;
    private static final int REQUEST_CODE = 0;
    //app's Google plus page URL
    private static final String APPURL = "https://play.google.com/store/apps/details?id=com.hashstar.cocbases";
    boolean isShown =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseslayout);


        intent = getIntent();
        if(intent!=null)
        {
            if(intent.hasExtra("tharray"))
            {
                 tharray = intent.getStringExtra("tharray");
                 townhallnode = intent.getStringExtra("townhallnode");
            }
        }

        Log.e("tharray",tharray);




        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);
        // [START instantiate_interstitial_ad]
        // Create an InterstitialAd object. This same object can be re-used whenever you want to
        // show an interstitial.
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
                if(!isShown) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
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
        // [END create_interstitial_ad_listener]al_ad_listener]

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        try {
            JSONArray jsonArray = new JSONArray(tharray);

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonnode = jsonArray.getJSONObject(i);
                String name =  jsonnode.getString("name");
                adapter.addFragment(new OneFragment(), name,jsonnode.getJSONArray("dir").toString(),name);
            }

        } catch (JSONException e) {
            Crashlytics.logException(e); e.printStackTrace();
        }


        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title,String baseimages,String catName) {

            Bundle args = new Bundle();
            args.putString("baseimages", baseimages);
            args.putString("catName", catName);
            args.putString("townhallnode", townhallnode);
            fragment.setArguments(args);

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;

        }

        return super.onOptionsItemSelected(item);
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
}
