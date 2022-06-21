package com.poc.paphoscafe;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Locale;
import java.util.StringTokenizer;

public class AmountTextWatcher implements TextWatcher {
    private final EditText etAmount;
    private String current = "";
    private final String currencySymbol;

    public AmountTextWatcher(EditText etAmount, String currencySymbol) {
        this.etAmount = etAmount;
        this.currencySymbol = currencySymbol;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!charSequence.toString().equals(current)) {
            etAmount.removeTextChangedListener(this);


            if (etAmount.getText() != null) {
                current = etAmount.getText().toString();
            }

            String amountWithNoDot = "";
            double amount;
            if (current.isEmpty()) {
                amountWithNoDot = current.replace(".", "");
                amountWithNoDot = amountWithNoDot.replace(currencySymbol, "");
                amountWithNoDot = amountWithNoDot.replace(",", "");
                amount = 0;
            } else {
                amountWithNoDot = current.replace(".", "");
                amountWithNoDot = amountWithNoDot.replace(currencySymbol, "");
                amountWithNoDot = amountWithNoDot.replace(",", "");
                amount = Double.parseDouble(amountWithNoDot);
            }

            String formattedAmount = String.format(Locale.getDefault(),
                    "%.2f", amount / 100);
            // Always replace Comma with a DOT
            formattedAmount = formattedAmount.replace(",", ".");

            if (!amountWithNoDot.equals(new String("00"))) {
                etAmount.setText(getThousandFormattedString(formattedAmount, currencySymbol));
            } else {
                String amountText = currencySymbol + "0" + "." + "00";
                etAmount.setText(amountText);
            }
            etAmount.setSelection(etAmount.getText().toString().length());

            etAmount.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static String getThousandFormattedString(String value,
                                                    String currencySymbol) {
        String doubleValue = "";
        if (value != null) {
            try {
                doubleValue = value.toString().replace(',', '.');
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        StringTokenizer lst = new StringTokenizer(doubleValue, ".");
        String str1 = doubleValue;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;

                str3 = str3;

                return currencySymbol + str3;
            }
            if (i == 3) {
                if (str1.charAt(k) != '-') {
                    str3 = "," + str3;
                    i = 0;
                }
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }

    public static String getThousandFormattedLong(long input,
                                                  String currencySymbol) {
        String value = String.format(Locale.ENGLISH,
                "%.2f", (input / 100.0));
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;

                str3 = str3;

                return currencySymbol + str3;
            }
            if (i == 3) {
                if (str1.charAt(k) != '-') {
                    str3 = "," + str3;
                    i = 0;
                }
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }
}