package com.mdb.wyn.stayfocused;


import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {
    public static final int MAX_NOTIFICATIONS = 3;
    public static Time timeSet = new Time(0, 25, 0);
    private TextView timeSetTextView;
    private Button startButton;
    public CountDownTimer timer;
    private boolean timerCreated = false;
    private int numNotified = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        timeSetTextView = (TextView) findViewById(R.id.timeSetTextView);
        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerCreated) {
                    createTimer(timeSet.totalTimeInMs());
                    timer.start();
                    timerCreated = true;
                }
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
            }
            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Time is up", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void updateTimeTextView() {
        timeSetTextView.setText(timeSet.toString());
    }

    public void setTimeSet(int hours, int minutes) {
        timeSet = new Time(hours, minutes, 0);
        updateTimeTextView();
    }

    private void createDialogAndSetTime() {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
