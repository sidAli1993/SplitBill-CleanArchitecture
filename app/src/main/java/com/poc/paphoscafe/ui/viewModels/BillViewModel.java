package com.poc.paphoscafe.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poc.paphoscafe.model.Bill;

public class BillViewModel extends ViewModel {
    private MutableLiveData<Bill> billModel = new MutableLiveData<>();
    private MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<Integer> current = new MutableLiveData<>();
    private MutableLiveData<Integer> total = new MutableLiveData<>();

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
