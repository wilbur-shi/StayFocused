package com.mdb.wyn.stayfocused;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class SettingsFragment extends Fragment {
    private MainActivity activity;
    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("GOT TO ONCLICK");
//                activity.createBlackList();
//                Intent blacklistIntent = new Intent(activity.getApplicationContext(), BlackListActivity.class);
//                activity.startActivity(blacklistIntent);
//                System.out.println("GOT TO INTENT");
//            }
//        });

        return view;
    }
}
