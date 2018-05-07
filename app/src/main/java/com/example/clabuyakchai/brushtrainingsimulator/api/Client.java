package com.example.clabuyakchai.brushtrainingsimulator.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Clabuyakchai on 04.05.2018.
 */

public class Client {
    private static final String BASE_URL = "http://192.168.5.101:8090";

    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }
}
