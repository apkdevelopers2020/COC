package com.hashstar.cocbases.notifications;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hashstar.cocbases.R;

import java.util.ArrayList;

/**
 * Created by suhas on 25/4/17 .
 */

public class CustomAdapter extends ArrayAdapter<NotiItem> implements View.OnClickListener{

    private ArrayList<NotiItem> dataSet;
    Activity mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        ImageView info;
    }

    public CustomAdapter(ArrayList<NotiItem> data, Activity context) {
        super(context, R.layout.notificationitem, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        NotiItem dataModel=(NotiItem)object;

        switch (v.getId())
        {
            case R.id.item_info:

                break;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotiItem dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.notificationitem, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.titlenoti);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        assert dataModel != null;
        viewHolder.txtName.setText(dataModel.getTitle());
        viewHolder.txtType.setText(dataModel.getDescreption());
        Glide.with(mContext).load(dataModel.getUrl()).into(viewHolder.info);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}