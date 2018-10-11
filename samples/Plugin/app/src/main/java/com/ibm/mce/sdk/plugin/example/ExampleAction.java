/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2017
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mce.sdk.plugin.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.ibm.mce.sdk.api.notification.MceNotificationAction;
import com.ibm.mce.sdk.api.notification.NotificationDetails;
import java.io.Serializable;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONException;
import com.ibm.mce.sdk.util.Logger;

public class ExampleAction implements MceNotificationAction {

    public ExampleAction() {
    }

    final String TAG = "ExampleAction";

    /**
     *This method implements the "example" action.
     * @param context - The application context
     * @param type - The notification action type
     * @param name - The notification action name (can be null)
     * @param attribution - The notification attribution (can be null)
     * @param mailingID - The notification mailing id
     * @param payload - The notification payload
     * @param fromNotification - true if this action is called from a notification and false otherwise
     */
    @Override
    public void handleAction(Context context, String type, String name, String attribution, String mailingID, Map<String, String> payload, boolean fromNotification) {
        String payloadValue = payload.get("value");
        if (payloadValue == null) {
            Logger.d(TAG, "no payload with value, do nothing");
            return;
        }

        //get custom properties defined in the action
        JSONObject customProperties = null;
        try {
            customProperties = new JSONObject(payloadValue);
        } catch (JSONException exception) {
            Logger.d(TAG, "couldn't parse JSON, giving up");
            return;
        }

        if (customProperties == null) {
            //The JSONObject code exception should handle this case, but if it doesn't, make sure we don't crash
            Logger.d(TAG, "couldn't acquire custom properties, giving up");
            return;
        }

        Logger.d(TAG, "customProperties = " + customProperties);
        boolean sendCustomEvent = customProperties.optBoolean("sendCustomEvent", true);
        boolean openForAction = customProperties.optBoolean("openForAction", true);
        Logger.d(TAG, "sendCustomEvent = " + sendCustomEvent + ", openForAction = " + openForAction);

        //start activity
        Intent intent = new Intent(context, ExampleActivity.class);
        intent.putExtra("payload", (Serializable) payload);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sendCustomEvent", sendCustomEvent);
        intent.putExtra("openForAction", openForAction);
        context.startActivity(intent);

    }
    /**
     *Initiates the action with the given options
     * @param context - The application's context
     * @param initOptions - The initialization options
     */
    @Override
    public void init(Context context, JSONObject initOptions) {
    }

    /**
     *Updates the action with the given options
     * @param context - The application's context
     * @param updateOptions - The update options
     */
    @Override
    public void update(Context context, JSONObject updateOptions) {
    }

    /**
     *This method is called when a notification with this action as its main action.
     * @param context - The application's context
     * @param notificationDetails - The received notification
     * @param sourceBundle - The bundle that contained the notification
     * @return can be true or false: true if you want to show a notification, false if you want a silent push
     */
    @Override
    public boolean shouldDisplayNotification(Context context, NotificationDetails notificationDetails, Bundle sourceBundle) {
        return true;
    }
}