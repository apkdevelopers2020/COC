package com.hashstar.cocbases.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.floatingwidget.FloatingViewService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public static final int SELECT_IMAGE = 2020;
    private static final int RESULT_LOAD_IMG = 2020;

    ImageView imageView;
    Button button;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private InterstitialAd mInterstitialAd;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        if(mInterstitialAd!=null)
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (ImageView)view.findViewById(R.id.imageView);
        button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {

                    //If the draw over permission is not available open the settings screen
                    //to grant the permission.
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                }
                else {
                    Intent intent;
                    Boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

                    if (isSDPresent) {
                        // yes SD-card is present
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    } else {
                        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    }

                    startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), SELECT_IMAGE);
                }
            }
        });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                && null != data) {

            Uri selectedImageUri = data.getData();
            String tempPath = getPath(selectedImageUri, getActivity());
            String url = data.getData().toString();
            String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/NewClashBases/";
            File tempFile = new File(path);
            if(!tempFile.exists())
            {
                tempFile.mkdir();
            }
            if (url.startsWith("content://com.google.android.apps.photos.content")){
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(selectedImageUri);
                    if (is != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        //You can use this bitmap according to your purpose or Set bitmap to imageview

                        if(new File(tempFile.getAbsolutePath()+"/file.png").exists())
                        {
                            new File(tempFile.getAbsolutePath()+"/file.png").delete();
                        }

                        FileOutputStream outStream = new FileOutputStream(new File(tempFile.getAbsolutePath()+"/file.png"));
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Crashlytics.logException(e); e.printStackTrace();
                }
            }
            else
            {
                try {
                    Log.e("tempPath", tempPath);
                    File file = new File(tempPath);


                    if(tempFile.exists())
                        tempFile.mkdirs();


                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(file), options);

                    if(new File(tempFile+"/file.png").exists())
                    {
                        new File(tempFile+"/file.png").delete();
                    }

                    FileOutputStream outStream = new FileOutputStream(new File(tempFile+"/file.png"));
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                }
                catch (Exception e)
                {
                    Crashlytics.logException(e); e.printStackTrace();
                }
            }

            String pathpng = tempFile.getPath()+"/file.png";
            Intent intent = new Intent(getActivity(), FloatingViewService.class);
            intent.putExtra("path",pathpng);
            getActivity().startService(intent);


        }
        else
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == Activity.RESULT_OK) {

            } else { //Permission is not available
                Toast.makeText(getActivity(),
                        "Draw over other app permission not available.",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String getPath(Uri uri, Activity activity) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.MediaColumns.DATA};
            cursor = activity.getContentResolver().query(uri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }
        return "";
    }




}
