package com.mdb.wyn.stayfocused;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Wilbur on 4/23/2016.
 */
public class CustomSharedPreferences {

    public static final String BLACKLIST_KEY = "blacklist_key";
    public static final String APPNAMES_KEY = "appnames_key";
    public static final String SYSTEMAPPS_KEY = "systemapps_key";
    private HashMap<String, Boolean> blacklistMap;
    private HashSet<String> appNames;
    private SharedPreferences mSharedPrefs;

    public CustomSharedPreferences(SharedPreferences prefs) {
        mSharedPrefs = prefs;
    }

    public void saveList(String key, Set<String> list) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        editor.putStringSet(key, list);
//        for (AppListItem item : list) {
//            appNames.add(item.appName);
//            if (!blacklistMap.containsKey(item.appName)) {
//                blacklistMap.put(item.appName, item.isBlacklisted);
//            }
//        }
        editor.apply();
    }

    public void changeChecked(String appName, boolean isChecked) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putBoolean(appName, isChecked);
        editor.apply();
    }

    public void removeFromBlackList(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        Set<String> blacklist = getSet(BLACKLIST_KEY);
        blacklist.remove(appName);
        editor.putStringSet(BLACKLIST_KEY, blacklist);
        editor.apply();
    }

    public void addToBlackList(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        Set<String> blacklist = getSet(BLACKLIST_KEY);
        blacklist.add(appName);
        editor.putStringSet(BLACKLIST_KEY, blacklist);
        editor.apply();
    }

    public Set<String> getSet(String key) {
        return mSharedPrefs.getStringSet(key, null);
    }

    public boolean getCheckedPref(String key) {
        return mSharedPrefs.getBoolean(key, false);
    }
}
