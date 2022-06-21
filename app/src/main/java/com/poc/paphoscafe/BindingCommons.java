package com.poc.paphoscafe;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.util.Locale;

public class BindingCommons {

    @BindingAdapter("setAmountText")
    public static void setFormattedAmountTextView(TextView textView, long amount) {
        String formatted = String.format(Locale.getDefault(),
                "%.2f", (amount / 100.0));
        textView.setText(AmountTextWatcher.getThousandFormattedString(formatted, "€ "));
    }

    @BindingAdapter("setCustomTotalAmountText")
    public static void setTotalAmountWithString(TextView textView, long amount) {
        String formatted = String.format(Locale.getDefault(),
                "%.2f", (amount / 100.0));
//        textView.setText("Pay Card " + AmountTextWatcher.getThousandFormattedString(formatted, "€ "));
        textView.setText("Pay Card");
    }

    @BindingAdapter("setTotalAmount")
    public static void setTotalAmountOnly(TextView textView, long amount) {
        String formatted = String.format(Locale.getDefault(),
                "%.2f", (amount / 100.0));
        textView.setText(AmountTextWatcher.getThousandFormattedString(formatted, "€ "));
    }


    @BindingAdapter({"setCustomCurrentText", "setCustomTotalText"})
    public static void setCustomCounterText(TextView textView, int current, int total) {
        textView.setText("Payment " + current + " of " + total);
    }
}
