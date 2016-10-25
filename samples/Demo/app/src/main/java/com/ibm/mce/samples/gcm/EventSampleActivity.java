/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2015
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mce.samples.gcm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.mce.sdk.api.Constants;
import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.OperationCallback;
import com.ibm.mce.sdk.api.OperationResult;
import com.ibm.mce.sdk.api.attribute.Attribute;
import com.ibm.mce.sdk.api.attribute.StringAttribute;
import com.ibm.mce.sdk.api.event.Event;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class EventSampleActivity extends ListSampleActivity {

    private static final String TAG = "EventSampleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListView((ListView) findViewById(resourcesHelper.getId("listView2")));
    }

    @Override
    protected List<String> getListItems() {
        return new LinkedList<String>();
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        return null;
    }

    @Override
    protected ListAdapter createListAdapter() {
        return new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {resourcesHelper.getString("event_send_title")});
    }

    @Override
    protected String getLayoutName() {
        return "activity_event_test";
    }

    @Override
    protected String getMenuName() {
        return "menu_event_test";
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Attribute> attributes = new LinkedList<Attribute>();
        attributes.add(new StringAttribute("payload", "{\"sampleData\": \"A sample value\"}"));
        Event event = new Event(Constants.Notifications.SIMPLE_NOTIFICATION_EVENT_TYPE, "appOpened", new Date(), attributes, "sampleAttribution");
        OperationCallback<Event> callback = new OperationCallback<Event>() {
            @Override
            public void onSuccess(Event event, OperationResult result) {
                Log.d(TAG, "Event sent successfully: " + event.getType());
                EventSampleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EventSampleActivity.this, resourcesHelper.getString("event_send_succeeded"), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Event event, OperationResult result) {
                Log.d(TAG, "Event sending failed: " + event.getType() + ". " + result.getMessage());
                EventSampleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EventSampleActivity.this, resourcesHelper.getString("event_send_failed"), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        MceSdk.getEventsClient(false).sendEvent(getApplicationContext(), event, callback);
        /**
         * Comment out the line above and uncomment this section in order to send the event in queue
         MceSdk.getQueuedEventsClient().sendEvent(getApplicationContext(), event);
         */
    }
}
