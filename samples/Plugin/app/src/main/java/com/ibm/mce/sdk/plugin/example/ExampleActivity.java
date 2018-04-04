/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2017
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.ibm.mce.sdk.plugin.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.ibm.mce.sdk.api.Constants;
import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.attribute.Attribute;
import com.ibm.mce.sdk.api.attribute.StringAttribute;
import com.ibm.mce.sdk.api.event.Event;
import com.ibm.mce.sdk.util.Logger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ExampleActivity extends AppCompatActivity {

    final String TAG = "ExampleActivity";

    @Override
    protected void onCreate(Bundle context) {
        HashMap<String, String> payload = (HashMap<String, String>) getIntent().getExtras().getSerializable("payload");
        String payloadAsString = payload.toString();
        boolean sendCustomEvent = getIntent().getBooleanExtra("sendCustomEvent", true);
        boolean openForAction = getIntent().getBooleanExtra("openForAction", true);
        String mailingId = getIntent().getStringExtra("mailingId");

        //send custom metric to server
        if (sendCustomEvent) {
            List<Attribute> attributes = new LinkedList<Attribute>();
            attributes.add(new StringAttribute("payload", "{\"customData1\":\"exampleEvent\", \"customData2\":" + openForAction + ", \"customData3\":" + sendCustomEvent));
            Event event = new Event(Constants.Notifications.SIMPLE_NOTIFICATION_EVENT_TYPE, "custom", new Date(), attributes, "example", mailingId);
            MceSdk.getQueuedEventsClient().sendEvent(getApplicationContext(), event);
        }

        //send payload to screen
        if (openForAction) {
            super.onCreate(context);
            int layoutId = getResources().getIdentifier("activity_example", "layout", getPackageName());
            setContentView(layoutId);
            int exampleId = getResources().getIdentifier("payload_values", "id", getPackageName());
            TextView payloadTextView = (TextView) findViewById(exampleId);
            payloadTextView.setText(payloadAsString);
        }
        //send payload to log
        else {
            Logger.d(TAG, "Payload is: " + payloadAsString);
        }
    }
}