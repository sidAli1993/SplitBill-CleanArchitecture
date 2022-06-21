package com.poc.paphoscafe.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;
import com.poc.paphoscafe.databinding.FragmentDefineSplitsBinding;
import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.SaleTransactionObject;
import com.poc.paphoscafe.model.Split;
import com.poc.paphoscafe.model.TransactionMode;
import com.poc.paphoscafe.retrofit.NetworkCall;
import com.poc.paphoscafe.ui.viewModels.BillViewModel;
import com.poc.paphoscafe.ui.viewModels.VMDefineSplits;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDefineSplits extends Fragment {
    private FragmentDefineSplitsBinding dataBinding;
    private VMDefineSplits viewModel;
    private Split split;
    private boolean flag = false;
    private Bill bill;
    private boolean isCustomTip = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_define_splits, container, false);
        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(VMDefineSplits.class);


        viewModel.setCurrent(1);
        viewModel.setTotal(1);


        if (getArguments() != null) {
            if (getArguments().containsKey("data_split")) {
                bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                flag = true;
                viewModel.setCurrent(split.getCurrent());
                viewModel.setTotal(split.getNumberOfSplits());
                if (split.getCurrent()>1){
                    hideAndVisibleView();
                }
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back_yellow);
//
            } else if (getArguments().containsKey("data")) {
                bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                if (split == null) {
                    split = new Split();
                }
                flag = false;
                split.setTotalAmount(bill.getBillAmount());
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);
            }
        }

        viewModel.setModel(split);
        dataBinding.setModel(viewModel);

        dataBinding.setFragment(this);
        dataBinding.setLifecycleOwner(this);
        if (bill == null) {
            bill = new Bill();
            bill.setBillAmount(split.getPerPersonShare());
        }


        viewModel.setBillModel(bill);

        if(split.getCurrent() <= 1){
            dataBinding.btnOwnAmount.setEnabled(false);
        }else{
            dataBinding.btnOwnAmount.setEnabled(true);
        }
    }

    private void setEvents() {
        dataBinding.etTotalPerson.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    int number = Integer.parseInt(dataBinding.etTotalPerson.getText().toString());
                    if (number > 62555) {
                        Toast.makeText(getActivity(), "Please enter a lower number", Toast.LENGTH_SHORT).show();
                        split.setNumberOfSplits(1);

                    } else {
                        split.setNumberOfSplits(number);
                    }

                    viewModel.setModel(split);
                }
                return false;
            }
        });
        dataBinding.tvtip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back_yellow);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip4.setBackgroundResource(R.drawable.btn_back);
                bill.setFlag(false);
                flag=false;
                bill.setTipPercent(10);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);
                viewModel.setBillModel(bill);
            }
        });

        dataBinding.tvtip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back_yellow);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip4.setBackgroundResource(R.drawable.btn_back);
                bill.setFlag(false);
                bill.setTipPercent(15);
                flag=false;
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
                dataBinding.tvtip4.setBackgroundResource(R.drawable.btn_back);
                bill.setFlag(false);
                flag=false;
                bill.setTipPercent(20);
                viewModel.setBillModel(bill);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);
            }
        });
        dataBinding.tvtip4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBinding.tvtip1.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip2.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip3.setBackgroundResource(R.drawable.btn_back);
                dataBinding.tvtip4.setBackgroundResource(R.drawable.btn_back_yellow);
                bill.setFlag(false);
                flag=false;
                bill.setTipPercent(5);
                viewModel.setBillModel(bill);
                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back);
            }
        });


        dataBinding.tvcustomTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataBinding.tvcustomTip.setBackgroundResource(R.drawable.btn_back_yellow);
                String splitJson = new Gson().toJson(split);
                String billJson = new Gson().toJson(bill);
                Bundle bundle = new Bundle();
                bundle.putString("data", billJson);
                bundle.putString("data_split", splitJson);
                bundle.putBoolean("is_for_tip", false);
                bundle.putBoolean("is_for_split", false);
                bundle.putBoolean("for_both", true);
                Statics.controller.navigate(R.id.fragownamount, bundle);

            }
        });

        dataBinding.btnOwnAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String splitJson = new Gson().toJson(split);
//                String billJson = new Gson().toJson(bill);
//                Bundle bundle = new Bundle();
//                bundle.putString("data", billJson);
//                bundle.putString("data_split", splitJson);
//                Statics.controller.navigate(R.id.fragownnewamount, bundle);

                Toast.makeText(requireContext(), "Work In Progress", Toast.LENGTH_SHORT).show();
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

                String amount  = AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL)
                        .replace(Statics.CURRENCY_SYMBOL, "");
                TransactionObject saleObject = new TransactionObject(amount, "R", TransactionType.SALE, Environment.DEVELOPMENT);
                TransactionListener transactionListener = new TransactionListener() {
                    @Override
                    public void transactionStarted() {

                    }

                    @Override
                    public void transactionCompleted(@NotNull TransactionObjectResponse transactionObjectResponse) {

                        ((Activity) getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if(transactionObjectResponse.isApproved()){
                                    hideAndVisibleView();
                                    split.setCurrent(split.getCurrent()+1);
                                    viewModel.setCurrent(split.getCurrent());
                                    bill.setTransactionMode(TransactionMode.CARD);
                                    // navigate to other
                                    bill.setCardNumber(transactionObjectResponse.getCardNumber());
                                    split.addNewProcessedPayment(bill);
                                    updateServer();
                                    if (split.getCurrent()>1){
                                        hideAndVisibleView();
                                    }

                                    viewModel.setModel(split);
                                    dataBinding.setModel(viewModel);

                                    if (split.getPaidAmount() >= split.getTotalAmount()) {
                                        Toast.makeText(getActivity(), "Transaction successful!", Toast.LENGTH_SHORT).show();
                                        final Handler handler = new Handler(Looper.getMainLooper());
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Do something after 100ms
                                                Statics.controller.navigate(R.id.mainfrag);
                                            }
                                        }, 500);

                                    }





                                }else{
                                    Toast.makeText(getActivity(), "Transaction Cancelled!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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

                    }

                    @Override
                    public void rkiInitialized() {

                    }
                };
                MiddlewareActivity.Companion.startTransaction(getContext(), saleObject, transactionListener,false);


            }
        });
