package com.example.yinp.gank.viewmodel.gank;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.yinp.gank.bean.GankIoDataBean;
import com.example.yinp.gank.data.remote.WelfareRepository;

public class WelfareViewModel extends ViewModel {
    private WelfareRepository welfareRepo;

    public WelfareViewModel() {
        this.welfareRepo = new WelfareRepository();
    }

    public LiveData<GankIoDataBean> getWelfareData(String id, int page, int prePage) {
        return welfareRepo.getWelfareData(id, page, prePage);
    }
}
