package com.example.yinp.gank.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.yinp.gank.bean.FeedImageInfo;

import java.util.ArrayList;

public class DriverViewTarget extends BitmapImageViewTarget {

    private FeedImageInfo mFeedImageInfo;

    public DriverViewTarget(ImageView mImageView, FeedImageInfo feedImageInfo) {
        super(mImageView);
        mFeedImageInfo = feedImageInfo;
    }

    private void setCardViewLayoutParams(int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    @Override
    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
        int viewWidth = view.getWidth();
        float scale = bitmap.getWidth() / (viewWidth * 1.0f);
        int viewHeight = (int) (bitmap.getHeight() * scale);
        setCardViewLayoutParams(viewWidth, viewHeight);
        mFeedImageInfo.setSize(viewWidth, viewHeight);
        super.onResourceReady(bitmap, transition);
    }
}
