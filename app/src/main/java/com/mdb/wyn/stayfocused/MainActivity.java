package com.mdb.wyn.stayfocused;



import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
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
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public Time timeLeft;
    public Time startingTime;
    public Time endingTime;
    public static Time alarmTimeLeft;
    public static CountDownTimer timer;
    public static boolean timerCreated = false;
    private ViewPager viewPager;
    private static MyPagerAdapter adapter;
    private TabLayout tabLayout;
    public static boolean isAlarmMode = false;
    public static boolean isBlockingOpen = false;
    public boolean timerIsRunning = false;
    private Context context;
    private ActivityManager activityManager;

    // Alarm stuff variables
    private AlarmManager alarmManager;
    private PendingIntent start;
    private PendingIntent end;
    public Calendar startingCalendar = Calendar.getInstance();
    public Calendar endingCalendar = Calendar.getInstance();

    public static ArrayList<AppListItem> nonSystemAppList;
    public static ArrayList<AppListItem> systemAppList;
    public static Set<String> blackList;
//    public boolean timerIsRunning = false;
    public static CustomSharedPreferences customPrefs;
    BroadcastReceiver closeAppBroadcastReceiver;

    public int savedTimeLeftSec;
    public int timeLeftSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        context = getApplicationContext();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        setupTimes();
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setCurrentItem(3);

        closeAppBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    handleGiveUpButton("timer");
                    finish();
                    System.out.println("received broadcast");
                    System.exit(0);
                }
            }
        };
        registerReceiver(closeAppBroadcastReceiver, new IntentFilter("finish_activity"));
        registerReceiver(alarmReceiver, new IntentFilter("start_alarm"));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        customPrefs = new CustomSharedPreferences(prefs);

        createAppList();
        setCalendarsHourMinToTimes();

    }
    BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("start_alarm")) {
                handleStartButton("alarm");
                System.out.println("got to reeive, is going to start alarm button");
            }
        }
    };

    private void setupTimes() {
        final Calendar rightNow = Calendar.getInstance();
        timeLeft = new Time(0, 25, 0, "timer");
        System.out.println(rightNow.get(Calendar.HOUR_OF_DAY) + " and " + rightNow.get(Calendar.MINUTE));
        startingTime= new Time(rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE),0,"alarm");
        endingTime= new Time(rightNow.get(Calendar.HOUR_OF_DAY) + 1, rightNow.get(Calendar.MINUTE), 0, "alarm");
        alarmTimeLeft= new Time(1,0,0,"timer");
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

    public void calculateAlarmTimeLeft() {
//            int hours= endingCalendar.HOUR_OF_DAY-startingCalendar.HOUR_OF_DAY;
    //    int min= endingCalendar.MINUTE-startingCalendar.MINUTE;

            int hours = endingTime.getHour() - startingTime.getHour();
            int min = endingTime.getMinute() - startingTime.getMinute();

        if (hours < 0) {
//            endingCalendar.add(Calendar.DATE, 1);
            hours = endingTime.getHour() + 24 - startingTime.getHour();
        }
            if (min<0){
                hours= hours-1;
                min=min+60;
            }
            System.out.println("Hours " + hours);
            System.out.println("Minutes " + min);

            if ((hours==0 && min<=0)){
                alarmTimeLeft= new Time(0,0,0,"timer");
            } else {
                alarmTimeLeft= new Time(hours,min,0,"timer");
            }
    }
//STATIC?
    private void createTimer(long ms) {
        timerIsRunning = true;
        updateBlackList();
        createTimerStartedNotification();
        timer = new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minLeft = Time.msToMinutes(millisUntilFinished);
                if (minLeft <= 5) {
                    //Toast.makeText(getApplicationContext(), String.format("%d min left", minLeft), Toast.LENGTH_SHORT).show();
                }
                if (isAlarmMode) {
                    alarmTimeLeft.addSecond(-1);
                } else {
                    timeLeft.addSecond(-1);
                    timeLeftSec=timeLeftSec-1;
                    TimerFragment.mProgressStatus= 100*(savedTimeLeftSec-timeLeftSec)/savedTimeLeftSec;
                    TimerFragment.progressBar.setProgress(TimerFragment.mProgressStatus);
                }
                // Checks running tasks, core functionality
                if (!isBlockingOpen) {
                    checkForTasks();
                }
                updateFragmentTextViews();
            }
            @Override
            public void onFinish() {
                if (isAlarmMode) {
                    alarmTimeLeft.reset();
                } else {
                    timeLeft.reset();
                }
                updateFragmentTextViews();
                Toast.makeText(context, "Congratulations, you finished your work! Yay!", Toast.LENGTH_SHORT).show();
                MediaPlayer mMediaPlayer = MediaPlayer.create(context, R.raw.tada_sound);
                for (int i = 0; i < 5; i++) {
                    mMediaPlayer.start();

                }
                // TODO: Show some notification or something and play a congratulatory noise
                timerIsRunning = false;
                setupTimes();
                resetFragmentButtons();
                cancelNotification(getApplicationContext(), 001);
            }

        };
    }

    public void updateBlackList() {
        blackList = customPrefs.getSet(CustomSharedPreferences.BLACKLIST_KEY);
        try {
            SettingsFragment frag = (SettingsFragment) adapter.getCurrentFragment();
            frag.changeData(blackList);
        } catch (ClassCastException e){
            System.out.println("weird error not on settings fragment");
        }
    }

