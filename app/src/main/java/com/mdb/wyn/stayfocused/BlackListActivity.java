package com.mdb.wyn.stayfocused;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


import java.util.ArrayList;

/**
 * Created by Young on 4/2/2016.
 */
public class BlackListActivity extends AppCompatActivity {
    BlackListAdapter blackListAdapter;
    CheckBox selectAll;
    ArrayList<BlackListItem> blackListItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);
        selectAll= (CheckBox) findViewById(R.id.selectAll);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        blackListItems = new ArrayList<>();

        for (String appName : MainActivity.nonSystemBlackList) {
            blackListItems.add(new BlackListItem(appName, false));
        }

        blackListAdapter = new BlackListAdapter(getApplicationContext(), blackListItems);
        recyclerView.setAdapter(blackListAdapter);


//this is to add system apps after the pressing the Show System Apps button
        final Button showSystemApps= (Button) findViewById(R.id.systemApps);
        showSystemApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showSystemApps.getText()=="Show System Apps"){
                    showSystemApps.setText("Hide System Apps");
                }
                else {
                    showSystemApps.setText("Show System Apps");
                }
                for (String appName : MainActivity.systemBlackList) {
                    blackListItems.add(new BlackListItem(appName, false));
                }
            }
        });
    }

    public void onCheckBoxClicked(View view) {
        //this is for the select all method
        boolean checked = ((CheckBox) view).isChecked();
        for (BlackListItem eachItem: BlackListAdapter.blackListArray){
            eachItem.isBlacklisted= checked;
            blackListAdapter.notifyDataSetChanged();
        }

    }
}