package com.mdb.wyn.stayfocused;

import android.graphics.drawable.Drawable;

/**
 * Created by Young on 4/2/2016.
 */
public class AppListItem {
    public String appName;
    public Boolean isBlacklisted;
    Drawable icon;
    public AppListItem(String appName, Boolean isBlacklisted, Drawable icon) {
        this.appName= appName;
        this.isBlacklisted= isBlacklisted;
        this.icon = icon;
    }
}