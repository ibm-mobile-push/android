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
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class KeyValueLayout implements CustomListAdapter.CustomItemLayout {

    private View createKeyValueStringView(final KeyValueString stringsValue, Activity activity, final CustomListAdapter adapter, final int position) {
        final ResourcesHelper resourcesHelper = new ResourcesHelper(activity.getResources(), activity.getPackageName());
        LayoutInflater inflater = activity.getLayoutInflater();
        final View keyValueView = inflater.inflate(stringsValue.isEditable() ? (stringsValue.getPredefinedValues() != null ?  resourcesHelper.getLayoutId("keyvalue_editable_preferences") : resourcesHelper.getLayoutId("keyvalue_editable")) : (stringsValue.isExtraLarge() ? resourcesHelper.getLayoutId("keyvalue_extra_large") : resourcesHelper.getLayoutId("keyvalue")), null, true);
        if(stringsValue.isEditable()) {
            final EditText editText = (EditText)keyValueView.findViewById(resourcesHelper.getId("valueView"));
            editText.setInputType(stringsValue.getInputType());
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        adapter.setFocusedPosition(position);
                    }
                    String viewText = ((TextView) v).getText().toString();
                    if (!viewText.equals(stringsValue.getValue())) {
                        stringsValue.setValue(viewText);
                        if (stringsValue.getPredefinedValues() != null) {
                            final Spinner spinner = (Spinner) keyValueView.findViewById(resourcesHelper.getId("valuePreferencesView"));
                            int valuePosition = stringsValue.getValuePosition();
                            if (valuePosition >= 0) {
                                spinner.setSelection(valuePosition);
                            }
                        }
                        adapter.valueChanged(position, stringsValue.getValue());
                    }
                }
            });
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    stringsValue.disableForce();
                    return false;
                }
            });


            if(stringsValue.getPredefinedValues() != null) {
                createPredefinedValuesKeyValueStringView(stringsValue, activity, adapter, position, keyValueView, editText, activity);
            }
        }
        return keyValueView;
    }

    private void createPredefinedValuesKeyValueStringView(final KeyValueString stringsValue, Activity activity, final CustomListAdapter adapter, final int listPosition, View keyValueStringView, final EditText editText, final Context context) {
        final ResourcesHelper resourcesHelper = new ResourcesHelper(activity.getResources(), activity.getPackageName());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line, stringsValue.getPredefinedValues());
        final Spinner spinner = (Spinner)keyValueStringView.findViewById(resourcesHelper.getId("valuePreferencesView"));
        spinner.setAdapter(spinnerAdapter);
        int valuePosition = stringsValue.getValuePosition();
        if (valuePosition >= 0) {
            spinner.setSelection(valuePosition);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == stringsValue.getNoValueIndex()) {
                    if(stringsValue.isPreDefinedValue(editText.getText().toString())) {
                        stringsValue.setValue("");
                        editText.setText("");
                        adapter.valueChanged(listPosition, "");
                        editText.setClickable(true);
                        editText.setFocusable(true);
                        editText.setCursorVisible(true);
                    }
                }
                else {
                    Object value = spinner.getItemAtPosition(position).toString();
                    String text = editText.getText().toString();
                    if (!text.equals(value)) {
                        stringsValue.setValue(value.toString());
                        editText.setText(value.toString());
                        adapter.valueChanged(listPosition, value.toString());
                    }
                    editText.setClickable(false);
                    editText.setFocusable(false);
                    editText.setCursorVisible(false);
                    editText.setBackgroundColor(Color.TRANSPARENT);
                    editText.setTextColor(context.getResources().getColor(android.R.color.tertiary_text_dark));
                    editText.setKeyListener(null);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private View createKeyValueOptionsView(final KeyValueOptions optionsValue, Activity activity, final CustomListAdapter adapter, final int position) {
        final ResourcesHelper resourcesHelper = new ResourcesHelper(activity.getResources(), activity.getPackageName());


        View keyValueView = activity.getLayoutInflater().inflate(resourcesHelper.getLayoutId("keyvalue_options"), null, true);
        RadioGroup radioGroup = (RadioGroup) keyValueView.findViewById(resourcesHelper.getId("valueView"));
        String[] options = optionsValue.getOptions();
        for(int i=0 ; i<options.length ;++i) {
            RadioButton rb = new RadioButton(activity);

            rb.setText(options[i]);
            rb.setTextSize(TypedValue.COMPLEX_UNIT_PT, 5);


            radioGroup.addView(rb, i);
            if(i==optionsValue.getSelectedIndex()) {
                rb.setChecked(true);
            }
            final int index = i;
            rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                private boolean lastChecked = (index == optionsValue.getSelectedIndex());

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        optionsValue.setValue(index);
                        if(!lastChecked) {
                            adapter.valueChanged(position, optionsValue.getValue());
                        }
                        lastChecked = true;

                    } else if(!isChecked) {
                        lastChecked = false;
                    }
                }
            });
        }
        return keyValueView;
    }


    @Override
    public View getView(Object item, Activity activity, final CustomListAdapter adapter, final int position) {
        final ResourcesHelper resourcesHelper = new ResourcesHelper(activity.getResources(), activity.getPackageName());

        View keyValueView = null;

        final KeyValue value = (KeyValue)item;
        if(value instanceof KeyValueString) {
            final KeyValueString stringsValue = (KeyValueString)value;
            if(keyValueView == null) {
                keyValueView = createKeyValueStringView(stringsValue, activity, adapter, position);
                ((TextView)keyValueView.findViewById(resourcesHelper.getId("keyView"))).setText(value.getKey());
            }
            TextView renderedView = (TextView)keyValueView.findViewById(resourcesHelper.getId("valueView"));
            renderedView.setText(stringsValue.getValue());
        } else  if(value instanceof KeyValueOptions) {
            final KeyValueOptions optionsValue = (KeyValueOptions)value;
            if(keyValueView == null) {
                keyValueView = createKeyValueOptionsView(optionsValue, activity, adapter, position);
                ((TextView)keyValueView.findViewById(resourcesHelper.getId("keyView"))).setText(value.getKey());
            }
            RadioGroup radioGroup = (RadioGroup) keyValueView.findViewById(resourcesHelper.getId("valueView"));
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(optionsValue.getSelectedIndex());
            rb.setSelected(true);
        }
        return keyValueView;
    }

    public static abstract class KeyValue<T> {
        protected String key;
        protected T value;
        protected boolean extraLarge;

        public KeyValue(String key, T value, boolean extraLarge) {
            this.key = key;
            this.value = value;
            this.extraLarge = extraLarge;
        }

        public KeyValue(String key, T value){
            this(key, value, false);
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public  void setValue(T value) {
            this.value = value;
        }

        public boolean isExtraLarge() {
            return extraLarge;
        }
    }


    public static class KeyValueString extends KeyValue<String> {
        private boolean editable;
        private int inputType;
        private String[] predefinedValues;
        private int noValueIndex;
        private boolean force =false;
        private String forcedValue = null;

        public KeyValueString(String key, String value, boolean editable, String[] predefinedValues, int noValueIndex, boolean extraLarge) {
            super(key, value, extraLarge);
            inputType = EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_DATETIME_VARIATION_NORMAL;
            this.editable = editable;
            this.predefinedValues = predefinedValues;
            this.noValueIndex = noValueIndex;
            if(predefinedValues != null && noValueIndex!=0) {
                setValue(predefinedValues[0]);
            }
        }

        public KeyValueString(String key, String value, boolean editable, String[] predefinedValues, int noValueIndex) {
            this(key, value, editable, predefinedValues, noValueIndex, false);
        }

        public KeyValueString(String key, String value, boolean editable, String[] predefinedValues) {
            this(key, value, editable, predefinedValues, -1);
        }


        public KeyValueString(String key, String value, boolean editable) {
            this(key, value, editable, null);
        }

        public KeyValueString(String key, String value, boolean editable, boolean extraLarge) {
            this(key, value, editable, null, -1, true);
        }

        public KeyValueString(String key, String value) {
            this(key, value, false);
        }

        public boolean isEditable() {
            return editable;
        }

        public String[] getPredefinedValues() {
            return predefinedValues;
        }

        public int getNoValueIndex() {
            return noValueIndex;
        }

        public int getValuePosition() {
            Object value = getValue();
            for(int i = 0; i<predefinedValues.length ; ++i) {
                if(predefinedValues[i].equals(value)) {
                    return i;
                }
            }
            return getNoValueIndex();
        }

        public boolean isPreDefinedValue(String value) {
            for(int i=0; i<predefinedValues.length ;++i) {
                if(i != noValueIndex) {
                    if(predefinedValues[i].equals(value)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void setInputType(int inputType) {
            this.inputType = inputType;
        }

        public int getInputType() {
            return inputType;
        }

        @Override
        public String getValue() {
            if(!force) {
                return super.getValue();
            } else {
                return forcedValue;
            }
        }

        public void forceValue(String value) {
            setValue(value);
            forcedValue = value;
            force = true;
        }

        public void disableForce() {
            force = false;
            forcedValue = null;
        }

        public boolean isForce() {
            return force;
        }
    }

    public static class KeyValueOptions extends KeyValue<String>{
        private int selectedIndex;
        private String[] options;

        public KeyValueOptions(String key, String[] options, int selectedIndex) {
            super(key, options[selectedIndex]);
            this.selectedIndex = selectedIndex;
            this.options = options;
        }


        public KeyValueOptions(String key, String[] options) {
            this(key, options, 0);
        }


        public void setValue(Integer valueIndex) {
            selectedIndex = valueIndex;
            this.value = options[selectedIndex];
        }

        public int getSelectedIndex() {
            return selectedIndex;
        }

        public String[] getOptions() {
            return options;
        }
    }





}
