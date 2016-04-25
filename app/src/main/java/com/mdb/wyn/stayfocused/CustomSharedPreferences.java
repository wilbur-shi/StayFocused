package com.mdb.wyn.stayfocused;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;

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
    public static final String RINGER_KEY = "notifcations_key";
    private HashMap<String, Boolean> blacklistMap;
    private HashSet<String> appNames;
    private SharedPreferences mSharedPrefs;

    public CustomSharedPreferences(SharedPreferences prefs) {
        mSharedPrefs = prefs;
    }

    public void setRinger(int mode) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.putInt(RINGER_KEY, mode);
        editor.apply();
    }

    public int getRingerMode() {
        return mSharedPrefs.getInt(RINGER_KEY, AudioManager.RINGER_MODE_NORMAL);
    }

    public void saveList(String key, Set<String> list) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        editor.putStringSet(key, list);
        for (String name : list) {
            editor.putBoolean(name, true);
        }
        editor.apply();
    }

    public void changeChecked(String appName, boolean isChecked) {
        if (mSharedPrefs.contains(appName)) {
            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.putBoolean(appName, isChecked);
            editor.apply();
        }
    }

    public void removeFromBlackList(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();
        editor.remove(appName);
        editor.apply();
    }

//    public void addBlackListedApp(String appName) {
//        SharedPreferences.Editor editor = mSharedPrefs.edit();
//        editor.putBoolean(appName, true);
//        editor.apply();
//    }

    public Set<String> getSet(String key) {
        return mSharedPrefs.getStringSet(key, null);
    }

    public boolean getCheckedPref(String appName) {
        return mSharedPrefs.getBoolean(appName, false);
    }
}
