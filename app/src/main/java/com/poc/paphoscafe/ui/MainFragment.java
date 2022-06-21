package com.poc.paphoscafe.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.poc.paphoscafe.AmountTextWatcher;
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;
import com.poc.paphoscafe.databinding.FragmentMainBinding;
import com.poc.paphoscafe.retrofit.NetworkCall;
import com.poc.paphoscafe.retrofit.model.DTORegister;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment implements MultiplePermissionsListener {
    public static final String TAG = "MainFragment";
    private FragmentMainBinding dataBinding;
    private IntentIntegrator scanIntegrator;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main,
                container,
                false);

        init();
        setEvents();
        return dataBinding.getRoot();
    }

    private void init() {
        dataBinding.customAmountET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                   dataBinding.customAmountET.clearFocus();

                }
                return false;
            }
        });
    }

    private void setEvents() {
        dataBinding.customAmountET.addTextChangedListener(new AmountTextWatcher(dataBinding.customAmountET,Statics.CURRENCY_SYMBOL));
        dataBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                String input = dataBinding.tableNoET.getText().toString();
                if (input.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter table number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (input.equals("1")) {
                    bundle.putString("transaction_value", "15000");
                } else if (input.equals("2")) {
                    bundle.putString("transaction_value", "10000");
                }else{
                    bundle.putString("transaction_value", "5000");
                }

                bundle.putString("p_id", UUID.randomUUID().toString());

                Statics.controller.navigate(R.id.fragment_bill_split, bundle);
            }
        });

        dataBinding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSettingsClicked();
            }
        });

        dataBinding.buttonPayCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dataBinding.customAmountET.getText().toString().isEmpty()){
                    Bundle bundle = new Bundle();
                    bundle.putString("p_id", UUID.randomUUID().toString());

                    String price =  dataBinding.customAmountET.getText().toString();

                    price = price.replace(Statics.CURRENCY_SYMBOL, "");
                    price = price.replace(".", "");
                    price = price.replace(",", "");
                    price = price.trim();


                    bundle.putString("transaction_value",price);
                    Statics.controller.navigate(R.id.fragment_bill_split, bundle);
                }
            }
        });
    }


    private void onSettingsClicked() {
        Dexter.withContext(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                )
                .withListener(this)
                .check();
    }

    private void launchScanner() {
        scanIntegrator = IntentIntegrator.forSupportFragment(MainFragment.this);
        scanIntegrator.setPrompt("Scan");
        scanIntegrator.setBeepEnabled(true);
        //The following line if you want QR code
        scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        scanIntegrator.setOrientationLocked(true);
        scanIntegrator.setBarcodeImageEnabled(true);
        activityResultLauncher.launch(scanIntegrator.createScanIntent());
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        IntentResult scanningResult = IntentIntegrator.parseActivityResult(49374, result.getResultCode(), data);
                        if (scanningResult != null) {
                            if (scanningResult.getContents() != null) {
                                String scanContent = scanningResult.getContents();
                                showDialog(getActivity(), scanContent);
                            }
                        }
                    }
                }
            });

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        launchScanner();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

    }

    private void showDialog(Context context, String scanId) {
        Dialog dialog = new Dialog(context);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * (90 / 100f));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_registerdevice);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(screenWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        Button btnYes = dialog.findViewById(R.id.btn_register);
        EditText etDeviceName = dialog.findViewById(R.id.et_device_name);
        TextInputLayout tilDeviceName = dialog.findViewById(R.id.til_device_name);
        if (!dialog.isShowing()) {
            dialog.show();
            dialog.setCancelable(false);
        }
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDeviceName.getText().toString().isEmpty()) {
                    tilDeviceName.setError("Required");
                } else {
                    registerQr(prepareRequest(Statics.DEVICE_PUSHY_TOKEN, scanId, etDeviceName.getText().toString()));
                    dialog.dismiss();
                }
            }
        });
    }

    private DTORegister prepareRequest(String deviceToken, String scanId, String etDeviceName) {
        DTORegister obj = new DTORegister();
        obj.setFcmToken(deviceToken);
        obj.setScanId(scanId);
        obj.setStatus(1);
        obj.setDeviceId(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID) + getActivity().getPackageName());
        obj.setDeviceTitle(etDeviceName.trim());
        return obj;
    }

    private void registerQr(DTORegister dtoRegister) {
        NetworkCall.getInstance().qrRegister(dtoRegister).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response != null && response.isSuccessful()) {

                } else {
                    Toast.makeText(getActivity(), "Already Registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
}
