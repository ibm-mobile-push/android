/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2015
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.wca.samples.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
import com.ibm.mce.sdk.location.MceLocation;
import com.ibm.mce.sdk.plugin.inapp.InAppManager;
import com.ibm.wca.samples.android.layout.ResourcesHelper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WcaNotifier extends MceBroadcastReceiver {

    protected static String TAG = WcaNotifier.class.getName();

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
        if(registrationDetails.getPushToken() != null) {
            showNotification(context, resourcesHelper.getString("gcm_reg_subject"), registrationDetails.getPushToken(), ACTION_GCM_REGISTRATION);
        }
    }

    @Override
    public void onSdkRegistrationChanged(Context context)
    {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(context);
        Log.i(TAG, "-- SDK registration changed");
        Log.i(TAG, "Channel ID is: " + registrationDetails.getChannelId());
        Log.i(TAG, "User ID is: " + registrationDetails.getUserId());

        showNotification(context, resourcesHelper.getString("sdk_rereg_subject"), resourcesHelper.getString("sdk_reg_msg_prefix") + ": " + registrationDetails.getChannelId(), ACTION_SDK_REGISTRATION);
    }

    @Override
    public void onSdkRegistrationUpdated(Context context)
    {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(context);
        Log.i(TAG, "-- SDK registration updated");
        Log.i(TAG, "Channel ID is: " + registrationDetails.getChannelId());
        Log.i(TAG, "User ID is: " + registrationDetails.getUserId());

        showNotification(context, resourcesHelper.getString("sdk_reg_update_subject"), resourcesHelper.getString("sdk_reg_msg_prefix") + ": " + registrationDetails.getChannelId(), ACTION_SDK_REGISTRATION);
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
        String mailingId = null;
        if(notificationDetails != null && notificationDetails.getMceNotificationPayload() != null) {
            mailingId = notificationDetails.getMceNotificationPayload().getMailingId();
        }
        InAppManager.handleNotification(context, bundle, attribution, mailingId);
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
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(context);
        Log.i(TAG, "-- Attributes operation performed");
        Log.i(TAG, "Type is: " + attributesOperation.getType());
        if(attributesOperation.getAttributeKeys() != null) {
            Log.i(TAG, "Keys: "+attributesOperation.getAttributeKeys());
            showNotification(context, resourcesHelper.getString("attribute_action_succeeded"), resourcesHelper.getString("attribute_action_delete")+" "+attributesOperation.getAttributeKeys(), ACTION_SDK_REGISTRATION);
        } else if(attributesOperation.getAttributes() != null) {
            String attributesStr = getAttributesString(attributesOperation.getAttributes());
            Log.i(TAG, "Attributes: "+attributesStr);
            showNotification(context, resourcesHelper.getString("attribute_action_succeeded"), resourcesHelper.getString("attribute_action_update")+" "+attributesStr, ACTION_SDK_REGISTRATION);
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
                    .append(", key = ").append(attribute.getKey())
                    .append(", value = ").append(attribute.getValue()).append("}");
            for(int i = 1 ; i < attributes.size() ; ++i) {
                attribute = attributes.get(i);
                builder.append(", {type = ").append(attribute.getType())
                        .append(", key = ").append(attribute.getKey())
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
        Log.i(TAG, "Events: " + builder.toString());
    }

    @Override
    public void onIllegalNotification(Context context, Intent intent) {
        Log.i(TAG, "-- Illegal SDK notification received");
    }

    @Override
    public void onNonMceBroadcast(Context context, Intent intent) {
        Log.i(TAG, "-- Non SDK broadcast received");
    }

    public void onLocationEvent(Context context, MceLocation location, LocationType locationType, LocationEventType locationEventType) {
        Log.d(TAG, "Location event: " + locationType.name() + " " + locationEventType.name() + " id = " + location.getId());
        // Uncomment this line to have the device display its location for testing purposes.
        // Do not ship with this uncommented.
        // showNotification(context, locationType.name()+" "+locationEventType.name(), location.getId(), locationType.name()); }

    }

    @Override
    public void onLocationUpdate(Context context, Location location) {
        Log.d(TAG, "Location was updated "+location);
    }

    protected void showNotification(Context context, String subject, String message, String action) {
        ResourcesHelper resourcesHelper = new ResourcesHelper(context.getResources(), context.getPackageName());
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent actionIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        actionIntent.putExtra(ACTION_KEY, action);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SampleApplication.MCE_SAMPLE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(resourcesHelper.getDrawableId("icon"))
                .setContentTitle(subject)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("MCE Sample")
                .setContentInfo("Mce Sample Info")
                .setContentIntent(PendingIntent.getActivity(context, action.hashCode(), actionIntent, 0));
        Notification notification = builder.build();
        setNotificationPreferences(context, notification);
        UUID notifUUID = UUID.randomUUID();
        notificationManager.notify(notifUUID.hashCode(), notification);

    }

    protected void setNotificationPreferences(Context context,
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
