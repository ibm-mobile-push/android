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
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ibm.mce.samples.gcm.layout.ResourcesHelper;

public abstract class SampleActivity extends AppCompatActivity {
    protected ResourcesHelper resourcesHelper;

    protected abstract String getMenuName();

    protected abstract String getSettingsName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourcesHelper = new ResourcesHelper(getResources(), getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menuName= getMenuName();
        if(menuName != null){
            getMenuInflater().inflate(resourcesHelper.getMenuId(getMenuName()), menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == resourcesHelper.getId(getSettingsName())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
