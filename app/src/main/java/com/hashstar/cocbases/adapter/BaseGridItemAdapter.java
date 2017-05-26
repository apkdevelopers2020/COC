package com.hashstar.cocbases.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.activity.FulllScreenImageView;

import java.util.List;

/**
 * Created by phoenix on 21/4/17.
 */
public class BaseGridItemAdapter extends RecyclerView.Adapter<BaseGridItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<BaseGrid> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView thtext;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            thtext = (TextView) view.findViewById(R.id.townhalltitle);
        }
    }


    public BaseGridItemAdapter(Context mContext, List<BaseGrid> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.samplealbum_video, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BaseGrid gridItem = albumList.get(position);

        holder.thtext.setVisibility(View.GONE);
        // loading album cover using Glide library
        Glide.with(mContext).load(gridItem.getUrl()).placeholder(R.drawable.ic_photo_library_black_24dp).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, FulllScreenImageView.class);
                intent.putExtra("imageurl",gridItem.getUrl());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
