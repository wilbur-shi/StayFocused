package com.mdb.wyn.stayfocused;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class SettingsFragment extends Fragment {
    private MainActivity activity;
    FloatingActionButton fab;
    Switch aSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

        aSwitch= (Switch) view.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             if (isChecked){
                 activity.silenceNotifications();
             }
            else {
                 //call the variable from shared prefs, change it back to the original settings
             }
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

//        ArrayList<AppListItem> stuff = new ArrayList<>();
//        for (String appName : MainActivity.blackList) {
//            stuff.add(new AppListItem(appName, false));
//        }
        // TODO: Figure out shared preferences and all as well as select all
//        recyclerView.setAdapter(new BlackListAdapter(activity, stuff));

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GOT TO ONCLICK");
                activity.createBlackList();
                Intent blacklistIntent = new Intent(activity.getApplicationContext(), AppListActivity.class);
                activity.startActivity(blacklistIntent);
                System.out.println("GOT TO INTENT");
            }
        });

        return view;
    }
}
