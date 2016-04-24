package com.mdb.wyn.stayfocused;



import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public Time timeLeft = new Time(0, 25, 0);
    public CountDownTimer timer;
    public boolean timerCreated = false;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private TabLayout tabLayout;
    public static boolean isBlockingOpen = false;
    public static ArrayList<String> nonSystemAppList;
    public static ArrayList<String> systemAppList;
    public static Set<String> blackList;
    public boolean timerIsRunning = false;
    public static CustomSharedPreferences customPrefs;

    BroadcastReceiver closeAppBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        setupTabs();

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                if (tab.getPosition() == 3) {
//                    SettingsFragment settings = (SettingsFragment) adapter.getCurrentFragment();
//                    settings.changeData();
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        closeAppBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                    System.out.println("received broadcast");
                    System.exit(0);
                }
            }
        };
        registerReceiver(closeAppBroadcastReceiver, new IntentFilter("finish_activity"));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customPrefs = new CustomSharedPreferences(prefs);

        if (customPrefs.getSet(CustomSharedPreferences.APPNAMES_KEY) == null) {
            createAppList();
        }

    }

    private void setupTabs() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Timer");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_av_timer_24dp, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabOne));

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Alarm");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_alarm_24dp, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabTwo));

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Settings");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_settings_black_24dp, 0, 0);
        tabLayout.addTab(tabLayout.newTab().setCustomView(tabThree));

    }

    private void createTimer(long ms) {
        timerIsRunning = true;
        updateBlackList();
        timer = new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minLeft = Time.msToMinutes(millisUntilFinished);
                if (minLeft <= 5) {
                    //Toast.makeText(getApplicationContext(), String.format("%d min left", minLeft), Toast.LENGTH_SHORT).show();
                }
                timeLeft.addSecond(-1);
                // Checks running tasks, core functionality
                if (!isBlockingOpen) {
                    checkForTasks();
                }
                updateFragmentTextViews();
            }
            @Override
            public void onFinish() {
                timeLeft.addSecond(-1);
                updateFragmentTextViews();
                Toast.makeText(getApplicationContext(), "Time is up", Toast.LENGTH_SHORT).show();
                timerIsRunning = false;
            }
        };
    }

    public void updateBlackList() {
        blackList = customPrefs.getSet(CustomSharedPreferences.BLACKLIST_KEY);
//        blackList = new ArrayList<>();
//        if (blacklistnames != null) {
//            for (String name : blacklistnames) {
//                blackList.add(name);
//            }
//        }
        try {
            SettingsFragment frag = (SettingsFragment) adapter.getCurrentFragment();
            frag.changeData(blackList);
        } catch (ClassCastException e){
            System.out.println("weird error not on settings fragment");
        }
    }

    private void checkForTasks() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        PackageManager pm2= getApplicationContext().getPackageManager();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            try {
//                System.out.println("TRY CATCH STATEMENT");
                CharSequence appName = pm2.getApplicationLabel(pm2.getApplicationInfo(appProcess.processName, PackageManager.GET_META_DATA));
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && blackList.contains(appName)) {
                    System.out.println("LOOK HERE"+ appName);

                    Intent blockingIntent= new Intent(getApplicationContext(),BlockingActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, blockingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    System.out.println("GOT TO PENDINGINTENT");
                    try {
                        // Perform the operation associated with our pendingIntent
                        pendingIntent.send();
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }

            catch (PackageManager.NameNotFoundException e){
                System.out.println("Error: " + e);

            }
            catch (NullPointerException nullPointerException){
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            System.out.println("got to settings frag on activityresult");
            updateBlackList();
        }

    }

    private void updateFragmentTextViews() {
        try {
            TimerInterface timerFragment = (TimerInterface) adapter.getCurrentFragment();
            timerFragment.updateTimeTextView();
        } catch (ClassCastException cce) {
            System.out.println("Wrong stufff");
        }
    }

    public void setTimeSet(int hours, int minutes) {
        timeLeft = new Time(hours, minutes, 0);
        updateFragmentTextViews();
    }

    public void createDialogAndSetTime() {
//        TimePicker timePicker = new TimePicker(getApplicationContext());
        DialogFragment newFragment = new TimerPickerFragment();
        newFragment.show(getSupportFragmentManager(), "timerPicker");
    }

    public void handleStartButton() {
        if (!timerCreated && !timeLeft.isZero()) {
            createTimer(timeLeft.totalTimeInMs());
            timer.start();
            timerCreated = true;
        }
    }

    public void handleGiveUpButton() {
        timer.cancel();
        timeLeft.reset();
        timerCreated = false;
        try {
            TimerInterface timerFragment = (TimerInterface) adapter.getCurrentFragment();
            timerFragment.resetTextViews();
        } catch (ClassCastException cce) {
            System.out.println("Wrong stufff");
        }
    }

    public void createAppList() {
        System.out.println("GOT TO METHOD");
//        nonSystemAppList = new ArrayList<>();
//        systemAppList = new ArrayList<>();
        HashSet<String> appNames = new HashSet<>();
        HashSet<String> systemApps = new HashSet<>();
        PackageManager pm = getApplicationContext().getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(0);
        try {
            for (PackageInfo pi : list) {
                ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);
                String currAppName = pm.getApplicationLabel(ai).toString();
                if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !currAppName.equals("StayFocused")) {
                    appNames.add(currAppName);
                }
                else if (!currAppName.equals("StayFocused")){
                    systemApps.add(currAppName);
                    System.out.println("YOUNG JUST ADDED"+currAppName);
                }
            }
            customPrefs.saveList(CustomSharedPreferences.APPNAMES_KEY, appNames);
            customPrefs.saveList(CustomSharedPreferences.SYSTEMAPPS_KEY, systemApps);
//            updateBlackList();
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    protected void onDestroy() {
//        check if the timer is already running here
        super.onDestroy();
        if (!timeLeft.isZero() && !timerIsRunning){
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Closing the app will reset the timer.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleGiveUpButton();
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        }
        else{
            finish();
            System.exit(0);
        }
        if (closeAppBroadcastReceiver != null) {
            unregisterReceiver(closeAppBroadcastReceiver);
            System.out.println("unregistered receiver");
        }
        //else if the timer is not running yet, do not reset the timer

    }
}
