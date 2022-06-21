package com.poc.paphoscafe;

import android.content.Context;
import android.os.AsyncTask;

import com.poc.paphoscafe.room.AppDatabase;
import com.poc.paphoscafe.room.model.UserConfig;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class PushyAsyncTask extends AsyncTask<Context, Void, String> {
    @Override
    protected String doInBackground(Context... contexts) {
        if (!Pushy.isRegistered(contexts[0])) {
            try {
                String deviceToken = Pushy.register(contexts[0]);
                Statics.DEVICE_PUSHY_TOKEN = deviceToken;
                UserConfig config = new UserConfig();
                config.setPushyToken(deviceToken);
                AppDatabase.getDbInstance(contexts[0]).userConfigDao().insert(config);
            } catch (PushyException e) {
                e.printStackTrace();
            }
        } else {
            Statics.DEVICE_PUSHY_TOKEN = AppDatabase.getDbInstance(contexts[0]).userConfigDao().getUserToken();
        }
        return Statics.DEVICE_PUSHY_TOKEN;
    }

    @Override
    protected void onPostExecute(String string) {
        super.onPostExecute(string);

    }
}