//
        dataBinding.tvPaywithCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // save and move forward
//                split.setPid(bill.getpId());
//                split.setTransactionId(bill.getTransactionId());
//                Bundle bundle = new Bundle();
//                bundle.putString("data_split", new Gson().toJson(split));
//                Statics.controller.navigate(R.id.fragment_each_split, bundle);
                Bundle bundle = new Bundle();
                bundle.putString("data_split", new Gson().toJson(split));
                bundle.putString("data", new Gson().toJson(bill));
                bundle.putBoolean("is_split", true);
                Statics.controller.navigate(R.id.fragpaywithcash, bundle);
            }
        });
    }

    public void imageClicked(View view) {
        int number = split.getNumberOfSplits();
        if (view.getTag().equals("1")) {
            ++number;
        } else if (view.getTag().equals("2")) {
            if (number > 1)
                --number;
        }

        if(number <= 1){
            dataBinding.btnOwnAmount.setEnabled(false);
        }else{
            dataBinding.btnOwnAmount.setEnabled(true);
        }

//        split.setNumberOfSplits(number);
//        bill.setBillAmount(split.getPerPersonShare());
//        viewModel.setModel(split);flag=false;
//        viewModel.setBillModel(bill);
        split.setNumberOfSplits(number);
        viewModel.setTotal(split.getNumberOfSplits());

        viewModel.setModel(split);
        if (flag) {
            long tamount = bill.getTipPercent();
            long ppTip = tamount / number;
            bill.setBillAmount(split.getPerPersonShare());
            bill.setTipAmount(ppTip);
            bill.setFlag(true);
            viewModel.setBillModel(bill);
        }else {
            bill.setBillAmount(split.getPerPersonShare());
            bill.setFlag(false);
            viewModel.setBillModel(bill);
        }
        setBtnClickable(number);

    }


    private void updateServer() {
        if (bill.getTransactionId() == null) {
            NetworkCall.getInstance().sendTransStatusFromApp(UUID.randomUUID().toString(),
                    bill.getCardNumber() == null ? "" : bill.getCardNumber(),
                    1,
                    bill.getpId(),
                    AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL)
                            .replace(Statics.CURRENCY_SYMBOL, ""),
                    0).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        } else {
            NetworkCall.getInstance().sendTransStatusFromApp(bill.getTransactionId(),
                    bill.getCardNumber() == null ? "" : bill.getCardNumber(),
                    1,
                    bill.getpId(),
                    AmountTextWatcher.getThousandFormattedLong(bill.getTotalAmount(), Statics.CURRENCY_SYMBOL)
                            .replace(Statics.CURRENCY_SYMBOL, ""),
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
                                hideAndVisibleView();
                                split.setCurrent(split.getCurrent()+1);
                                bill.setTransactionMode(TransactionMode.CARD);
                                // navigate to other
                                split.addNewProcessedPayment(bill);
                                updateServer();
                                if (split.getCurrent()>1){
                                    hideAndVisibleView();
                                }

                                viewModel.setModel(split);
                                dataBinding.setModel(viewModel);

                                if (split.getPaidAmount() >= split.getTotalAmount()) {
                                    Toast.makeText(getActivity(), "Transaction successful!", Toast.LENGTH_SHORT).show();
                                    Statics.controller.navigate(R.id.mainfrag);
                                }
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

    private void handleCompleteTransactionAndInformServer() {
        if (bill.getTransactionMode() != null) {
            Toast.makeText(getActivity(), "Transaction Completed!", Toast.LENGTH_SHORT).show();
            // add server call
            updateServer();
            Statics.controller.navigate(R.id.mainfrag);
        }
    }

    public void hideAndVisibleView() {
        if (split.getPaidAmount() >= 0) {
            dataBinding.stillOwingTV.setVisibility(View.VISIBLE);
            dataBinding.tvTotalStillBillAmount.setVisibility(View.VISIBLE);
            dataBinding.tvPeoplePaying.setVisibility(View.INVISIBLE);
            dataBinding.relativeLayout2.setVisibility(View.INVISIBLE);
            dataBinding.tvTotalStillBillAmount.setText(split.getTotalAmount() - split.getPaidAmount() + "");
        }
    }

    public void setBtnClickable(int number){

    }
}
