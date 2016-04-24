package com.mdb.wyn.stayfocused;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Young on 4/2/2016.
 */
public class AppListActivity extends AppCompatActivity {
    AppListAdapter appListAdapter;
    CheckBox selectAll;
    ArrayList<AppListItem> appListItems;
    CustomSharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);
        selectAll= (CheckBox) findViewById(R.id.selectAll);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appListItems = new ArrayList<>();

        prefs = MainActivity.customPrefs;
        Set<String> blacklist = prefs.getSet(CustomSharedPreferences.BLACKLIST_KEY);
        Set<String> applist = prefs.getSet(CustomSharedPreferences.APPNAMES_KEY);
        for (String appName : applist) {
            boolean isChecked = false;
            if (blacklist != null && blacklist.contains(appName)) {
                isChecked = true;
            }
            appListItems.add(new AppListItem(appName, isChecked));
        }

        appListAdapter = new AppListAdapter(getApplicationContext(), appListItems);
        recyclerView.setAdapter(appListAdapter);


//this is to add system apps after the pressing the Show System Apps button
        final Button showSystemApps= (Button) findViewById(R.id.systemApps);
        showSystemApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showSystemApps.getText()=="Show System Apps"){
                    Toast.makeText(getApplicationContext(), "Blacklisting system apps can lead to unpredictable results.", Toast.LENGTH_LONG).show();
                    showSystemApps.setText("Hide System Apps");
                    for (String appName : MainActivity.systemAppList) {
                        appListItems.add(new AppListItem(appName, false));
                    }
                }
                else {
                    showSystemApps.setText("Show System Apps");
                    for (String appName : MainActivity.systemAppList) {
                        //find way to remove
//                        appListItems.remove(new AppListItem(appName, false));
                    }
                }

            }
        });
    }

    private void setSelectAllCheckBox() {
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //result of changing the checkbox
                //dunno if need to notify datasetchanged
                if (isChecked) {
                    // TODO: check all the apps
                }
            }
        });
    }

    public void onCheckBoxClicked(View view) {
        //this is for the select all method
        boolean checked = ((CheckBox) view).isChecked();
        for (AppListItem eachItem: AppListAdapter.appListArray){
            eachItem.isBlacklisted= checked;
            appListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        ArrayList<AppListItem> appListItems = appListAdapter.getAppListArray();
        System.out.println("on back pressed " + appListItems);
//        Set<String> blacklist = prefs.getSet(CustomSharedPreferences.BLACKLIST_KEY);
//        if (blacklist == null) {
        Set<String> blacklist = new HashSet<>();
//        }
        for (AppListItem item : appListItems) {
            if (item.isBlacklisted) {
                blacklist.add(item.appName);
            }
        }
        System.out.println("before shared prefs, local blacklist is " + blacklist);
        prefs.saveList(CustomSharedPreferences.BLACKLIST_KEY, blacklist);
        System.out.println("after saving, sharedprefs blacklist is " + prefs.getSet(CustomSharedPreferences.BLACKLIST_KEY));
        setResult(RESULT_OK);
        finish();
    }

}