package com.mdb.wyn.stayfocused;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    static ArrayList<String> nonSystemBlackList;
    static ArrayList<String> systemBlackList;
    ImageButton blackListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferenceScreenFragment()).commit();
        System.out.println("GOT TO BUTTON");
        blackListButton= (ImageButton) findViewById(R.id.blackListButton);

        blackListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("GOT TO ONCLICK");
                blackListCreator();

            }
        });
    }
//    public static class PreferenceScreenFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.preferences);
//
//
//
//        }
//    }


    public void blackListCreator() {
        System.out.println("GOT TO METHOD");
        nonSystemBlackList = new ArrayList<>();
        systemBlackList= new ArrayList<>();
        PackageManager pm = getApplicationContext().getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(0);
        try {
            for (PackageInfo pi : list) {
                ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);
                String currAppName = pm.getApplicationLabel(ai).toString();
                if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && ! currAppName.equals("StayFocused")) {
                    nonSystemBlackList.add(currAppName);
                }
                else if (! currAppName.equals("StayFocused")){
                    systemBlackList.add(currAppName);
                    System.out.println("YOUNG JUST ADDED"+currAppName);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("Error: " + e);
        }
        System.out.println("GOT TO INTENT");

        Intent blacklistIntent = new Intent(getApplicationContext(), BlackListActivity.class);
        startActivity(blacklistIntent);
        //System.out.println("after intent");
    }

}