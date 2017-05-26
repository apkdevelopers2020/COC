package com.hashstar.cocbases.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hashstar.cocbases.R;
import com.hashstar.cocbases.activity.BasesCatsActivity;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class TownHallGridItemAdapter extends RecyclerView.Adapter<TownHallGridItemAdapter.MyViewHolder> {

    private Activity mContext;
    private List<TownHallGridItem> albumList;
    Typeface tf;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public TextView thtext;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            thtext = (TextView) view.findViewById(R.id.townhalltitle);

        }
    }


    public TownHallGridItemAdapter(Activity mContext, List<TownHallGridItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
         tf = Typeface.createFromAsset(mContext.getAssets(),"Supercell-magic-webfont.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.samplealbum_video, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TownHallGridItem gridItem = albumList.get(position);


        // loading album cover using Glide library
        Glide.with(mContext).load(gridItem.getUrl()).placeholder(R.drawable.ic_photo_library_black_24dp).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.thumbnail);




        holder.thtext.setText(mContext.getString(R.string.townhallgridtitle)+gridItem.getOrder());
        holder.thtext.setTypeface(tf);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(mContext, BasesCatsActivity.class);
                intent.putExtra("tharray",gridItem.getJsonArray().toString());
                intent.putExtra("townhallnode", gridItem.getName());
                mContext.startActivityForResult(intent,5);
            }
        });
        

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
