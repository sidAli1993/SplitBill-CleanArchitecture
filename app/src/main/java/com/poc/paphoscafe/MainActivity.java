package com.poc.paphoscafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import com.poc.paphoscafe.service.model.DTORemoteTransaction;

import java.util.UUID;

import me.pushy.sdk.Pushy;

public class MainActivity extends AppCompatActivity {
    private boolean intentFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Statics.controller = Navigation.findNavController(this, R.id.myNavHostFragment);

        new PushyAsyncTask().execute(getApplicationContext());
        Pushy.listen(this);
        Pushy.setHeartbeatInterval(10, this);
        parseIntent();
    }


    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (Statics.controller.getCurrentDestination().getId() == R.id.fragment_bill_split ||
                Statics.controller.getCurrentDestination().getId() == R.id.define_splits ||
                Statics.controller.getCurrentDestination().getId() == R.id.fragment_each_split ||
                Statics.controller.getCurrentDestination().getId() == R.id.fragpaywithcash ||
                Statics.controller.getCurrentDestination().getId() == R.id.fragment_review_split) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Cancel Transaction");
            alertDialog.setMessage("are you sure you want to exit the transaction?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    Statics.notificationBody.setValue(null);
                    if (intentFlag) {
                        Intent i = new Intent();
                        i.putExtra("status", 0);
                        i.putExtra("mesasge", "user cancelled the transaction");
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Statics.controller.navigate(R.id.mainfrag);
                    }
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Statics.notificationBody.removeObserver(observer);
        Statics.notificationBody.setValue(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Statics.notificationBody.observe(this, observer);
    }

    private void parseIntent() {
        if (getIntent() != null) {
            if (getIntent().getBundleExtra("request_data") != null) {
                Bundle b = getIntent().getBundleExtra("request_data");
                Bundle bundle = new Bundle();
                intentFlag = true;
                bundle.putString("trans_id", b.getString("trans_id"));
                bundle.putBoolean("should_set_result", true);
                bundle.putString("transaction_value", b.getString("amount"));
                bundle.putString("p_id", UUID.randomUUID().toString());
                Statics.controller.navigate(R.id.fragment_bill_split, bundle);
            }
        }
    }

    Observer<DTORemoteTransaction> observer = new Observer<DTORemoteTransaction>() {
        @Override
        public void onChanged(DTORemoteTransaction dtoRemoteTransaction) {
            if (dtoRemoteTransaction != null) {
                Bundle bundle = new Bundle();
                bundle.putString("transaction_value", dtoRemoteTransaction.getPrice());
                bundle.putString("trans_id", dtoRemoteTransaction.getTransId());
                bundle.putString("p_id", UUID.randomUUID().toString());
                Statics.controller.navigate(R.id.fragment_bill_split, bundle);
            }
        }
    };

    public void reintializeNavController(){
        Statics.controller = Navigation.findNavController(this, R.id.myNavHostFragment);
    }

}