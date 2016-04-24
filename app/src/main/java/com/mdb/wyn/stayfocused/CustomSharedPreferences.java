package com.mdb.wyn.stayfocused;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Wilbur on 4/23/2016.
 */
public class CustomSharedPreferences {

    public static final String BLACKLIST_KEY = "blacklist_key";
    private HashMap<String, Boolean> blacklistMap;
    private HashSet<String> appNames;
    private SharedPreferences mSharedPrefs;

    public CustomSharedPreferences(SharedPreferences prefs) {
        mSharedPrefs = prefs;
    }

    public void saveBlackList(Context context, List<AppListItem> list) {
        if (blacklistMap == null) {
            blacklistMap = new HashMap<>();
        }
        if (appNames == null) {
            appNames = new HashSet<>();
        }
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        for (AppListItem item : list) {
            appNames.add(item.appName);
            if (!blacklistMap.containsKey(item.appName)) {
                blacklistMap.put(item.appName, item.isBlacklisted);
            }
        }

        editor.putStringSet(BLACKLIST_KEY, appNames);
        editor.apply();
    }

    public void removeFromList(AppListItem item) {

    }


}
