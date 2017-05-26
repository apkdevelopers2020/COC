package com.hashstar.cocbases.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.adapter.BaseGrid;
import com.hashstar.cocbases.adapter.BaseGridItemAdapter;
import com.hashstar.cocbases.utilities.Constants;
import com.hashstar.cocbases.utilities.GridSpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OneFragment extends Fragment{

    JSONArray jsonArray;

    ArrayList<BaseGrid>  arrayList;

    BaseGridItemAdapter baseGridItems;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        arrayList=new ArrayList<>();

        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        baseGridItems = new BaseGridItemAdapter(getActivity(),arrayList);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(baseGridItems);




        Bundle args = getArguments();
        String index = args.getString("baseimages", "Not found");
        String catname = args.getString("catName", "Not found");
        String townhallnode = args.getString("townhallnode", "Not found");

        String BASE_URL_IMG = Constants.BASE_LINK+townhallnode+"/"+catname+"/";

        Log.e("index",index);
        try {
            jsonArray = new JSONArray(index);

            for (int i = 0 ; i<jsonArray.length();i++)
            {
                JSONObject node = jsonArray.getJSONObject(i);
                String file =  node.getString("file");
                String s =  BASE_URL_IMG+file;
                BaseGrid  baseGrid = new BaseGrid(file,s);
                Log.v("Image Url ",s);
                arrayList.add(baseGrid);
            }






        } catch (JSONException e) {
            Crashlytics.logException(e); e.printStackTrace();
        }


        super.onViewCreated(view, savedInstanceState);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
