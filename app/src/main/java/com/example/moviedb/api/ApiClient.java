package com.example.moviedb.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.moviedb.constant.Constant.BASE_URL;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static ApiClient instance;

    public ApiClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static synchronized ApiClient getInstance(){
        if(instance == null){
            instance = new ApiClient();
        }
        return instance;
    }

    public static ApiService getApiService(){
        return retrofit.create(ApiService.class);
    }
}
