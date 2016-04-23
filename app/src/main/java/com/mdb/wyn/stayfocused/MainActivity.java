package com.mdb.wyn.stayfocused;



import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Time timeLeft = new Time(0, 25, 0);
    public CountDownTimer timer;
    public boolean timerCreated = false;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private TabLayout tabLayout;
    public static boolean isBlockingOpen = false;
    public static ArrayList<String> nonSystemBlackList;
    public static ArrayList<String> systemBlackList;

    BroadcastReceiver closeAppBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        setSupportActionBar(myToolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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
                }
            }
        };
        registerReceiver(closeAppBroadcastReceiver, new IntentFilter("finish_activity"));

    }

    private void createTimer(long ms) {
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
            }
        };
    }

    private void checkForTasks() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        PackageManager pm2= getApplicationContext().getPackageManager();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            try {
                System.out.println("TRY CATCH STATEMENT");
                CharSequence appName = pm2.getApplicationLabel(pm2.getApplicationInfo(appProcess.processName, PackageManager.GET_META_DATA));
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && nonSystemBlackList.contains(appName)) {
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

    public void createBlackList() {
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
    }

    @Override
    public void onBackPressed() {
        if (!timeLeft.isZero()){
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
    }





    @Override
    protected void onDestroy() {
//        check if the timer is already running here
        super.onDestroy();
        if (closeAppBroadcastReceiver != null) {
            unregisterReceiver(closeAppBroadcastReceiver);
            System.out.println("unregistered receiver");
        }
        //else if the timer is not running yet, do not reset the timer

    }
}
