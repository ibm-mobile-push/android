/*
 * Licensed Materials - Property of IBM
 *
 * 5725E28, 5725I03
 *
 * © Copyright IBM Corp. 2011, 2015
 * US Government Users Restricted Rights - Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
package com.ibm.mce.samples.gcm.layout;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


public class ClickItemLayout implements CustomListAdapter.CustomItemLayout {

    @Override
    public View getView(Object item, Activity activity, CustomListAdapter adapter, int position) {
        ResourcesHelper resourcesHelper = new ResourcesHelper(activity.getResources(), activity.getPackageName());
        LayoutInflater inflater = activity.getLayoutInflater();
        final View clickItemView = inflater.inflate(resourcesHelper.getLayoutId("click_item"), null, true);
        TextView itemView = (TextView)clickItemView.findViewById(resourcesHelper.getId("itemView"));
        ClickItem clickItem = (ClickItem)item;
        itemView.setText(clickItem.getItemText());
        return clickItemView;
    }

    public static class ClickItem {
        private String itemText;

        public ClickItem(String itemText) {
            this.itemText = itemText;
        }

        public String getItemText() {
            return itemText;
        }
    }
}
