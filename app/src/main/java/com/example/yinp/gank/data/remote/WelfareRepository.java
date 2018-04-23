package com.example.yinp.gank.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.yinp.gank.bean.GankIoDataBean;
import com.example.yinp.gank.http.HttpClient;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton  // informs Dagger that this class should be constructed once
public class WelfareRepository {
    public LiveData<GankIoDataBean> getWelfareData(String id, int page, int prePage) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gank.io/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MutableLiveData<GankIoDataBean> data = new MutableLiveData();
        retrofit.create(HttpClient.class).getGankIoData(id, page, prePage).enqueue(new Callback<GankIoDataBean>() {
            @Override
            public void onResponse(Call<GankIoDataBean> call, Response<GankIoDataBean> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GankIoDataBean> call, Throwable t) {

            }
        });
        return data;
    }
}
