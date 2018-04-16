package com.example.yinp.gank.ui.gank.child;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
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

        loadData(true);
        initRefreshView();
    }

    private void initRefreshView() {
        bindingView.srlWelfare.setColorSchemeColors(getResources().getColor(R.color.colorTheme));
        bindingView.srlWelfare.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlWelfare.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(true);
                    }
                }, 100);
            }
        });
    }

    private void loadData(final boolean isRefresh){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HttpClient client = retrofit.create(HttpClient.class);
        Call<GankIoDataBean> call = client.getGankIoData("福利", 1, 20);
        call.enqueue(new Callback<GankIoDataBean>() {
            @Override
            public void onResponse(Call<GankIoDataBean> call, Response<GankIoDataBean> response) {
//                System.out.print(response.body().toString());
//                Toast.makeText(getContext(),
//                        response.body().getResults().get(0).getUrl(),
//                        Toast.LENGTH_LONG)
//                        .show();
//                ArrayList<String> mUrls = new ArrayList<>();
                if (isRefresh){
                    feedImageInfos.clear();
                }
                adapter.notifyDataSetChanged();

                for(GankIoDataBean.ResultBean s : response.body().getResults()){
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
