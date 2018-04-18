package com.example.yinp.gank.view.recyclerview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * @author yinp
 * 去除pullrefresh封装，使用时外层套用SwipeRefreshLayout
 * https://github.com/XRecyclerView/XRecyclerView/blob/master/xrecyclerview/src/main/java/com/jcodecraeer/xrecyclerview/XRecyclerView.java
 * todo: 对照优化header和footer
 */
public class XRecyclerView extends RecyclerView {
    private WrapAdapter mWrapAdapter;
    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFootViews = new SparseArray<>();
    private LoadingListener mLoadingListener;
    private boolean loadingMoreEnabled = true;
    private boolean isNoMore;

    public void setLoadingListener(LoadingListener mLoadingListener) {
        this.mLoadingListener = mLoadingListener;
    }

    private boolean isLoadingData;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LoadingMoreFooter footer = new LoadingMoreFooter(context);
        footer.reset();
        mFootViews.put(0, footer);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFootViews, adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
    }

    private final RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE
                && mLoadingListener != null
                && !isLoadingData
                && loadingMoreEnabled) {
            LayoutManager mLayoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (mLayoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            }
            if (mLayoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= mLayoutManager.getItemCount() - 1
                    && !isNoMore) {
                isLoadingData = true;
                View footView = mFootViews.get(0);
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                if (isNetWorkConnected(getContext())) {
                    mLoadingListener.onLoadMore();
                } else {
                    //todo: network error
                }
            }
        }
    }

    public void clearHeader() {
        mHeaderViews.clear();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int height = (int) (1.0f * scale + 0.5f);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        View view = new View(getContext());
        view.setLayoutParams(params);
        mHeaderViews.put(0, view);
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size(), view);
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        ((LoadingMoreFooter) mFootViews.get(0)).setState(LoadingMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        ((LoadingMoreFooter) mFootViews.get(0)).setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
    }

    private int findMax(int[] into) {
        int max = into[0];
        for (int value : into) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public interface LoadingListener {
        void onLoadMore();
    }
}
