package com.poc.paphoscafe.retrofit;


import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkCall {
    private static WebService instance;

    public static WebService getInstance() {
        if (instance == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://deazitech.com:448/public/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory
                            .create(new GsonBuilder().serializeNulls().create()))
                    .build();
            instance = retrofit.create(WebService.class);
        }
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

}
