package com.mdb.wyn.stayfocused;
/**
 * Created by Young on 4/2/2016.
 */
public class AppListItem {
    public String appName;
    public Boolean isBlacklisted;
    public AppListItem(String appName, Boolean isBlacklisted) {
        this.appName= appName;
        this.isBlacklisted= isBlacklisted;
    }
}