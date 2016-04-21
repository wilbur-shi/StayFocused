package com.mdb.wyn.stayfocused;



import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public Time timeLeft = new Time(0, 25, 0);
    public CountDownTimer timer;
    public boolean timerCreated = false;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;
    private TabLayout tabLayout;
    public static boolean isBlockingOpen = false;


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
    }

}
