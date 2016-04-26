package com.mdb.wyn.stayfocused;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class SettingsFragment extends Fragment {
    private MainActivity activity;
    SwitchCompat aSwitch;
    private FloatingActionButton fab;
    private CustomSharedPreferences prefs;
    private ArrayList<AppListItem> blacklistItems;
    private BlackListAdapter mBlackListAdapter;
    private TextView youHaveNoBlackListWarningTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(Color.WHITE);

        youHaveNoBlackListWarningTextView = (TextView) view.findViewById(R.id.optionalTextView);

        aSwitch= (SwitchCompat) view.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activity.silenceNotifications(isChecked);
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.settingsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        blacklistItems = new ArrayList<>();

        mBlackListAdapter = new BlackListAdapter(activity, blacklistItems);
        setBlackListItems(MainActivity.customPrefs.getSet(CustomSharedPreferences.BLACKLIST_KEY));
        recyclerView.setAdapter(mBlackListAdapter);

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
//                System.out.println("GOT TO ONCLICK");
                Intent blacklistIntent = new Intent(activity.getApplicationContext(), AppListActivity.class);
                activity.startActivityForResult(blacklistIntent, 0);
//                System.out.println("GOT TO INTENT");
            }
        });
        return view;
    }

    public void setBlackListItems(Set<String> list) {
//        System.out.println("got to setblacklistitems");
        ArrayList<AppListItem> blacklistArray = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            youHaveNoBlackListWarningTextView.setVisibility(View.GONE);
            for (String appName : list) {
                blacklistArray.add(new AppListItem(appName, true, null));
            }
        } else {
            youHaveNoBlackListWarningTextView.setVisibility(View.VISIBLE);
        }
        mBlackListAdapter.setBlackListArray(blacklistArray);
    }

    public void changeData(Set<String> list) {
//        System.out.println("got to chahgedata");
        setBlackListItems(list);
        mBlackListAdapter.notifyDataSetChanged();
    }
}
