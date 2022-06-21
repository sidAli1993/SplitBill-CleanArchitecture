package com.poc.paphoscafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.ono.intenthandler.MiddlewareActivity;
import com.ono.intenthandler.enums.Environment;
import com.ono.intenthandler.enums.TransactionType;
import com.ono.intenthandler.listeners.TransactionListener;
import com.ono.intenthandler.models.TransactionObject;
import com.ono.intenthandler.models.TransactionObjectResponse;
import com.ono.intenthandler.models.initializesession.SessionResponseObject;
import com.ono.intenthandler.models.searchtransaction.SearchTransactionResultObject;
import com.poc.paphoscafe.AmountTextWatcher;
import com.poc.paphoscafe.App;
import com.poc.paphoscafe.MainActivity;
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;
import com.poc.paphoscafe.databinding.FragmentBillSplitBinding;
import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.SaleTransactionObject;
import com.poc.paphoscafe.model.TransactionMode;
import com.poc.paphoscafe.ui.viewModels.BillViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class BillSplitFragment extends Fragment {
    private BillViewModel viewModel;
    private FragmentBillSplitBinding dataBinding;
    private Bill bill;
    private boolean wasCardPayment = false;
    private int responseStatus = 0;
    private Boolean isComingNewScreen = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bill_split, container, false);
        init();
        setListeners();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(BillViewModel.class);
        dataBinding.setModel(viewModel);
        dataBinding.setLifecycleOwner(this);
        bill = new Bill();
        if (getArguments() != null) {
            if (getArguments().containsKey("transaction_value")) {
                String transactionValue = getArguments().getString("transaction_value");
                if (transactionValue != null) {
                    if (transactionValue.contains(",")) {
                        transactionValue = transactionValue.replace(",", "");
                    } else if (transactionValue.contains(".")) {
                        transactionValue = transactionValue.replace(".", "");
                    }
                    viewModel.setAmount(AmountTextWatcher.getThousandFormattedLong(Long.parseLong(transactionValue), Statics.CURRENCY_SYMBOL));
                }

                bill.setpId(getArguments().getString("p_id"));
                if (getArguments().containsKey("trans_id")) {
                    bill.setTransactionId(getArguments().getString("trans_id"));
                }

                isComingNewScreen = false;
            } else if (getArguments().containsKey("should_show_prompt")) {
                bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                handleCompleteTransactionAndInformServer();
                isComingNewScreen = false;
            } else {
                bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                viewModel.setAmount(AmountTextWatcher.getThousandFormattedLong(bill.getBillAmount(), Statics.CURRENCY_SYMBOL));
                viewModel.setBillModel(bill);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back_yellow);
                isComingNewScreen = true;
            }
        }
        viewModel.setBillModel(bill);

    }

    private void setListeners() {
        dataBinding.totalBillET.addTextChangedListener(new AmountTextWatcher(dataBinding.totalBillET, Statics.CURRENCY_SYMBOL));
        viewModel.getAmount().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null && (!s.isEmpty()) && !isComingNewScreen) {
                    bill.setBillAmount(Long.parseLong(s.replace(Statics.CURRENCY_SYMBOL, "").replace(",", "").replace(".", "")));
                    viewModel.setBillModel(bill);
                }
            }
        });
    }

    private void setEvents() {
        dataBinding.tvtip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back_yellow);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                bill.setFlag(false);
                bill.setTipPercent(10);
                viewModel.setBillModel(bill);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);

            }
        });

        dataBinding.tvtip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back_yellow);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                bill.setFlag(false);
                bill.setTipPercent(15);
                viewModel.setBillModel(bill);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);
            }
        });

        dataBinding.tvtip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back_yellow);
                bill.setFlag(false);
                bill.setTipPercent(20);
                viewModel.setBillModel(bill);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);
            }
        });

        dataBinding.tvcustomTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back_yellow);
                String billJson = new Gson().toJson(bill);
                Bundle bundle = new Bundle();
                bundle.putString("data", billJson);
                bundle.putBoolean("is_for_tip", true);
                bundle.putBoolean("is_for_split", false);
                Statics.controller.navigate(R.id.fragownamount, bundle);

            }
        });

        dataBinding.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SaleTransactionObject transactionObject = new SaleTransactionObject();
