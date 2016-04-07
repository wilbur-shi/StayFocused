package com.mdb.wyn.stayfocused;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import java.util.ArrayList;
import java.util.List;

public class TimerActivity extends AppCompatActivity {
  // public static final int MAX_NOTIFICATIONS = 3;
    public static Time timeSet = new Time(0, 25, 0);
    private TextView timeSetTextView;
    private TextView startButton;
    private TextView giveUpButton;
    public CountDownTimer timer;
    private boolean timerCreated = false;
   // private int numNotified = 0;
  //  private TimePicker timePicker;
    private ProgressBar pb;
    static ArrayList<String> nonSystemBlackList;
    static ArrayList<String> systemBlackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timeSetTextView = (TextView) findViewById(R.id.timeSetTextView);
        startButton = (TextView) findViewById(R.id.startButton);
        giveUpButton = (TextView) findViewById(R.id.giveUpTextView);
        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TimerActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Reset values")
                        .setMessage("Are you sure you want to reset values?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timer.cancel();
                                resetTextViews();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                // TODO: Failed stuff, you gave up
            }
        });

        pb = (ProgressBar) findViewById(R.id.countdownTimerCircle);

        Animation an = new RotateAnimation(0.0f, 90.0f, 250f, 273f);
        an.setFillAfter(true);
        pb.startAnimation(an);

        resetTextViews();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerCreated && !timeSet.isZero()) {
                    createTimer(timeSet.totalTimeInMs());
                    timer.start();
                    timerCreated = true;
                    giveUpButton.setVisibility(TextView.VISIBLE);
                    startButton.setVisibility(TextView.GONE);
                }
//                test();
            }
        });

        timeSetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogAndSetTime();
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }

    @Override
    protected void onDestroy() {
//        check if the timer is already running here
        super.onDestroy();
        new AlertDialog.Builder(TimerActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Closing the app will reset the timer.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", null)
                .show();
        //else if the timer is not running yet, do not reset the timer

    }


    public void blackListCreator() {
        nonSystemBlackList = new ArrayList<>();
        systemBlackList= new ArrayList<>();
        PackageManager pm = getApplicationContext().getPackageManager();
        List<PackageInfo> list = pm.getInstalledPackages(0);
        try {
            for (PackageInfo pi : list) {
                ApplicationInfo ai = pm.getApplicationInfo(pi.packageName, 0);
                String currAppName = pm.getApplicationLabel(ai).toString();
                if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    nonSystemBlackList.add(currAppName);
//                    System.out.println("into for loop");
                }
                else {
                    systemBlackList.add(currAppName);
                    System.out.println("YOUNG JUST ADDED"+currAppName);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("Error: " + e);
        }
        Intent blacklistIntent = new Intent(getApplicationContext(), BlackListActivity.class);
        startActivity(blacklistIntent);
        //System.out.println("after intent");
    }


    private void createTimer(long ms) {
        timer = new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minLeft = Time.msToMinutes(millisUntilFinished);
                if (minLeft <= 5) {
                    //Toast.makeText(getApplicationContext(), String.format("%d min left", minLeft), Toast.LENGTH_SHORT).show();
                }
                timeSet.addSecond(-1);
                updateTimeTextView();
                pb.setProgress((int)minLeft);
            }
            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time is up", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void resetTextViews() {
        giveUpButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        timeSet.reset();
        timerCreated = false;
        timeSetTextView.setText(timeSet.toString());
    }

    private void updateTimeTextView() {
        timeSetTextView.setText(timeSet.toString());
    }

    public void setTimeSet(int hours, int minutes) {
        timeSet = new Time(hours, minutes, 0);
        updateTimeTextView();
    }

    private void createDialogAndSetTime() {
//        TimePicker timePicker = new TimePicker(getApplicationContext());
        DialogFragment newFragment = new TimerPickerFragment();
        newFragment.show(getSupportFragmentManager(), "timerPicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            blackListCreator();
        }

        return super.onOptionsItemSelected(item);
    }
}
