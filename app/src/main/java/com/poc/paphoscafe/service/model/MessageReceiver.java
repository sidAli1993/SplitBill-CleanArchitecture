package com.poc.paphoscafe.service.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.poc.paphoscafe.R;
import com.poc.paphoscafe.Statics;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("PushyTag", "onReceive: " + intent.getExtras().toString());
        NotificationBody notificationBody = new Gson().fromJson(intent.getExtras().getString("notification"), NotificationBody.class);
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            boolean isScreenOn = manager.isInteractive();
            if (!isScreenOn) {
                PowerManager.WakeLock lock = manager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
                lock.acquire(3000);
            }
        }

        try {
            stringToJson(notificationBody.getBody(), context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    private void stringToJson(String string, Context context) throws JSONException {
        JSONObject stringObject = new JSONObject(string);
        if (stringObject.has("type")) {
            if (stringObject.get("type").equals("Cancel")) {
                return;
            }
        }
        int status = stringObject.getInt("status");
        int transId = stringObject.getInt("Trans_id");

        if (status == 1) {
            String price = stringObject.getString("Price");
            DTORemoteTransaction dtoResponse = new DTORemoteTransaction();
            dtoResponse.setPrice(price);
            dtoResponse.setTransId(String.valueOf(transId));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                AudioAttributes builder = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build();
                String channelId = context.getPackageName() + "_notification_id";
                String channelName = context.getPackageName() + "_notification_name";
                Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
                NotificationChannel mChannel = notificationManager.getNotificationChannel(channelId);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    mChannel.setSound(defaultRingtoneUri, builder);
                    notificationManager.createNotificationChannel(mChannel);
                }
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(context.getString(R.string.payment_request))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setAutoCancel(true)
                        .setOngoing(false);
                Notification notification = notificationBuilder.build();
                notificationManager.notify(100, notification);
            }

            Statics.notificationBody.postValue(dtoResponse);
        }
    }
}
