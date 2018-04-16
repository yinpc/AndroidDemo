package com.example.yinp.gank.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.yinp.gank.R;
import com.example.yinp.gank.bean.FeedImageInfo;
import com.example.yinp.gank.view.DriverViewTarget;

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
            String url = mFeedImageInfos.get(position).getUrl();

            RequestOptions options = new RequestOptions()
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .override(holder.mImageView.getWidth(), BitmapImageViewTarget.SIZE_ORIGINAL)
                    .fitCenter();

            Glide.with(mContext)
                    .asBitmap()
                    .apply(options)
                    .load(url)
                    .into(new DriverViewTarget(holder.mImageView, mFeedImageInfos.get(position)));
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
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
//                    Log.d("NormalTextViewHolder", "onClick--> position = " + getLayoutPosition());
                    Toast.makeText(mContext, "click", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
