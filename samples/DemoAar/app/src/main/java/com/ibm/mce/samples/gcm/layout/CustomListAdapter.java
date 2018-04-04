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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomListAdapter extends BaseAdapter {

    private static final Map<Class, Class> CLASS_TO_LAYOUT_CLASS = new HashMap<>();

    private Activity activity;
    private Object[] items;
    private int focusedPosition = -1;
    private ValueChangeListener valueChangeListener;
    private Map<Integer, CustomItemLayout> positionToLayout = new HashMap<>();
    private String id = null;

    public static void registerCustomLayout(Class cls, CustomItemLayout layout) {
        CLASS_TO_LAYOUT_CLASS.put(cls, layout.getClass());
    }

    public static CustomItemLayout getRegisteredLayout(Class cls) {
        try {
            return (CustomItemLayout)(CLASS_TO_LAYOUT_CLASS.get(cls).newInstance());
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    static {
        registerCustomLayout(KeyValueLayout.KeyValueString.class, new KeyValueLayout());
        registerCustomLayout(KeyValueLayout.KeyValueOptions.class, new KeyValueLayout());
        registerCustomLayout(ClickItemLayout.ClickItem.class, new ClickItemLayout());
    }

    public CustomListAdapter(Activity activity, ValueChangeListener valueChangeListener, Object... items) {
        this.activity = activity;
        this.valueChangeListener = valueChangeListener;
        this.items = items;
    }

    public CustomListAdapter(Activity activity, ValueChangeListener valueChangeListener, Collection items) {
        this(activity, valueChangeListener, items.toArray());
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setValueChangeListener(ValueChangeListener listener) {
        valueChangeListener = listener;
    }

    public void valueChanged(int position, Object value) {
        if(valueChangeListener != null) {
            valueChangeListener.valueChanged(this, position, value);
        }
    }


    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object value = getItem(position);
        CustomItemLayout layout = positionToLayout.get(position);
        if(layout == null) {
            layout = getRegisteredLayout(value.getClass());
            positionToLayout.put(position, layout);
        }
        if(layout != null) {
            convertView = layout.getView(value, activity, this, position);
            if(position == focusedPosition) {
                convertView.requestFocus();
            }
        }
        return convertView;
    }

    public void setFocusedPosition(int position) {
        focusedPosition = position;
    }

    public static interface CustomItemLayout {
        public View getView(Object item, Activity activity, CustomListAdapter adapter, int position);
    }

    public static interface ValueChangeListener {
        public void valueChanged(CustomListAdapter adapter, int position, Object value);
    }

}
