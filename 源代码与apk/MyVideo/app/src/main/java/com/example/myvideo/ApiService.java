package com.example.myvideo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

// 用于描述网络请求的接口
public interface ApiService {
    @GET("invoke/video")
    Call<ResponseBody> getData();
}
