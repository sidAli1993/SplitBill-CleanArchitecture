package com.poc.paphoscafe.ui;

import android.os.Bundle;
import android.util.Log;
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
import com.poc.paphoscafe.databinding.FragmentOwnNewAmountBinding;
import com.poc.paphoscafe.model.Bill;
import com.poc.paphoscafe.model.Split;
import com.poc.paphoscafe.ui.viewModels.VMOwnAmount;

public class FragmentOwnAmount extends Fragment {
    private VMOwnAmount viewModel;
    private FragmentOwnNewAmountBinding dataBinding;
    private String price = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_own_new_amount, container, false);
        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(VMOwnAmount.class);
        dataBinding.setFragment(this);
        //dataBinding.buttonDoneNewwlllllll.setEnabled(true);
        //dataBinding.buttonDoneNewwlllllll.setClickable(true);
        Log.e("MEHROZE", "THIS IS ME....I was here");



    }

    private void setEvents() {
        dataBinding.buttonDoneNewwlllllll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("MEHROZE", "TRYYYYYYYYYYYYYYYYY");
                onDoneClick();
            }
        });

        dataBinding.buttonBackSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackSpaceClicked(v);
            }
        });

        dataBinding.buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
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

    private void onBackSpaceClicked(View view) {
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

    public void onDoneClick() {
        Bill bill = new Gson().fromJson(getArguments().getString("data"), Bill.class);
        Split split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
        if (price == null || price.isEmpty()) {
            Toast.makeText(requireContext(), "Enter the amount", Toast.LENGTH_SHORT).show();
        }else {
            price = price.replace(Statics.CURRENCY_SYMBOL, "");
            price = price.replace(".", "");
            price = price.replace(",", "");
            price = price.trim();


            split.setTotalAmount(split.getTotalAmount() - Long.parseLong(price));
            Bundle bundle = new Bundle();
            bundle.putString("data", new Gson().toJson(bill));
            bundle.putString("data_split", new Gson().toJson(split));
            Statics.controller.navigate(R.id.define_splits, bundle);

        }


    }
}
