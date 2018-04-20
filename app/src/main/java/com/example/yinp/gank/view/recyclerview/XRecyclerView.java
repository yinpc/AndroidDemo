package com.example.yinp.gank.view.recyclerview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author yinp
 * 去除pullrefresh封装，使用时外层套用SwipeRefreshLayout
 * https://github.com/XRecyclerView/XRecyclerView/blob/master/xrecyclerview/src/main/java/com/jcodecraeer/xrecyclerview/XRecyclerView.java
 */
public class XRecyclerView extends RecyclerView {
    private static final int TYPE_FOOTER = 10000;
    private static final int TYPE_HEADER = 10001;
    private WrapAdapter mWrapAdapter;
    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private View mFootView;
    private LoadingListener mLoadingListener;
    private boolean loadingMoreEnabled = true;
    private boolean isNoMore;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

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
        mFootView = new LoadingMoreFooter(context);
        mFootView.setVisibility(GONE);
    }

    public void destroy() {
        if (mHeaderViews != null) {
            mHeaderViews.clear();
            mHeaderViews = null;
        }
        mFootView = null;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

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
                View footView = mFootView;
                ((LoadingMoreFooter) footView).setState(LoadingMoreFooter.STATE_LOADING);
                if (isNetWorkConnected(getContext())) {
                    mLoadingListener.onLoadMore();
                } else {
                    //todo: network error
                }
            }
        }
    }

    public void addHeaderView(View view) {
        if (mHeaderViews != null) {
            mHeaderViews.put(mHeaderViews.size(), view);
        }
        if (mWrapAdapter != null) {
            mWrapAdapter.notifyDataSetChanged();
        }
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        ((LoadingMoreFooter) mFootView).setState(LoadingMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        ((LoadingMoreFooter) mFootView).setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
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
            if (mConnectivityManager != null) {
                NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        }
        return false;
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
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
    }

    private class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter adapter;

        private int headerPosition = 0;

        WrapAdapter(@NonNull RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        private boolean isHeader(int position) {
            if (mHeaderViews.size() > 0) {
                return position >= 0 && position < mHeaderViews.size();
            }
            return false;
        }

        private boolean isFooter(int position) {
            if (loadingMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        private int getHeadersCount() {
            if (mHeaderViews == null) {
                return 0;
            }
            return mHeaderViews.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                return new SimpleViewHolder(mHeaderViews.get(headerPosition++));
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int innerPosition = position - getHeadersCount();
            if (adapter != null) {
                if (innerPosition < adapter.getItemCount()) {
                    return adapter.getItemViewType(innerPosition);
                }
            }
            return 0;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            int innerPosition = position - getHeadersCount();
            if (adapter != null) {
                if (innerPosition < adapter.getItemCount()) {
                    adapter.onBindViewHolder(holder, innerPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            int adjLen = loadingMoreEnabled ? 1 : 0;
            if (adapter != null) {
                return getHeadersCount() + adapter.getItemCount() + adjLen;
            } else {
                return getHeadersCount() + adjLen;
            }
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int innerPosition = position - getHeadersCount();
                if (innerPosition < adapter.getItemCount()) {
                    return adapter.getItemId(innerPosition);
                }
            }
            return -1;
        }

        //http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/1120/3705.html
        //解决GirdLayout头部和尾部可能不占据一行的问题
        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager mLayoutManager = recyclerView.getLayoutManager();
            if (mLayoutManager instanceof GridLayoutManager) {
                final GridLayoutManager mGridLayoutManager = (GridLayoutManager) mLayoutManager;
                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position))
                                ? mGridLayoutManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        //解决StaggeredGridLayout头部和尾部可能不占据一行的问题
        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams mLayoutParams = holder.itemView.getLayoutParams();
            if (mLayoutParams != null
                    && mLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || (isFooter(holder.getLayoutPosition())))) {
                ((StaggeredGridLayoutManager.LayoutParams) mLayoutParams).setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewRecycled(@NonNull ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(@NonNull ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        @Override
        public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public interface LoadingListener {
        void onLoadMore();
    }
}
