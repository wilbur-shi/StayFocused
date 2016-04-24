package com.mdb.wyn.stayfocused;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

        TimerPickerFragment.startingCalendar.set(Calendar.HOUR_OF_DAY,12);
        TimerPickerFragment.startingCalendar.set(Calendar.MINUTE,0);

        TimerPickerFragment.endingCalendar.set(Calendar.HOUR_OF_DAY,13);
        TimerPickerFragment.endingCalendar.set(Calendar.MINUTE,0);

        createSetButton();
        setTimeSetTextViews();

        return view;

    }

    private void setTimeSetTextViews() {
        startingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.createDialogAndSetTime(1);
            }
        });
        endingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.createDialogAndSetTime(2);
            }
        });
    }

    private void createSetButton() {
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
                activity.handleGiveUpButton("alarm");

            }
        });
    }
    @Override
    public void resetButtons() {
        cancelButton.setVisibility(View.GONE);
        setButton.setVisibility(View.VISIBLE);
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
