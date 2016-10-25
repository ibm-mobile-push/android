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
import android.widget.ListView;
import android.widget.Toast;


import com.ibm.mce.sdk.api.MceSdk;
import com.ibm.mce.sdk.api.OperationCallback;
import com.ibm.mce.sdk.api.OperationResult;
import com.ibm.mce.sdk.api.attribute.Attribute;
import com.ibm.mce.sdk.api.attribute.AttributesOperation;
import com.ibm.mce.sdk.api.attribute.StringAttribute;
import com.ibm.mce.samples.gcm.layout.ClickItemLayout;
import com.ibm.mce.samples.gcm.layout.KeyValueLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AttributesSampleActivity extends ListSampleActivity {

    private static final String TAG = "AttributesActivity";

    // Action indices
    private static final int ATTRIBUTE_KEY_INDEX = 0;
    private static final int ATTRIBUTE_VALUE_INDEX = 1;
    private static final int ATTRIBUTE_ACTION_INDEX = 2;
    private static final int ATTRIBUTE_SEND_INDEX = 3;

    protected KeyValueLayout.KeyValueString attributeKey;
    protected KeyValueLayout.KeyValueString attributeValue;
    protected KeyValueLayout.KeyValueOptions attributeActions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListView((ListView) findViewById(resourcesHelper.getId("listView2")));
    }

    @Override
    protected List<String> getListItems() {
        LinkedList<String> items = new LinkedList<String>();
        items.add(resourcesHelper.getString("attr_key_title"));
        items.add(resourcesHelper.getString("attr_value_title"));
        items.add(resourcesHelper.getString("attr_action_title"));
        items.add(resourcesHelper.getString("attr_send_title"));
        return items;
    }

    @Override
    protected Object[] createUIValues(String[] itemsArray) {
        Object[] uiValues = new Object[itemsArray.length];

        String setActionName = resourcesHelper.getString("attribute_action_set");
        String updateActionName = resourcesHelper.getString("attribute_action_update");
        String deleteActionName = resourcesHelper.getString("attribute_action_delete");

        attributeKey = new KeyValueLayout.KeyValueString(itemsArray[ATTRIBUTE_KEY_INDEX] ,resourcesHelper.getString("attribute_default_key"), true);
        attributeValue =  new KeyValueLayout.KeyValueString(itemsArray[ATTRIBUTE_VALUE_INDEX] ,resourcesHelper.getString("attribute_default_value"), true);
        attributeActions = new KeyValueLayout.KeyValueOptions(itemsArray[ATTRIBUTE_ACTION_INDEX] ,new String[] {setActionName, updateActionName, deleteActionName});


        uiValues[ATTRIBUTE_KEY_INDEX] = attributeKey;
        uiValues[ATTRIBUTE_VALUE_INDEX] = attributeValue;
        uiValues[ATTRIBUTE_ACTION_INDEX] = attributeActions;
        uiValues[ATTRIBUTE_SEND_INDEX] = new ClickItemLayout.ClickItem(itemsArray[ATTRIBUTE_SEND_INDEX]);

        return uiValues;
    }

    @Override
    protected String getLayoutName() {
        return "activity_attributes";
    }

    @Override
    protected String getMenuName() {
        return "menu_attributes";
    }

    @Override
    protected String getSettingsName() {
        return "action_settings";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == ATTRIBUTE_SEND_INDEX) {
            StringAttribute attribute = new StringAttribute(attributeKey.getValue(), attributeValue.getValue());
            List<Attribute> attributes = new ArrayList<>(1);
            attributes.add(attribute);
            List<String> attributeKeys = new ArrayList<String>(1);
            attributeKeys.add(attribute.getKey());
            String action = attributeActions.getValue();
            OperationCallback<AttributesOperation> callback = new OperationCallback<AttributesOperation>() {
                @Override
                public void onSuccess(AttributesOperation attributesOperation, OperationResult result) {
                    Log.d(TAG, "Attributes operation was successful: "+attributesOperation.getType());
                    AttributesSampleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AttributesSampleActivity.this, resourcesHelper.getString("attribute_action_succeeded"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(AttributesOperation attributesOperation, OperationResult result) {
                    Log.d(TAG, "Attributes operation failed: "+attributesOperation.getType()+". "+result.getMessage());
                    AttributesSampleActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AttributesSampleActivity.this, resourcesHelper.getString("attribute_action_failed"), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };

            if (resourcesHelper.getString("attribute_action_set").equals(action)) {
                MceSdk.getAttributesClient(false).setUserAttributes(getApplicationContext(), attributes, callback);
            } else if(resourcesHelper.getString("attribute_action_update").equals(action)) {
                MceSdk.getAttributesClient(false).updateUserAttributes(getApplicationContext(), attributes, callback);
            } else if(resourcesHelper.getString("attribute_action_delete").equals(action)) {
                MceSdk.getAttributesClient(false).deleteUserAttributes(getApplicationContext(), attributeKeys, callback);
            }

        }
    }
}