//                transactionObject.setCurrency(Statics.CURRENCY_SYMBOL);
//                transactionObject.setEnvironment(Environment.DEVELOPMENT);
//                transactionObject.setRequest(TransactionType.SALE);
//                transactionObject.setAmount(AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL)
//                        .replace(Statics.CURRENCY_SYMBOL, ""));
//                Intent i = new Intent();
//                i.setAction("com.onopayment.launch_action");
//                i.putExtra("FunCall", 1);
//                i.putExtra("saleObject", new Gson().toJson(transactionObject));
//                cardTransactionLauncher.launch(i);


                String amount = AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL)
                        .replace(Statics.CURRENCY_SYMBOL, "");
                TransactionObject saleObject = new TransactionObject(amount, "R", TransactionType.SALE, Environment.DEVELOPMENT);
                TransactionListener transactionListener = new TransactionListener() {
                    @Override
                    public void transactionStarted() {

                    }

                    @Override
                    public void transactionCompleted(@NotNull TransactionObjectResponse transactionObjectResponse) {

                        if (transactionObjectResponse.isApproved()) {
                            bill.setTransactionMode(TransactionMode.CARD);
                            bill.setCardNumber(transactionObjectResponse.getCardNumber());
                            responseStatus = 1;
                            handleCompleteTransactionAndInformServer();

                        } else {
                            responseStatus = 0;
                            Toast.makeText(getActivity(), "Transaction Cancelled!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void sessionInitialized(@NotNull SessionResponseObject sessionResponseObject) {

                    }

                    @Override
                    public void searchTransactionCompleted(@org.jetbrains.annotations.Nullable SearchTransactionResultObject searchTransactionResultObject) {

                    }

                    @Override
                    public void lastTransactionCompleted(@org.jetbrains.annotations.Nullable SearchTransactionResultObject searchTransactionResultObject) {

                    }

                    @Override
                    public void internetNotConnected() {

                    }

                    @Override
                    public void errorOccured(@NotNull String s) {

                        responseStatus = -1;
                    }

                    @Override
                    public void rkiInitialized() {

                    }
                };
                MiddlewareActivity.Companion.startTransaction(getContext(), saleObject, transactionListener, false);


            }
        });

        dataBinding.tvPaywithCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", new Gson().toJson(bill));
                bundle.putBoolean("is_split", false);

                if (getArguments() != null) {
                    if (getArguments().containsKey("should_set_result")) {
                        bundle.putBoolean("should_set_result", true);
                    }
                }
                Statics.controller.navigate(R.id.fragpaywithcash, bundle);
            }
        });

        dataBinding.btnsplitbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("data", new Gson().toJson(bill));

                Statics.controller.navigate(R.id.define_splits, bundle);
            }
        });
    }

    private void handleCompleteTransactionAndInformServer() {
        if (bill.getTransactionMode() != null) {
            Toast.makeText(getActivity(), "Transaction Completed!", Toast.LENGTH_SHORT).show();
            // add server call
            updateServer();

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    Statics.controller.navigate(R.id.mainfrag);
                }
            }, 500);

        }
    }


    private void updateServer() {
        if (bill.getTransactionId() == null || bill.getTransactionId().isEmpty()) {
            Statics.updateServer(UUID.randomUUID().toString(), bill.getCardNumber(), bill.getpId(), bill.getBillAmount());
        } else {
            Statics.updateServer(bill.getTransactionId(), bill.getCardNumber(), bill.getpId(), bill.getBillAmount());
        }

        if (getArguments() != null) {
            if (getArguments().containsKey("should_set_result")) {
                if (getArguments().getBoolean("should_set_result")) {
                    Intent i = new Intent();

                    if (getArguments().containsKey("status")) {
                        i.putExtra("status", getArguments().getInt("status"));
                        responseStatus = getArguments().getInt("status");
                    } else {
                        i.putExtra("status", responseStatus);
                    }

                    if (responseStatus == 0) {
                        i.putExtra("mesasge", "User cancelled the transaction");
                    } else if (responseStatus == 1) {
                        i.putExtra("mesasge", "Transaction Completed");
                    } else {
                        i.putExtra("mesasge", "Error Occured");
                    }

                    getActivity().setResult(RESULT_OK, i);
                    getActivity().finish();
                }
            }
        }

    }

    ActivityResultLauncher<Intent> cardTransactionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
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
