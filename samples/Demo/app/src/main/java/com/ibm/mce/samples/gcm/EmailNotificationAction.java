/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2015
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mce.samples.gcm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.ibm.mce.sdk.api.notification.Action;
import com.ibm.mce.sdk.api.notification.MceNotificationAction;
import com.ibm.mce.sdk.api.notification.NotificationDetails;
import com.ibm.mce.sdk.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EmailNotificationAction implements MceNotificationAction {

    public static final String TAG = "EmailNotificationAction";

    public static final String EMAIL_ADDRESS_KEY = "recipient";
    public static final String EMAIL_SUBJECT_KEY = "subject";
    public static final String EMAIL_BODY_KEY = "body";


    @Override
    public void handleAction(Context context, String type, String name, String attribution, Map<String, String> payload, boolean fromNotification) {
        String valueJSONStr = payload.get(Action.KEY_VALUE);

        if(valueJSONStr != null && !(valueJSONStr.trim().length()==0)) {
            try {
                JSONObject valueJSON = new JSONObject(valueJSONStr);
                String emailAddress = valueJSON.getString(EMAIL_ADDRESS_KEY);
                String subject = valueJSON.getString(EMAIL_SUBJECT_KEY);
                String body = valueJSON.getString(EMAIL_BODY_KEY);
                String address = emailAddress + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", address, null));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                intent.setData(Uri.parse("mailto:" + address));
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                try {

                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Logger.e(TAG, "No Email activity found:" + e.getMessage(), e);
                }
            } catch (JSONException jsone) {
                Logger.e(TAG, "Failed to parse JSON email message", jsone);
            }
        } else {
            Log.e(TAG, "No address for mail action");
        }
    }

    @Override
    public void init(Context context, JSONObject jsonObject) {

    }

    @Override
    public void update(Context context, JSONObject jsonObject) {

    }

    @Override
    public boolean shouldDisplayNotification(Context context, NotificationDetails notificationDetails, Bundle bundle) {
        return true;
    }
}
