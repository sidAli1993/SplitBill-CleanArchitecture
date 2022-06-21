package com.poc.paphoscafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.poc.paphoscafe.AmountTextWatcher;
import com.poc.paphoscafe.App;
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;
import com.poc.paphoscafe.databinding.FragmentEachSplitBinding;
import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.Environment;
import com.poc.paphoscafe.model.SaleTransactionObject;
import com.poc.paphoscafe.model.Split;
import com.poc.paphoscafe.model.TransactionMode;
import com.poc.paphoscafe.model.TransactionType;
import com.poc.paphoscafe.ui.viewModels.BillViewModel;

import java.util.UUID;

public class FragmentEachSplit extends Fragment {
    private BillViewModel viewModel;
    private FragmentEachSplitBinding dataBinding;
    private Bill bill;
    private Split split;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_each_split, container, false);
        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(BillViewModel.class);
        dataBinding.setModel(viewModel);
        dataBinding.setLifecycleOwner(this);
        bill = new Bill();
        if (getArguments() != null) {
            if (getArguments().containsKey("should_show_prompt")) {
                bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                handleCompleteTransactionAndInformServer();
            } else {
                split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                if (getArguments().containsKey("data")) {
                    bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                } else {
                    bill = new Bill();
                    bill.setBillAmount(split.getPerPersonShare());
                }
                viewModel.setCurrent(split.getCurrent());
                viewModel.setTotal(split.getNumberOfSplits());
                viewModel.setAmount(AmountTextWatcher.getThousandFormattedLong(bill.getBillAmount(), Statics.CURRENCY_SYMBOL));
                viewModel.setBillModel(bill);
            }
        }
        viewModel.setBillModel(bill);
    }


    private void setEvents() {
        dataBinding.tvtip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back_yellow);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                bill.setTipPercent(10);
                viewModel.setBillModel(bill);
            }
        });

        dataBinding.tvtip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back_yellow);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                bill.setTipPercent(15);
                viewModel.setBillModel(bill);
            }
        });

        dataBinding.tvtip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back_yellow);
                bill.setTipPercent(20);
                viewModel.setBillModel(bill);
            }
        });

        dataBinding.tvcustomTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String splitJson = new Gson().toJson(split);
                String billJson = new Gson().toJson(bill);
                Bundle bundle = new Bundle();
                bundle.putString("data", billJson);
                bundle.putString("data_split", splitJson);
                bundle.putBoolean("is_for_tip", true);
                bundle.putBoolean("is_for_split", true);

                Statics.controller.navigate(R.id.fragownamount, bundle);
            }
        });

        dataBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleTransactionObject transactionObject = new SaleTransactionObject();
                transactionObject.setCurrency(Statics.CURRENCY_SYMBOL);
                transactionObject.setEnvironment(Environment.DEVELOPMENT);
                transactionObject.setRequest(TransactionType.SALE);
                transactionObject.setAmount(AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL)
                        .replace(Statics.CURRENCY_SYMBOL, ""));
                Intent i = new Intent();
                i.setAction("com.onopayment.launch_action");
                i.putExtra("FunCall", 1);
                i.putExtra("saleObject", new Gson().toJson(transactionObject));
                cardTransactionLauncher.launch(i);
            }
        });

        dataBinding.btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data_split", new Gson().toJson(split));
                bundle.putString("data", new Gson().toJson(bill));
                bundle.putBoolean("is_split", true);
                Statics.controller.navigate(R.id.fragpaywithcash, bundle);
            }
        });

    }

    private void handleCompleteTransactionAndInformServer() {
        if (bill.getTransactionMode() != null) {
            Toast.makeText(getActivity(), "Transaction Completed!", Toast.LENGTH_SHORT).show();
            // add server call
            updateServer();
            Statics.controller.navigate(R.id.mainfrag);
        }
    }


    private void updateServer() {
        if (bill.getTransactionId() == null || bill.getTransactionId().isEmpty()) {
            Statics.updateServer(UUID.randomUUID().toString(), bill.getCardNumber(), bill.getpId(), bill.getBillAmount());
        } else {
            Statics.updateServer(bill.getTransactionId(), bill.getCardNumber(), bill.getpId(), bill.getBillAmount());
        }
    }

    ActivityResultLauncher<Intent> cardTransactionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        String stringResp = data.getStringExtra("transaction_response");
                        int transactionCode = data.getIntExtra("transactionCode", 2);
                        String maskedPan = data.getStringExtra("maskedPan");
                        switch (transactionCode) {
                            case -1:
                                // Approved
                                bill.setTransactionMode(TransactionMode.CARD);
                                bill.setCardNumber(maskedPan);
                                handleCompleteTransactionAndInformServer();
                                break;
                            case 0:
                                // Cancelled
                                Toast.makeText(getActivity(), "Transaction Cancelled!", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                // Error
                                break;
                        }
                    }
                }
            });
}

