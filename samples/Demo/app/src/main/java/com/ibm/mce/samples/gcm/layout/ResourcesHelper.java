package com.ibm.mce.samples.gcm.layout;

import android.content.res.Resources;

public class ResourcesHelper {

    private Resources resources;
    private String packageName;

    public ResourcesHelper(Resources resources, String packageName) {
        this.resources = resources;
        this.packageName = packageName;
    }

    public int getLayoutId(String layoutName) {
        return getResourceId(layoutName, "layout");
    }

    public int getMenuId(String menuName) {
        return getResourceId(menuName, "menu");
    }

    public int getDrawableId(String drawableName) {
        return getResourceId(drawableName, "drawable");
    }

    public int getRawId(String rawName) {
        return getResourceId(rawName, "raw");
    }

    public int getStringId(String stringName) {
        return getResourceId(stringName, "string");
    }

    public String getString(String stringName) {
        return resources.getString(getStringId(stringName));
    }

    public int getId(String name) {
        return getResourceId(name, "id");
    }

    private int getResourceId(String resourceName, String resourceType) {
        return resources.getIdentifier(resourceName, resourceType, packageName);
    }
}
