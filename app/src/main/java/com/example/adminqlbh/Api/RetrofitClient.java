package com.example.adminqlbh.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String baseURL = "http://116.109.64.101:8080/";
    public static final String baseURLImage = "http://116.109.64.101:8080/img/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

}


