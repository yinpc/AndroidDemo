package com.example.yinp.gank.http;

import com.example.yinp.gank.bean.GankIoDataBean;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HttpClient {
    @GET("data/{type}/{pre_page}/{page}")
    Call<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);
}
