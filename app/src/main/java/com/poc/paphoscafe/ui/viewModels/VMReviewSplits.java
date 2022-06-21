package com.poc.paphoscafe.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poc.paphoscafe.model.Split;

public class VMReviewSplits extends ViewModel {
    private MutableLiveData<Split> model = new MutableLiveData<>();

    public MutableLiveData<Split> getModel() {
        return model;
    }

    public void setModel(Split model) {
        this.model.setValue(model);
    }
}
