/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2016
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.wca.samples.android;


import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.ibm.wca.samples.android.layout.ResourcesHelper;
import com.ibm.mce.sdk.api.MceApplication;
import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.notification.NotificationsPreference;
import com.ibm.mce.sdk.registration.RegistrationClientImpl;

public class SampleApplication extends MceApplication {

    public static final String MCE_SAMPLE_NOTIFICATION_CHANNEL_ID = "mce_sample_channel";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("VersionTest", "v = "+RegistrationClientImpl.getVersion(getApplicationContext()));

        ResourcesHelper resourcesHelper = new ResourcesHelper(getResources(), getPackageName());

        /**
         * Custom layout
         */

        MceSdk.getNotificationsClient().setCustomNotificationLayout(this,
                resourcesHelper.getString("expandable_layout_type"),
                resourcesHelper.getLayoutId("custom_notification"),
                resourcesHelper.getId("bigText"),
                resourcesHelper.getId("bigImage"), resourcesHelper.getId("action1"),
                resourcesHelper.getId("action2"),
                resourcesHelper.getId("action3"));

        MceSdk.getNotificationsClient().getNotificationsPreference().setSoundEnabled(getApplicationContext(), true);
        MceSdk.getNotificationsClient().getNotificationsPreference().setSound(getApplicationContext(), resourcesHelper.getRawId("notification_sound"));
        MceSdk.getNotificationsClient().getNotificationsPreference().setVibrateEnabled(getApplicationContext(), true);
        long[] vibrate = { 0, 100, 200, 300 };
        MceSdk.getNotificationsClient().getNotificationsPreference().setVibrationPattern(getApplicationContext(), vibrate);
        MceSdk.getNotificationsClient().getNotificationsPreference().setIcon(getApplicationContext(),resourcesHelper.getDrawableId("icon"));
        MceSdk.getNotificationsClient().getNotificationsPreference().setLightsEnabled(getApplicationContext(), true);
        int ledARGB = 0x00a2ff;
        int ledOnMS = 300;
        int ledOffMS = 1000;
        MceSdk.getNotificationsClient().getNotificationsPreference().setLights(getApplicationContext(), new int[]{ledARGB, ledOnMS, ledOffMS});

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(getApplicationContext());
        }
    }

    private static final String PREFS_NAME = "IBM_MCE_SAMPLE";

    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPref(context).edit();
    }

    @TargetApi(26)
    private static void createNotificationChannel(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = notificationManager.getNotificationChannel(MCE_SAMPLE_NOTIFICATION_CHANNEL_ID);
        if(channel == null) {
            CharSequence name = context.getString(R.string.notif_channel_name);
            String description = context.getString(R.string.notif_channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel = new NotificationChannel(MCE_SAMPLE_NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            NotificationsPreference notificationsPreference = MceSdk.getNotificationsClient().getNotificationsPreference();
            notificationsPreference.setNotificationChannelId(context, MCE_SAMPLE_NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
