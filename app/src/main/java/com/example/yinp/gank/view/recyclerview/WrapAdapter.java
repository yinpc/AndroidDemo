package com.example.yinp.gank.view.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author yinp
 * https://github.com/XRecyclerView/XRecyclerView/blob/master/xrecyclerview/src/main/java/com/jcodecraeer/xrecyclerview/XRecyclerView.java
 * todo: 考虑放到XRecyclerView类下
 */
public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = -1;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    private RecyclerView.Adapter adapter;
    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFootViews;

//    private int headerPosition = 1;

    public WrapAdapter(SparseArray<View> mHeaderViews,
                       SparseArray<View> mFootViews,
                       @NonNull RecyclerView.Adapter adapter) {
        this.mHeaderViews = mHeaderViews;
        this.mFootViews = mFootViews;
        this.adapter = adapter;
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
    }

    //解决StaggeredGridLayout头部和尾部可能不占据一行的问题
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams mLayoutParams = holder.itemView.getLayoutParams();
        if (mLayoutParams != null
                && mLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || (isFooter(holder.getLayoutPosition())))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) mLayoutParams;
            p.setFullSpan(true);
        }
    }

    private boolean isHeader(int position) {
        if (mHeaderViews.size() > 0) {
            return position >= 0 && position < mHeaderViews.size();
        }
        return false;
    }

    private boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - mFootViews.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new SimpleViewHolder(mHeaderViews.get(0));
        } else if (viewType == TYPE_FOOTER) {
            return new SimpleViewHolder(mFootViews.get(0));
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
        return TYPE_NORMAL;
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
        if (adapter != null) {
            return getHeadersCount() + adapter.getItemCount() + getFootersCount();
        } else {
            return getHeadersCount() + getFootersCount();
        }
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
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

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
