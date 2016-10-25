/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2016
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mce.samples.gcm;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.ibm.mce.samples.gcm.layout.ResourcesHelper;
import com.ibm.mce.sdk.api.MceApplication;
import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.plugin.inapp.ImageTemplate;
import com.ibm.mce.sdk.plugin.inapp.InAppTemplateRegistry;
import com.ibm.mce.sdk.plugin.inapp.VideoTemplate;
import com.ibm.mce.sdk.plugin.inbox.HtmlRichContent;
import com.ibm.mce.sdk.plugin.inbox.PostMessageTemplate;
import com.ibm.mce.sdk.plugin.inbox.RichContentTemplateRegistry;

public class SampleApplication extends MceApplication {

    @Override
    public void onCreate() {
        super.onCreate();

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


        if(SampleGcmBroadcastReceiver.SENDER_ID != null) {
            String registeredSenderId = getSharedPref(getApplicationContext()).getString("senderId", null);
            if(!SampleGcmBroadcastReceiver.SENDER_ID.equals(registeredSenderId)) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                            String regid = gcm.register(SampleGcmBroadcastReceiver.SENDER_ID);
                            Log.i(SampleGcmBroadcastReceiver.TAG, "GCM registration id: " + regid);
                            getEditor(getApplicationContext()).putString("senderId", SampleGcmBroadcastReceiver.SENDER_ID).commit();
                        } catch (Exception e) {
                            Log.e(SampleGcmBroadcastReceiver.TAG, "Failed to register GCM", e);
                        }
                    }
                })).start();
            }
        }
    }

    private static final String PREFS_NAME = "IBM_MCE_SAMPLE";

    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPref(context).edit();
    }
}
