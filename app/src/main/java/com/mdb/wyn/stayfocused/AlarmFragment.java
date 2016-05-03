package com.mdb.wyn.stayfocused;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class AlarmFragment extends Fragment implements TimerInterface{
    private MainActivity activity;
    private TextView startingTextView;
    private TextView endingTextView;
    private TextView timeSetTextView;
    private Button setButton;
    private Button cancelButton;
    private Button giveUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.alarm_fragment, container, false);
        startingTextView = (TextView) view.findViewById(R.id.startingTimeText);
        endingTextView = (TextView) view.findViewById(R.id.endingTimeText);
        timeSetTextView= (TextView) view.findViewById(R.id.timeSetTextView);
        setButton = (Button) view.findViewById(R.id.setButton);
        cancelButton= (Button) view.findViewById(R.id.cancelButton);
        giveUpButton= (Button) view.findViewById(R.id.giveUpButton);

        createButtons();
        setTimeSetTextViews();

        return view;

    }

    private void setTimeSetTextViews() {
        startingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.timerIsRunning && !activity.scheduledAlarm) {
                    activity.createDialogAndSetTime(1);

                }
            }
        });
        endingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.timerIsRunning && !activity.scheduledAlarm) {
                    activity.createDialogAndSetTime(2);
                }
            }
        });
        updateTimeTextView();
    }

    private void createButtons() {
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.alarmTimeLeft.isZero()) {
                    activity.scheduleAlarm();
                    cancelButton.setVisibility(View.VISIBLE);
                    setButton.setVisibility(View.GONE);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Alarm cancelled", Toast.LENGTH_LONG).show();
                activity.handleCancelAlarm();
            }
        });
        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int style;
                if (Build.VERSION.SDK_INT > 20) {
                    style = R.style.MyAlertDialogStyle;
                } else {
                    style = 0;
                }
                new AlertDialog.Builder(activity, style)
                        .setIcon(R.drawable.ic_block_24dp)
                        .setTitle("RESET ALL VALUES")
                        .setMessage("Are you sure you want to give up?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.handleGiveUpButton("alarm");
                            }
                        })
                        .setPositiveButton("No", null)
                        .show();
            }
        });
    }

    @Override
    public void resetButtons() {
        if (activity.isAlarmMode && activity.timerIsRunning) {
            giveUpButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.GONE);
            setButton.setVisibility(View.GONE);
        } else {
            giveUpButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            setButton.setVisibility(View.VISIBLE);
        }
        updateTimeTextView();
    }
    @Override
    public void updateTimeTextView() {
        //difference text view

        startingTextView.setText(activity.startingTime.toString());
        endingTextView.setText(activity.endingTime.toString());
        timeSetTextView.setText(activity.alarmTimeLeft.toString());



    }
}
