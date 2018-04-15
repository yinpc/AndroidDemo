package com.example.yinp.gank.ui.gank.child;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yinp.gank.R;
import com.example.yinp.gank.databinding.FragmentWelfareBinding;
import com.example.yinp.gank.view.statusbar.MyRecyclerViewAdapter;

import java.util.ArrayList;

public class WelfareFragment extends Fragment {

    private FragmentWelfareBinding bindingView;

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
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        bindingView.recyclerView.setLayoutManager(layoutManager);
        bindingView.recyclerView.setAdapter(new MyRecyclerViewAdapter(this.getActivity(), getData()));
    }

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        String temp = " item";
        for (int i = 1; i <= 100; i++) {
            data.add(i + temp);
        }
        return data;
    }
}
