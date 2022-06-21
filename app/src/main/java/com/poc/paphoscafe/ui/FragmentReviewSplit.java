package com.poc.paphoscafe.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.poc.paphoscafe.App;
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;
import com.poc.paphoscafe.databinding.FragmentReviewSplitBinding;
import com.poc.paphoscafe.model.Split;
import com.poc.paphoscafe.ui.viewModels.VMReviewSplits;

public class FragmentReviewSplit extends Fragment {
    private FragmentReviewSplitBinding dataBinding;
    private VMReviewSplits viewModel;
    private Split split;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_split, container, false);
        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(App.getInstance()).create(VMReviewSplits.class);
        if (getArguments() != null) {
            if (getArguments().containsKey("data_split")) {
                split = new Gson().fromJson(getArguments().getString("data_split"), Split.class);
                split.setNumberOfSplits(Math.max((split.getNumberOfSplits() - 1), 0));
            }
        }
        viewModel.setModel(split);
        dataBinding.setFragment(this);
        dataBinding.setModel(viewModel);
        dataBinding.setLifecycleOwner(this);
    }

    private void setEvents() {
        dataBinding.btnNextDefinedSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int number = split.getCurrent();
                ++number;
                split.setCurrent(number);
                bundle.putString("data_split", new Gson().toJson(split));
                Statics.controller.navigate(R.id.fragment_each_split, bundle);
            }
        });

        dataBinding.tvCustomAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String splitJson = new Gson().toJson(split);
                Bundle bundle = new Bundle();
                bundle.putString("data_split", splitJson);
                bundle.putBoolean("is_for_tip", false);
                bundle.putBoolean("is_for_split", true);
                Statics.controller.navigate(R.id.fragownamount, bundle);
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
        split.setNumberOfSplits(number);
        viewModel.setModel(split);
    }

}
