package com.recycler.recyclerviewdemo.http;

import com.recycler.recyclerviewdemo.bean.ImageData;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by WangYu on 2017/7/19.
 */

public interface ApiService {
//    //post请求
//    @POST("login/index")
//    @FormUrlEncoded
//    Flowable<Login> postLogin(@Field("account") String username, @Field("pwd") String pwd, @Field("accessPort") int prot, @Field("version") String version);
//    //get请求
//    @GET("appcms/cmsunlogin")
//    Flowable<CmsEntity> getInfo();
    @GET("福利/{num}/{page}")
    Flowable<ImageData> getImageData(@Path("num") int num,@Path("page") int page);
}
