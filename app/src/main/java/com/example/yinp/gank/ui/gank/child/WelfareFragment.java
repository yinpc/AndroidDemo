package com.example.yinp.gank.ui.gank.child;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yinp.gank.R;
import com.example.yinp.gank.bean.FeedImageInfo;
import com.example.yinp.gank.bean.GankIoDataBean;
import com.example.yinp.gank.databinding.FragmentWelfareBinding;
import com.example.yinp.gank.http.HttpClient;
import com.example.yinp.gank.adapter.WelfareRecyclerViewAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelfareFragment extends Fragment {

    private FragmentWelfareBinding bindingView;
    private WelfareRecyclerViewAdapter adapter;
    private ArrayList<FeedImageInfo> feedImageInfos;
    private int page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_welfare, null);
        bindingView = DataBindingUtil.inflate(inflater, R.layout.fragment_welfare, null, false);
        return bindingView.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        bindingView.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        bindingView.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd")
//                .create();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        bindingView.recyclerView.setLayoutManager(layoutManager);
        feedImageInfos = new ArrayList<>();
        adapter = new WelfareRecyclerViewAdapter(getContext(), feedImageInfos);
        bindingView.recyclerView.setAdapter(adapter);

        page = 1;
        loadData(true, page);
        initRefreshView();
        initLoadMoreListener();
    }

    private void initLoadMoreListener() {
        bindingView.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (adapter != null) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 1 == adapter.getItemCount()) {
                        bindingView.srlWelfare.setRefreshing(true);
                        page++;
                        loadData(false, page);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
//                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
//                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                lastVisibleItem = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
//                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
//                    Toast.makeText(RecyclerActivity.this, "滑动到底了", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void initRefreshView() {
        bindingView.srlWelfare.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        bindingView.srlWelfare.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlWelfare.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        loadData(true, page);
                    }
                }, 100);
            }
        });
    }

    private void loadData(final boolean isRefresh, int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpClient client = retrofit.create(HttpClient.class);
        Call<GankIoDataBean> call = client.getGankIoData("福利", page, 20);
        call.enqueue(new Callback<GankIoDataBean>() {
            @Override
            public void onResponse(Call<GankIoDataBean> call, Response<GankIoDataBean> response) {
//                System.out.print(response.body().toString());
//                Toast.makeText(getContext(),
//                        response.body().getResults().get(0).getUrl(),
//                        Toast.LENGTH_LONG)
//                        .show();
//                ArrayList<String> mUrls = new ArrayList<>();
                if (isRefresh) {
                    feedImageInfos.clear();
                }
                adapter.notifyDataSetChanged();

                for (GankIoDataBean.ResultBean s : response.body().getResults()) {
//                    mUrls.add(s.getUrl());
                    feedImageInfos.add(new FeedImageInfo(s.getUrl()));
                }

                adapter.notifyDataSetChanged();
                bindingView.srlWelfare.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<GankIoDataBean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
