package com.poc.paphoscafe;

import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;

import com.poc.paphoscafe.retrofit.NetworkCall;
import com.poc.paphoscafe.service.model.DTORemoteTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Statics {
    public static NavController controller;
    public static final String CURRENCY_SYMBOL = "€ ";

    public static String DEVICE_PUSHY_TOKEN = "";
    public static MutableLiveData<DTORemoteTransaction> notificationBody = new MutableLiveData<>();

    public static void updateServer(String transactionId,
                                    String cardNumber,
                                    String pId,
                                    long amount) {
        NetworkCall.getInstance().sendTransStatusFromApp(transactionId,
                cardNumber,
                1,
                pId,
                AmountTextWatcher.getThousandFormattedLong(amount, Statics.CURRENCY_SYMBOL).replace(Statics.CURRENCY_SYMBOL, ""),
                0).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
