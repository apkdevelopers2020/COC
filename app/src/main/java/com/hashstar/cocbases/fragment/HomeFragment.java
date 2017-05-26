package com.hashstar.cocbases.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.plus.PlusOneButton;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.adapter.TownHallGridItem;
import com.hashstar.cocbases.adapter.TownHallGridItemAdapter;
import com.hashstar.cocbases.data.ServiceUrls;
import com.hashstar.cocbases.network.ServerResponseHandler;
import com.hashstar.cocbases.network.WebService;
import com.hashstar.cocbases.utilities.Constants;
import com.hashstar.cocbases.utilities.GridSpacingItemDecoration;
import com.hashstar.cocbases.utilities.StoreData;
import com.hashstar.cocbases.utilities.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<TownHallGridItem> albumList;
    TownHallGridItemAdapter adapter;
    FloatingActionButton floatingActionButton;
    private InterstitialAd mInterstitialAd;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
       /*
    * Plus one*/

    //    PlusOneButton mPbutton_standard;
    private static final int REQUEST_CODE = 0;
    //app's Google plus page URL
    private static final String APPURL = "https://play.google.com/store/apps/details?id=com.hashstar.cocbases";
//    private static final String APPURL = "https://play.google.com/store/apps/details?id=com.hashstar.cocbases";

    PlusOneButton mPbutton_standard;

    /*
    * Plus one end
    * */

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPbutton_standard.initialize(APPURL, REQUEST_CODE);

        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new TownHallGridItemAdapter(getActivity(), albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        mPbutton_standard = (PlusOneButton)view.findViewById(R.id.plus_one);
         floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
         TextView button = (TextView) view.findViewById(R.id.button2);

        LikeView likeView = (LikeView) view.findViewById(R.id.like);
        likeView.setObjectIdAndType(
                "https://www.facebook.com/New-CLASH-BASES-289084064852399",
                LikeView.ObjectType.PAGE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(APPURL))
                        .setContentDescription("Made My COC Base in minutes. Now make yours.")
                        .build();

                ShareDialog shareDialog = new ShareDialog(getActivity());
                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }
        });


        if(albumList.size()==0)
        {
           String response = StoreData.LoadString(Constants.RESPONSE,"{}",getActivity());
            try {

            JSONObject object  = new JSONObject(response);

            if(response.equals("{}"))
                    new LoadBases().execute();
            else
                    showItems(object);

            } catch (JSONException e) {
                Crashlytics.logException(e); e.printStackTrace();
            }
        }

        // [START instantiate_interstitial_ad]
        // Create an InterstitialAd object. This same object can be re-used whenever you want to
        // show an interstitial.
        mInterstitialAd = new InterstitialAd(getActivity());
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

            }

            @Override
            public void onAdFailedToLoad(int i) {
                // See https://goo.gl/sCZj0H for possible error codes.
                Log.w("HOMEFRAG", "onAdFailedToLoad:" + i);
            }
        });
        // [END create_interstitial_ad_listener]

        super.onViewCreated(view, savedInstanceState);
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }




    public class LoadBases extends AsyncTask <JSONObject,JSONObject,JSONObject>
    {

        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Please wait..");
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Bases are loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... jsonObjects) {

            HashMap<String,String> hashMap = new HashMap<>();

           JSONObject object =  WebService.sendServer(hashMap, ServiceUrls.LOADBASES,WebService.GET_REQUEST);
            return object;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            progressDialog.dismiss();

            if (ServerResponseHandler.handleResponse(getActivity(), jsonObject,floatingActionButton)) {

                try {

                    if(jsonObject.has("response")) {
                        if(jsonObject.getJSONArray("response").length()>0) {

                            StoreData.SaveString(Constants.RESPONSE,jsonObject.toString(),getActivity());
                            showItems(jsonObject);
                        }
                    }
                } catch (JSONException e) {
                    Crashlytics.logException(e); e.printStackTrace();
                }
            }
        }
    }

    private void showItems(JSONObject jsonObject) throws JSONException {
        JSONArray responseArray = jsonObject.getJSONArray("response");

        JSONArray townhallArray = new JSONArray();

        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject townhalljsonobject = responseArray.getJSONObject(i);
            String name = townhalljsonobject.getString("name");
            townhallArray.put(name);
            JSONArray dirJsonArray = townhalljsonobject.getJSONArray("dir");
//                    TownHallGridItem a = new TownHallGridItem(name,WebService.BASE_URL+ServiceUrls.COCDIR_THS+name+"png",dirJsonArray);
            int order = UtilityMethods.extractDigits(name);
            TownHallGridItem a = new TownHallGridItem(name, WebService.BASE_URL + ServiceUrls.COC_THS + name + ".png", dirJsonArray, order);
            albumList.add(a);
        }
        Comparator<TownHallGridItem> comp = new Comparator<TownHallGridItem>() {
            @Override
            public int compare(TownHallGridItem lhs, TownHallGridItem rhs) {

                return UtilityMethods.compare(lhs, rhs);
            }
        };
        Collections.sort(albumList, comp);


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
