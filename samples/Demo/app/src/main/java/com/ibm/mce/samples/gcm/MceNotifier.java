/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2015
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mce.samples.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ibm.mce.sdk.api.MceBroadcastReceiver;
import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.attribute.Attribute;
import com.ibm.mce.sdk.api.attribute.AttributesOperation;
import com.ibm.mce.sdk.api.event.Event;
import com.ibm.mce.sdk.api.notification.NotificationDetails;
import com.ibm.mce.sdk.api.registration.RegistrationDetails;
import com.ibm.mce.sdk.plugin.inapp.InAppManager;
import com.ibm.mce.samples.gcm.layout.ResourcesHelper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MceNotifier extends MceBroadcastReceiver {

    private static String TAG = MceNotifier.class.getName();

    public static String ACTION_KEY = "action";
    public static String ACTION_SDK_REGISTRATION = "sdkreg";
    public static String ACTION_GCM_REGISTRATION = "gcmreg";

    @Override
    public void onSdkRegistered(Context context) {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(context);
        Log.i(TAG, "-- SDK registered");
        Log.i(TAG, "Channel ID is: " + registrationDetails.getChannelId());
        Log.i(TAG, "User ID is: " + registrationDetails.getUserId());

        showNotification(context,  resourcesHelper.getString("sdk_reg_subject"),  resourcesHelper.getString("sdk_reg_msg_prefix")+": "+registrationDetails.getChannelId(), ACTION_SDK_REGISTRATION);

    }

    @Override
    public void onC2dmError(Context context, String errorId) {
        Log.i(TAG, "ErrorId: " + errorId);
    }

    @Override
    public void onDeliveryChannelRegistered(Context context) {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(context);
        Log.i(TAG, "-- SDK delivery channel registered");
        Log.i(TAG, "Registration ID  is: " + registrationDetails.getPushToken());

        showNotification(context,resourcesHelper.getString("gcm_reg_subject"), registrationDetails.getPushToken(), ACTION_GCM_REGISTRATION);
    }

    @Override
    public void onSdkRegistrationChanged(Context context)
    {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(context);
        Log.i(TAG, "-- SDK registration changed");
        Log.i(TAG, "Channel ID is: " + registrationDetails.getChannelId());
        Log.i(TAG, "User ID is: " + registrationDetails.getUserId());

        showNotification(context,  resourcesHelper.getString("sdk_reg_subject"), resourcesHelper.getString("sdk_reg_msg_prefix")+": "+registrationDetails.getChannelId(), ACTION_SDK_REGISTRATION);
    }

    @Override
    public void onMessage(Context context, NotificationDetails notificationDetails, final Bundle bundle) {
        if(notificationDetails != null) {
            Log.i(TAG, "-- SDK delivery channel message received");
            Log.i(TAG, "Subject is: " + notificationDetails.getSubject());
            Log.i(TAG, "Message is: " + notificationDetails.getMessage());
        }
        String attribution = null;
        if(notificationDetails != null && notificationDetails.getMceNotificationPayload() != null) {
            attribution = notificationDetails.getMceNotificationPayload().getAttribution();
        }
        InAppManager.handleNotification(context, bundle, attribution);
    }

    @Override
    public void onSessionStart(Context context, Date date) {
        Log.i(TAG, "-- SDK session started");
        Log.i(TAG, "Date is: " + date);
    }

    @Override
    public void onSessionEnd(Context context, Date date, long sessionDurationInMinutes) {
        Log.i(TAG, "-- SDK session ended");
        Log.i(TAG, "Date is: " + date);
        Log.i(TAG, "Session duration is: " + sessionDurationInMinutes);
    }

    @Override
    public void onNotificationAction(Context context, Date actionTime, String pushType, String actionType, String actionValue) {
        Log.i(TAG, "-- SDK notification clicked");
        Log.i(TAG, "Date is: " + actionTime);
        Log.i(TAG, "Push type is: " + pushType);
        Log.i(TAG, "Action type is: " + actionType);
        Log.i(TAG, "Action values is: " + actionValue);
    }

    @Override
    public void onAttributesOperation(Context context, AttributesOperation attributesOperation) {
        Log.i(TAG, "-- Attributes operation performed");
        Log.i(TAG, "Type is: " + attributesOperation.getType());
        if(attributesOperation.getAttributeKeys() != null) {
            Log.i(TAG, "Keys: "+attributesOperation.getAttributeKeys());
        } else if(attributesOperation.getAttributes() != null) {
            String attributesStr = getAttributesString(attributesOperation.getAttributes());
            Log.i(TAG, "Attributes: "+attributesStr);
        }
    }

    private String getAttributesString(List<Attribute> attributes) {
        if(attributes == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder("{");
        if(!attributes.isEmpty()) {
            Attribute attribute = attributes.get(0);
            builder.append("{type = ").append(attribute.getType())
                    .append(", key = ").append(attribute.getValue())
                    .append(", value = ").append(attribute.getValue()).append("}");
            for(int i = 1 ; i < attributes.size() ; ++i) {
                attribute = attributes.get(i);
                builder.append(", {type = ").append(attribute.getType())
                        .append(", key = ").append(attribute.getValue())
                        .append(", value = ").append(attribute.getValue()).append("}");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public void onEventsSend(Context context, List<Event> events) {
        Log.i(TAG, "-- Events were sent");

        StringBuilder builder = new StringBuilder("{");
        if(events!=null && !events.isEmpty()) {
            Event event = events.get(0);
            builder.append("{type = ").append(event.getType())
                    .append(", name = ").append(event.getName())
                    .append(", timestamp = ").append(event.getTimestamp())
                    .append(", attributes = ").append(getAttributesString(event.getAttributes()))
                    .append(", attribution = ").append(event.getAttribution()).append("}");
            for(int i = 1 ; i < events.size() ; ++i) {
                event = events.get(i);
                builder.append("{type = ").append(event.getType())
                        .append(", name = ").append(event.getName())
                        .append(", timestamp = ").append(event.getTimestamp())
                        .append(", attributes = ").append(getAttributesString(event.getAttributes()))
                        .append(", attribution = ").append(event.getAttribution()).append("}");
            }
        }
        builder.append("}");
        Log.i(TAG, "Events: "+builder.toString());
    }

    @Override
    public void onIllegalNotification(Context context, Intent intent) {
        Log.i(TAG, "-- Illegal SDK notification received");
    }

    @Override
    public void onNonMceBroadcast(Context context, Intent intent) {
        Log.i(TAG, "-- Non SDK broadcast received");
    }

    private void showNotification(Context context, String subject, String message, String action) {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent actionIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        actionIntent.putExtra(ACTION_KEY, action);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(resourcesHelper.getDrawableId("icon"))
                .setContentTitle(subject)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(context, action.hashCode(), actionIntent, 0));
        Notification notification = builder.build();
        setNotificationPreferences(context, notification);
        UUID notifUUID = UUID.randomUUID();
        notificationManager.notify(notifUUID.hashCode(), notification);

    }

    private  void setNotificationPreferences(Context context,
                                                   Notification notification) {
        if (MceSdk.getNotificationsClient().getNotificationsPreference().isSoundEnabled(context)) {
            Integer sound = MceSdk.getNotificationsClient().getNotificationsPreference().getSound(context);
            if (sound == null) {
                notification.defaults |= Notification.DEFAULT_SOUND;
            } else {
                notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + sound);
            }
        }
        if (MceSdk.getNotificationsClient().getNotificationsPreference().isVibrateEnabled(context)) {
            long[] vibrationPattern = MceSdk.getNotificationsClient().getNotificationsPreference().getVibrationPattern(context);
            if (vibrationPattern == null || vibrationPattern.length == 0) {
                notification.defaults |= Notification.DEFAULT_VIBRATE;
            } else {
                notification.vibrate = vibrationPattern;
            }
        }
        if (MceSdk.getNotificationsClient().getNotificationsPreference().isLightsEnabled(context)) {
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            int[] lightsPref = MceSdk.getNotificationsClient().getNotificationsPreference().getLights(context);
            if (lightsPref == null || lightsPref.length < 3) {
                notification.defaults |= Notification.DEFAULT_LIGHTS;
            } else {
                notification.ledARGB = lightsPref[0];
                notification.ledOnMS = lightsPref[1];
                notification.ledOffMS = lightsPref[2];
            }
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
    }
}
