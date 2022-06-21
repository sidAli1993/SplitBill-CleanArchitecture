package com.poc.paphoscafe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.poc.paphoscafe.databinding.FragmentOwnAmountBinding;
import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.Split;
import com.poc.paphoscafe.ui.viewModels.VMEnterAmount;

public class FragmentEnterAmount extends Fragment {
    private VMEnterAmount viewModel;
    private FragmentOwnAmountBinding dataBinding;
    private String price = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_own_amount, container, false);
        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(VMEnterAmount.class);
        dataBinding.setFragment(this);
    }

    private void setEvents() {
        dataBinding.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(price == null || price.isEmpty()){
                    Toast.makeText(requireContext(), "Enter the amount", Toast.LENGTH_SHORT).show();
                } else {
                    if (getArguments() != null) {
                        if (getArguments().containsKey("data")) {
                            Bill bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                            price = price.replace(Statics.CURRENCY_SYMBOL, "");
                            price = price.replace(".", "");
                            price = price.replace(",", "");
                            price = price.trim();
                            Bundle bundle = new Bundle();
                            if (getArguments().containsKey("is_for_tip")) {
                                if (getArguments().getBoolean("is_for_tip")) {
                                    bill.setTipAmount(Long.parseLong(price));
                                    bundle.putString("data", new Gson().toJson(bill));
                                    if (getArguments().getBoolean("is_for_split")) {
                                        bundle.putString("data_split", getArguments().getString("data_split"));
                                        Statics.controller.navigate(R.id.fragment_each_split, bundle);
                                    }
                                    else {
                                        Statics.controller.navigate(R.id.fragment_bill_split, bundle);
                                    }
                                }else if (getArguments().containsKey("for_both")){
                                    bundle = new Bundle();
                                    Split split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                                    Bill bill2 = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                                    bill2.setTipAmount(Long.parseLong(price));
                                    bill2.setFlag(true);
                                    bill2.setTipPercent(Integer.parseInt(price));
                                    bundle.putString("data",new Gson().toJson(bill2));
                                    bundle.putString("data_split",new Gson().toJson(split));
                                    Statics.controller.navigate(R.id.define_splits,bundle);
                                }
                                else {

                                    bill.setBillAmount(Long.parseLong(price));
                                    bundle.putString("data", new Gson().toJson(bill));
                                    Statics.controller.navigate(R.id.define_splits, bundle);
                                }
                            }
                        } else if (getArguments().containsKey("data_split")) {
                            if (getArguments().getBoolean("is_for_split")) {
                                Bundle bundle = new Bundle();
                                Split split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                                split.setCurrent(split.getCurrent() + 1);
                                split.setPerPersonShare(Long.parseLong(price));
                                bundle.putString("data_split", new Gson().toJson(split));
                                Statics.controller.navigate(R.id.fragment_each_split, bundle);
                            }
                        }
                    }
                }


            }
        });

        dataBinding.buttonBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackSpaceClicked();
            }
        });

        dataBinding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (getArguments() != null) {
                        if (getArguments().containsKey("data")) {
                            price="0.00";
                            Bill bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                            price = price.replace(Statics.CURRENCY_SYMBOL, "");
                            price = price.replace(".", "");
                            price = price.replace(",", "");
                            price = price.trim();
                            Bundle bundle = new Bundle();
                            if (getArguments().containsKey("is_for_tip")) {
                                if (getArguments().getBoolean("is_for_tip")) {

                                    bill.setTipAmount(Long.parseLong(price));
                                    bundle.putString("data", new Gson().toJson(bill));
                                    if (getArguments().getBoolean("is_for_split")) {
                                        bundle.putString("data_split", getArguments().getString("data_split"));
                                        Statics.controller.navigate(R.id.fragment_each_split, bundle);
                                    }
                                    else {
                                        Statics.controller.navigate(R.id.fragment_bill_split, bundle);
                                    }
                                }else if (getArguments().containsKey("for_both")){
                                    bundle = new Bundle();
                                    Split split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                                    Bill bill2 = new Gson().fromJson(getArguments().getString("data"), Bill.class);
                                    bill2.setTipAmount(Long.parseLong(price));
                                    bill2.setFlag(true);
                                    bill2.setTipPercent(Integer.parseInt(price));
                                    bundle.putString("data",new Gson().toJson(bill2));
                                    bundle.putString("data_split",new Gson().toJson(split));
                                    Statics.controller.navigate(R.id.define_splits,bundle);
                                }
                                else {

                                    bill.setBillAmount(Long.parseLong(price));
                                    bundle.putString("data", new Gson().toJson(bill));
                                    Statics.controller.navigate(R.id.define_splits, bundle);
                                }
                            }
                        } else if (getArguments().containsKey("data_split")) {
                            if (getArguments().getBoolean("is_for_split")) {
                                Bundle bundle = new Bundle();
                                Split split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                                split.setCurrent(split.getCurrent() + 1);
                                split.setPerPersonShare(Long.parseLong(price));
                                bundle.putString("data_split", new Gson().toJson(split));
                                Statics.controller.navigate(R.id.fragment_each_split, bundle);
                            }
                        }
                    }
                }
            }
        });

    }


    public void buttonClicked(View view) {
        setValue(Integer.parseInt(view.getTag().toString()));
    }

    private void setValue(int num) {
        if (price.length() < 9) {
            price += String.valueOf(num);
        }
        String formattedAmount = AmountTextWatcher.getThousandFormattedLong(Long.parseLong(price), Statics.CURRENCY_SYMBOL);
        if (formattedAmount.length() <= 11) {
            dataBinding.textViewOutputNumbers.setText(formattedAmount);
        }
    }

    private void onBackSpaceClicked() {
        if (!price.isEmpty()) {
            price = price.substring(0, price.length() - 1);
            if (!price.isEmpty()) {
                price = price.replace(".", "");
                price = price.replace(",", "");
                dataBinding.textViewOutputNumbers.setText(AmountTextWatcher.getThousandFormattedLong(Long.parseLong(price), Statics.CURRENCY_SYMBOL));
            } else {
                dataBinding.textViewOutputNumbers.setText("€ 0.00");
            }
        }
    }
}
