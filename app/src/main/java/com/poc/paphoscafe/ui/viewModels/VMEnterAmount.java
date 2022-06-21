package com.poc.paphoscafe.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VMEnterAmount extends ViewModel {
    private MutableLiveData<String> amount = new MutableLiveData<>();

    public MutableLiveData<String> getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.setValue(amount);
    }
}
