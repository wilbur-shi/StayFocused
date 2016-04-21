package com.mdb.wyn.stayfocused;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import java.util.ArrayList;

/**
 * Created by Young on 4/2/2016.
 */
public class BlackListActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        System.out.println("after intent");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<BlackListItem> blackListItems = new ArrayList<>();

//        System.out.println(TimerActivity.Blacklist.size());
//        for (String appName : TimerActivity.Blacklist) {
//            blackListItems.add(new BlackListItem(appName,false));
//            Log.i("ArrayList", appName);
//            System.out.println("into for for blacklist method");
//        }

        BlackListAdapter blackListAdapter = new BlackListAdapter(getApplicationContext(), blackListItems);
        recyclerView.setAdapter(blackListAdapter);
    }
}