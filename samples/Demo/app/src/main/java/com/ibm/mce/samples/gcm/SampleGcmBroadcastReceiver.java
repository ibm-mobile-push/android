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
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.util.Log;

/**
 * This GCM receiver is added to demonstrate how to receive for non-MCE notifications. If SENDER_ID is set to a Google project id (not the MCE sender id),
 * the application will be registered with GCM also with that project id. When a notification from that sender will arrive, it will be ignored by the MCE
 * GCM broadcast receiver and it will be sent here.
 */
public class SampleGcmBroadcastReceiver extends WakefulBroadcastReceiver {

    static final String TAG = "SampleGCM";

    /**
     * Set another google project id (not the MCE sender id) here if you want the application to receive non-MCE notifications.
     */
    public static final String SENDER_ID = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent == null) {
            return;
        }
        String senderId = intent.getExtras() != null ? intent.getExtras().getString("from") : null;
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        Log.d(TAG, "Sample GCM onHandleEvent: message type: " + messageType+" from "+senderId);



    }
}