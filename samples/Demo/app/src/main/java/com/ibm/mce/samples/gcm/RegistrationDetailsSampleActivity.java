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
import android.widget.ListView;

import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.registration.RegistrationDetails;
import com.ibm.mce.samples.gcm.layout.KeyValueLayout;

import java.util.LinkedList;
import java.util.List;

public class RegistrationDetailsSampleActivity extends ListSampleActivity {

    private static final int USER_ID_INDEX = 0;
    private static final int CHANNEL_ID_INDEX = 1;
    private static final int APPKEY_INDEX = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListView((ListView) findViewById(resourcesHelper.getId("listView2")));
    }

    @Override
    protected List<String> getListItems() {
        LinkedList<String> items = new LinkedList<String>();
        items.add(resourcesHelper.getString("user_id_title"));
        items.add(resourcesHelper.getString("channel_id_title"));
        items.add(resourcesHelper.getString("appkey_title"));
        return items;
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        String notRegistered = resourcesHelper.getString("not_registered");

        KeyValueLayout.KeyValue<String>[] uiValues = new KeyValueLayout.KeyValue[itemsArray.length];
        RegistrationDetails registrationDetails = MceSdk.getRegistrationClient().getRegistrationDetails(getApplicationContext());

        String userId = registrationDetails.getUserId() != null ? registrationDetails.getUserId() : notRegistered;
        String channelId = registrationDetails.getChannelId() != null ? registrationDetails.getChannelId() : notRegistered;
        uiValues[USER_ID_INDEX] = new KeyValueLayout.KeyValueString(itemsArray[USER_ID_INDEX] ,userId);
        uiValues[CHANNEL_ID_INDEX] = new KeyValueLayout.KeyValueString(itemsArray[CHANNEL_ID_INDEX] ,channelId);
        uiValues[APPKEY_INDEX] = new KeyValueLayout.KeyValueString(itemsArray[APPKEY_INDEX] ,MceSdk.getRegistrationClient().getAppKey(getApplicationContext()));

        return uiValues;
    }

    @Override
    public String getLayoutName() {
        return "activity_credentials";
    }

    @Override
    protected String getMenuName() {
        return "menu_main";
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }
}
