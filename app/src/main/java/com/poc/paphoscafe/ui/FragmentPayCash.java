package com.poc.paphoscafe.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.poc.paphoscafe.AmountTextWatcher;
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;
import com.poc.paphoscafe.databinding.FragmentPayWithCashBinding;
import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.Split;
import com.poc.paphoscafe.model.TransactionMode;

import java.util.UUID;

public class FragmentPayCash extends Fragment {
    private FragmentPayWithCashBinding dataBinding;
    private Bill bill;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_with_cash, container, false);
        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        if (getArguments() != null) {
            bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
            dataBinding.tvPayableAmount.setText(AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL));
            dataBinding.tvcashperhead.setText(AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL));
        }
    }

    private void setEvents() {
        dataBinding.tvcashperhead.addTextChangedListener(new AmountTextWatcher(dataBinding.tvcashperhead, Statics.CURRENCY_SYMBOL));
        dataBinding.tvcashperhead.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (getEnteredAmount() < bill.getTotalAmount()) {
                        dataBinding.tvchange.setText(AmountTextWatcher.getThousandFormattedLong(0L, Statics.CURRENCY_SYMBOL));
                        Toast.makeText(getActivity(), "Cash paid must either be equal or greater", Toast.LENGTH_SHORT).show();
                    } else if (getEnteredAmount() > bill.getTotalAmount()) {
                        long change = getEnteredAmount() - bill.getTotalAmount();
                        if (change < 0) {
                            change = change * -1;
                        }
                        dataBinding.tvchange.setText(AmountTextWatcher.getThousandFormattedLong(change, Statics.CURRENCY_SYMBOL));
                    }
                    return false;
                }
                return false;
            }
        });
        dataBinding.btnPaywithcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getEnteredAmount() >= bill.getTotalAmount()) {
                    bill.setTransactionMode(TransactionMode.CASH);
                    Bundle bundle = new Bundle();
                    bundle.putString("data", new Gson().toJson(bill));
                    bundle.putBoolean("should_show_prompt", true);
                    if (getArguments() != null) {
                        if (getArguments().containsKey("is_split")) {
                            if (getArguments().getBoolean("is_split")) {
                                // navigate to other
                                Split split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                                split.addNewProcessedPayment(bill);
                                split.setCurrent(split.getCurrent()+1);
                                updateServer();
                                bundle.putString("data_split", new Gson().toJson(split));
                                if (split.getPaidAmount() >= split.getTotalAmount()) {
                                    Toast.makeText(getActivity(), "Transaction successful!", Toast.LENGTH_SHORT).show();
                                    Statics.controller.navigate(R.id.mainfrag);
                                } else {
                                    Statics.controller.navigate(R.id.define_splits, bundle);
                                }
                            } else {
                                if (getArguments() != null) {
                                    if (getArguments().containsKey("should_set_result")) {
                                        bundle.putBoolean("should_set_result", true);
                                        bundle.putInt("status", 1);
                                    }
                                }
                                Statics.controller.navigate(R.id.fragment_bill_split, bundle);
                            }
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Cash paid must either be equal or greater", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateServer() {
        if (bill.getTransactionId() == null || bill.getTransactionId().isEmpty()) {
            Statics.updateServer(UUID.randomUUID().toString(), bill.getCardNumber(), bill.getpId(), bill.getBillAmount());
        } else {
            Statics.updateServer(bill.getTransactionId(), bill.getCardNumber(), bill.getpId(), bill.getBillAmount());
        }
    }

    private long getEnteredAmount() {
        String amount = dataBinding.tvcashperhead.getText().toString();
        amount = amount.replace(Statics.CURRENCY_SYMBOL, "");
        amount = amount.replace(".", "");
        amount = amount.replace(",", "");
        amount = amount.trim();
        return Long.parseLong(amount);
    }
}
