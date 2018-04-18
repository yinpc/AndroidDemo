package com.example.yinp.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yinp.gank.R;
import com.example.yinp.gank.bean.FeedImageInfo;

import java.util.ArrayList;

public class WelfareRecyclerViewAdapter extends RecyclerView.Adapter<WelfareRecyclerViewAdapter.ViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<FeedImageInfo> mFeedImageInfos;

    public WelfareRecyclerViewAdapter(Context context, ArrayList<FeedImageInfo> feedImageInfos) {
//        mTitles = context.getResources().getStringArray(R.array.titles);
        mFeedImageInfos = feedImageInfos;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_welfare, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        String url = mFeedImageInfos.get(position).getUrl();

        // dynamic height
//            RequestOptions options = new RequestOptions()
//                    .placeholder(new ColorDrawable(Color.BLACK))
//                    .override(holder.mImageView.getWidth(), BitmapImageViewTarget.SIZE_ORIGINAL)
//                    .fitCenter();
//
//            Glide.with(mContext)
//                    .asBitmap()
//                    .apply(options)
//                    .load(url)
//                    .into(new DriverViewTarget(holder.mImageView, mFeedImageInfos.get(position)));

        Glide.with(mContext)
                .load(mFeedImageInfos.get(position).getUrl())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mFeedImageInfos == null ? 0 : mFeedImageInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.view_img_feed);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "click", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
