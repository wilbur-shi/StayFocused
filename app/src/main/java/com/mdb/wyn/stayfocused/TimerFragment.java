package com.mdb.wyn.stayfocused;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class TimerFragment extends Fragment implements TimerInterface {
    private TextView timeSetTextView;
    private TextView startButton;
    private TextView giveUpButton;
    private MainActivity activity;

    public static void newInstance(/* TODO: some other arguments */) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        View view = inflater.inflate(R.layout.timer_fragment, container, false);
        timeSetTextView = (TextView) view.findViewById(R.id.timeSetTextView);
        startButton = (TextView) view.findViewById(R.id.startButton);
        giveUpButton = (TextView) view.findViewById(R.id.giveUpTextView);
        setGiveUpButton();
        setStartButton();
        setTimeSetTextView();
        resetTextViews();

        return view;
    }

    private void setTimeSetTextView() {
        timeSetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.createDialogAndSetTime();
            }
        });
    }

    private void setGiveUpButton() {
        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Reset values")
                        .setMessage("Are you sure you want to reset values?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.handleGiveUpButton();
                            }
                        })
                        .setNegativeButton("No", null)
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
                    activity.handleStartButton();
                    giveUpButton.setVisibility(TextView.VISIBLE);
                    startButton.setVisibility(TextView.GONE);
                }
            }
        });
    }

    @Override
    public void resetTextViews() {
        giveUpButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        updateTimeTextView();
    }

    @Override
    public void updateTimeTextView() {
        timeSetTextView.setText(activity.timeLeft.toString());
    }


}
