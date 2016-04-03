package com.mdb.wyn.stayfocused;
/**
 * Created by Young on 4/2/2016.
 */
public class BlackListItem {
    public String appName;
    public Boolean isBlacklisted;
    public BlackListItem(String appName,Boolean isBlacklisted) {
        this.appName= appName;
        this.isBlacklisted= isBlacklisted;
    }
}