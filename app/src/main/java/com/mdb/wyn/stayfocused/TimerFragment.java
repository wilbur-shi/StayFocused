package com.mdb.wyn.stayfocused;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class TimerFragment extends Fragment implements TimerInterface {
    private TextView timeSetTextView;
    private TextView startButton;
    private TextView giveUpButton;
    private MainActivity activity;

    public static ArcProgress progressBar;
    public static int mProgressStatus = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.timer_fragment, container, false);
        timeSetTextView = (TextView) view.findViewById(R.id.timeSetTextView);
        startButton = (Button) view.findViewById(R.id.startButton);
        giveUpButton = (Button) view.findViewById(R.id.giveUpTextView);
        setGiveUpButton();
        setStartButton();
        setTimeSetTextView();
        resetButtons();

        progressBar= (ArcProgress) view.findViewById(R.id.arc_progress);

        return view;
    }

    private void setTimeSetTextView() {
        timeSetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.timerIsRunning) {
                    activity.createDialogAndSetTime(0);
                }
            }
        });
    }

    private void setGiveUpButton() {
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
                                activity.handleGiveUpButton("timer");
                            }
                        })
                        .setPositiveButton("No", null)
                        .show();
                // TODO: Failed stuff, you gave up
            }
        });
    }

    private void setStartButton() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!activity.timeLeft.isZero()) {
                    //TODO
                    activity.savedTimeLeftSec=activity.timeLeft.totalTimeInSec();
                    activity.timeLeftSec=activity.timeLeft.totalTimeInSec();
                    activity.handleStartButton("timer");
                    giveUpButton.setVisibility(TextView.VISIBLE);
                    startButton.setVisibility(TextView.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void resetButtons() {
        giveUpButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        try{
            progressBar.setVisibility(View.GONE);
            mProgressStatus=0;
            progressBar.setProgress(0);}
        catch (NullPointerException n){}
        updateTimeTextView();
    }

    @Override
    public void updateTimeTextView() {
        timeSetTextView.setText(activity.timeLeft.toString());
    }


}
