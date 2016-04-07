package com.mdb.wyn.stayfocused;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.CheckBox;


import java.util.ArrayList;

/**
 * Created by Young on 4/2/2016.
 */
public class BlackListActivity extends AppCompatActivity {
    BlackListAdapter blackListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<BlackListItem> blackListItems = new ArrayList<>();

        for (String appName : TimerActivity.nonSystemBlackList) {
            blackListItems.add(new BlackListItem(appName, false));
        }

        blackListAdapter = new BlackListAdapter(getApplicationContext(), blackListItems);
        recyclerView.setAdapter(blackListAdapter);
    }

    public void onCheckBoxClicked(View view) {
        //this is for the select all method
        boolean checked = ((CheckBox) view).isChecked();
        for (BlackListItem eachItem: BlackListAdapter.blackListArray){
            eachItem.isBlacklisted= checked;
//            blackListAdapter.notifyDataSetChanged();
        }

    }
}