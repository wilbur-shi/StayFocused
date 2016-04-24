package com.mdb.wyn.stayfocused;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Created by Young on 4/2/2016.
 */
public class AppListActivity extends AppCompatActivity {
    AppListAdapter appListAdapter;
    CheckBox selectAll;
    ArrayList<AppListItem> appListItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);
        selectAll= (CheckBox) findViewById(R.id.selectAll);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        appListItems = new ArrayList<>();

        for (String appName : MainActivity.nonSystemAppList) {
            appListItems.add(new AppListItem(appName, false));
        }

        appListAdapter = new AppListAdapter(getApplicationContext(), appListItems);
        recyclerView.setAdapter(appListAdapter);


//this is to add system apps after the pressing the Show System Apps button
        final Button showSystemApps= (Button) findViewById(R.id.systemApps);
        showSystemApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showSystemApps.getText()=="Show System Apps"){
                    Toast.makeText(getApplicationContext(), "Blacklisting system apps can lead to unpredictable results", Toast.LENGTH_LONG).show();
                    showSystemApps.setText("Hide System Apps");
                }
                else {
                    showSystemApps.setText("Show System Apps");
                }
                for (String appName : MainActivity.systemAppList) {
                    appListItems.add(new AppListItem(appName, false));
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
}