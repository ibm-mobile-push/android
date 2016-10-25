/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2015
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mce.samples.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.registration.RegistrationDetails;
import com.ibm.mce.sdk.plugin.inapp.InAppManager;
import com.ibm.mce.sdk.plugin.inapp.InAppStorage;
import com.ibm.mce.sdk.plugin.inbox.InboxMessagesClient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class MainSampleMenuActivity extends ListSampleActivity {

    private static final int REGISTRATION_DETAILS_INDEX = 0;
    private static final int SEND_TEST_EVENTS_INDEX = 1;
    private static final int SEND_USER_ATTRIBUTES_INDEX = 2;
    private static final int INAPP_INDEX = 3;
    private static final int INBOX_INDEX = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMainView();
    }

    @Override
    protected void handleSdkRegistration() {
        super.handleSdkRegistration();
        Intent goToIntent = new Intent();
        goToIntent.setClass(getApplicationContext(), getRegistrationActivityClass());
        startActivity(goToIntent);
    }

    @Override
    protected void handleGcmRegistration() {
        super.handleGcmRegistration();
        handleSdkRegistration();
    }

    private void showMainView() {
        setupListView((ListView)findViewById(resourcesHelper.getId("listView")));
        if(isTitle()) {
            TextView titleView = (TextView) findViewById(resourcesHelper.getId("title"));
            titleView.setText(resourcesHelper.getString("title") + " " + MceSdk.getSdkVerNumber());
        }
    }

    @Override
    protected List<String> getListItems() {
        LinkedList<String> items = new LinkedList<String>();
        items.add(resourcesHelper.getString("registration_details_title"));
        items.add(resourcesHelper.getString("send_test_events_title"));
        items.add(resourcesHelper.getString("send_user_attributes_title"));
        items.add(resourcesHelper.getString("inapp_title"));
        items.add(resourcesHelper.getString("inbox_title"));
        return items;
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        return null;
    }

    @Override
    protected ListAdapter createListAdapter() {
        List<String> items = getListItems();
        String[] values = items.toArray(new String[items.size()]);

        return new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
    }

    @Override
    protected String getMenuName() {
        return null;
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }

    @Override
    protected String getLayoutName() {
        return "activity_main";
    }

    protected boolean isTitle() {
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == REGISTRATION_DETAILS_INDEX) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), getRegistrationActivityClass());
            startActivity(intent);
        } else if (position == SEND_TEST_EVENTS_INDEX) {

            RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());
            if (registrationDetails.getChannelId() == null || registrationDetails.getChannelId().length() == 0) {
                Toast.makeText(MainSampleMenuActivity.this, resourcesHelper.getString("no_sdk_reg_toast"), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), getEventsActivityClass());
                startActivity(intent);
            }
        } else if (position == SEND_USER_ATTRIBUTES_INDEX) {
            RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());
            if (registrationDetails.getChannelId() == null || registrationDetails.getChannelId().length() == 0) {
                Toast.makeText(MainSampleMenuActivity.this, resourcesHelper.getString("no_sdk_reg_toast"), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), getAttributesActivityClass());
                startActivity(intent);
            }
        } else if (position == INAPP_INDEX) {
            InAppManager.show(getApplicationContext(), getSupportFragmentManager(), resourcesHelper.getId("con"));
            inAppShown = true;
        } else if (position == INBOX_INDEX) {
            InboxMessagesClient.showInbox(getApplicationContext());
        }
    }

    public Class getRegistrationActivityClass() {
        return RegistrationDetailsSampleActivity.class;
    }

    public Class getAttributesActivityClass() {
        return AttributesSampleActivity.class;
    }

    public Class getEventsActivityClass() {
        return EventSampleActivity.class;
    }
}

