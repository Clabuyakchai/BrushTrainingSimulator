package com.example.clabuyakchai.brushtrainingsimulator.api;

import com.example.clabuyakchai.brushtrainingsimulator.model.ResponseStatistics;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserLogin;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserRegistration;
import com.example.clabuyakchai.brushtrainingsimulator.model.UserStatistics;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public interface ApiService {
    @POST("/users/signin")
    Call<ResponseBody> signin(@Body UserLogin user);

    @POST("/users/signup")
    Call<ResponseBody> signup(@Body UserRegistration user);

    @GET("/statistics/{username}")
    Call<List<ResponseStatistics>> getStatisticsUsername(@Header("Authorization") String authorization, @Path("username") String username);

    @POST("/statistics/addall")
    Call<Void> addAllStatistics(@Header("Authorization") String authorization, @Body List<UserStatistics> listStatistics);
}