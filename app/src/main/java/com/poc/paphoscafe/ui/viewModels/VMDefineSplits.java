package com.poc.paphoscafe.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.Split;

public class VMDefineSplits extends ViewModel {
    private MutableLiveData<Split> model = new MutableLiveData<>();
    private MutableLiveData<Bill> billModel = new MutableLiveData<>();
    private MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<Integer> current = new MutableLiveData<>();
    private MutableLiveData<Integer> total = new MutableLiveData<>();


    public MutableLiveData<Split> getModel() {
        return model;
    }

    public void setModel(Split model) {
        this.model.setValue(model);
    }

    public MutableLiveData<Bill> getBillModel() {
        return billModel;
    }

    public void setBillModel(Bill billModel) {
        this.billModel.setValue(billModel);
    }

    public MutableLiveData<String> getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.setValue(amount);
    }

    public MutableLiveData<Integer> getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current.setValue(current);
    }

    public MutableLiveData<Integer> getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total.setValue(total);
    }
}
