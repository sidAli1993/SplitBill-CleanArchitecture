package com.poc.paphoscafe.retrofit;

import com.poc.paphoscafe.retrofit.model.DTORegister;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WebService {

    @POST("qrRegister")
    Call<Object> qrRegister(
            @Body DTORegister dtoRegister);


    @GET("completetransaction")
    Call<Object> sendTransStatusFromApp(
            @Query("trans_id") String transID,
            @Query("card_no") String cardNumber,
            @Query("status") int status,
            @Query("p_id") String pID,
            @Query("price") String price,
            @Query("isApp") int isApp);


}