//    STATIC?
    private void checkForTasks() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        PackageManager pm2= context.getPackageManager();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            try {
//                System.out.println("TRY CATCH STATEMENT");
                CharSequence appName = pm2.getApplicationLabel(pm2.getApplicationInfo(appProcess.processName, PackageManager.GET_META_DATA));
                if (!appName.equals("Kimojo")) {
//                    System.out.println(blackList + " also the pref for appname is: " + customPrefs.getCheckedPref(appName.toString()));
                }
                if (!appName.equals("Kimojo") && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && blackList.contains(appName) && customPrefs.getCheckedPref(appName.toString())) {
                    System.out.println("LOOK HERE"+ appName);

                    Intent blockingIntent= new Intent(context, BlockingActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, blockingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
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
                System.out.println("not detecting stuff");
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
//STATIC?
        try {
            TimerInterface Fragment = (TimerInterface) adapter.getCurrentFragment();
            if (Fragment != null) {
                Fragment.updateTimeTextView();
            }
        } catch (ClassCastException cce) {
            System.out.println("Wrong stufff");
        }
    }

    public void setTimeSet(int hours, int minutes, int mode) {
        System.out.println("then the settimeset");
        if (mode==0){
        timeLeft = new Time(hours, minutes, 0, "timer");}
        else if (mode==1){
            startingTime= new Time(hours,minutes,0,"alarm");
            calculateAlarmTimeLeft();
        }
        else if (mode==2){
            endingTime= new Time(hours,minutes,0,"alarm");
            calculateAlarmTimeLeft();
        }

        updateFragmentTextViews();
    }

    public void createDialogAndSetTime(int mode) {
//        TimePicker timePicker = new TimePicker(getApplicationContext());
        TimerPickerFragment newFragment = new TimerPickerFragment();
        newFragment.setMode(mode);
        newFragment.show(getSupportFragmentManager(), "timerPicker");
    }

//    STATIC??
    public void handleStartButton(String type) {
        if (type.equals("timer")&& !timerCreated && !timeLeft.isZero() && !timerIsRunning) {
            isAlarmMode = false;
            createTimer(timeLeft.totalTimeInMs());
            timer.start();
            timerCreated = true;
        }
        else if (type.equals("alarm")&& !timerCreated && !alarmTimeLeft.isZero() && !timerIsRunning) {
            isAlarmMode = true;
            createTimer(alarmTimeLeft.totalTimeInMs());
            Toast.makeText(this, "Starting Alarm", Toast.LENGTH_LONG).show();
            timer.start();
            timerCreated = true;
            resetFragmentButtons();
        }
    }

    public void handleGiveUpButton(String type) {
        timerCreated = false;
        timer.cancel();
        timerIsRunning = false;
        setupTimes();
//        if (type.equals("timer")) {
//            timeLeft.reset();
//        }
//        else{
//            alarmTimeLeft.reset();
//        }
        resetFragmentButtons();
        cancelNotification(getApplicationContext(), 001);
    }

    private void resetFragmentButtons() {
        try {
            TimerInterface timerFragment = (TimerInterface) adapter.getCurrentFragment();
            timerFragment.resetButtons();
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
        nonSystemAppList = new ArrayList<>();
        try {
            for (PackageInfo pi : list) {
                ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);
                String currAppName = pm.getApplicationLabel(ai).toString();
                Drawable icon = pm.getApplicationIcon(ai);
                int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
                if ((ai.flags & mask) == 0 && ! currAppName.equals("Kimojo")) {
//                    System.out.println(icon==null);
                    appNames.add(currAppName);
                    nonSystemAppList.add(new AppListItem(currAppName, false, icon));
                }
                else if (! currAppName.equals("Kimojo")){
                    systemApps.add(currAppName);
                }
//                System.out.println("YOUNG JUST ADDED"+currAppName);

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
        unregisterReceiver(alarmReceiver);
        if (closeAppBroadcastReceiver != null) {
            unregisterReceiver(closeAppBroadcastReceiver);
            System.out.println("unregistered receiver");
        }
        cancelNotification(getApplicationContext(), 001);

//        if (!timeLeft.isZero() && timerIsRunning){
            new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Closing the app will reset the timer.")
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handleGiveUpButton("fix this later");
                            finish();
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("No", null)
                    .show();
//        }
//        else{
//            finish();
//            System.exit(0);
//        }

        //else if the timer is not running yet, do not reset the timer

    }

    public void scheduleAlarm() {

        Intent startingIntent = new Intent(this, AlarmReceiver.class);
        startingIntent.putExtra("requestcode", 1);
        Intent endingIntent= new Intent(this, AlarmReceiver.class);
        endingIntent.putExtra("requestcode", 2);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //start alarm stuff
        start = PendingIntent.getBroadcast(this,1,  startingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        setCalendarsHourMinToTimes();
        final Calendar rightNow = Calendar.getInstance();
        if (startingCalendar.get(Calendar.HOUR_OF_DAY) < rightNow.get(Calendar.HOUR_OF_DAY) || startingCalendar.get(Calendar.MINUTE) < rightNow.get(Calendar.MINUTE)) {
            startingCalendar.add(Calendar.DATE, 1);
            endingCalendar.add(Calendar.DATE, 1);
        }
        if (endingCalendar.get(Calendar.HOUR_OF_DAY) < startingCalendar.get(Calendar.HOUR_OF_DAY)) {
            endingCalendar.add(Calendar.DATE, 1);
        }
//        setCalendarsHourMinToTimes();

        long startTime = Time.minutesToMs(Time.msToMinutes(startingCalendar.getTimeInMillis()));
        long endTime = Time.minutesToMs(Time.msToMinutes(endingCalendar.getTimeInMillis()));
        Date date = new Date(startTime);
        Date endDate = new Date(endTime);
        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, start);
        Toast.makeText(this, "Scheduled alarm successfully, will begin at " + date, Toast.LENGTH_LONG).show();

        //end alarm stuff
        end = PendingIntent.getBroadcast(this, 2, endingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, endTime, end);
        Toast.makeText(this, "Alarm will end at " + endDate, Toast.LENGTH_LONG).show();

    }

    private void setCalendarsHourMinToTimes() {
        startingCalendar.setTime(Calendar.getInstance().getTime());
        endingCalendar.setTime(Calendar.getInstance().getTime());
        startingCalendar.set(Calendar.HOUR_OF_DAY, startingTime.getHour());
        startingCalendar.set(Calendar.MINUTE, startingTime.getMinute());
        endingCalendar.set(Calendar.HOUR_OF_DAY, endingTime.getHour());
        endingCalendar.set(Calendar.MINUTE, endingTime.getMinute());
    }

    public void handleCancelAlarm() {
        if (start != null && end != null) {
            alarmManager.cancel(start);
            alarmManager.cancel(end);
        }
        resetFragmentButtons();
    }



    // Create notification for timer
    private void createTimerStartedNotification() {
        String time;
        if (isAlarmMode) {
            calculateAlarmTimeLeft();
            time = alarmTimeLeft.toString();
        } else {
            time = timeLeft.toString();
        }
            int icon;
            if (Build.VERSION.SDK_INT < 20) {
                icon = R.mipmap.ic_launcher;
            } else {
                icon = R.drawable.ic_av_timer_24dp;
            }
            Intent openMainActivity = new Intent(this, MainActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, openMainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(icon)
                        .setContentTitle("Timer started")
                        .setContentText("Timer set for " + time)
                        .setAutoCancel(true)
//                    .setOngoing(true)
                    .addAction(0, "Check time", resultPendingIntent)
                        .addAction(0, "Give up", PendingIntent.getBroadcast(this, 5, new Intent("finish_activity"), PendingIntent.FLAG_UPDATE_CURRENT))
                        .setDefaults(Notification.DEFAULT_ALL);
//            mBuilder.setContentIntent(resultPendingIntent);

            Notification notif = mBuilder.build();
            notif.flags = Notification.FLAG_ONGOING_EVENT;

            int mNotificationId = 001;
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
//        }
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(notifyId);
        nMgr.cancelAll();
    }

    public void silenceNotifications(boolean isChecked) {
        final AudioManager mode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = mode.getRingerMode();
        if (ringerMode != AudioManager.RINGER_MODE_SILENT) {
            customPrefs.setRinger(ringerMode);
        }
        if (isChecked) {
            mode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            mode.setRingerMode(customPrefs.getRingerMode());
        }
        //save this shit to sharedprefs


    }
}
