package com.mdb.wyn.stayfocused;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Wilbur on 4/2/2016.
 */
public class TimerPickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int mode;
    private Calendar startingCalendar;
    private Calendar endingCalendar;
    public MainActivity context;
    public TimePickerDialog picker;

    public void setMode(int num) {
        mode = num;
        //0: timer, 1: alarm start, 2: alarm end
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        startingCalendar = context.startingCalendar;
        endingCalendar = context.endingCalendar;
        if (mode==0) {
            picker = new TimePickerDialog(getActivity(), this, context.timeLeft.getHour(), context.timeLeft.getMinute(), true);
        } else if (mode == 1) {
            picker = new TimePickerDialog(getActivity(), this, context.startingTime.getHour(), context.startingTime.getMinute(), false);

        } else {
            picker = new TimePickerDialog(getActivity(), this, context.endingTime.getHour(), context.endingTime.getMinute(), false);
        }
        TextView tv = new TextView(context);
        tv.setTextSize(17);
        tv.setPadding(40, 15, 15, 15);
        tv.setText("Set Time");
        picker.setCustomTitle(tv);
        return picker;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
//        picker.setTitle("Set Time");
//        System.out.println("ran nigga" + hourOfDay + " " + mode);
        if (mode==1){
//            startingCalendar= Calendar.getInstance();
            startingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startingCalendar.set(Calendar.MINUTE, minute);
//            Toast.makeText(context, "Scheduled alarm successfully, will begin at " + startingCalendar.getTime(), Toast.LENGTH_LONG).show();
        }
        else if (mode==2){
//            endingCalendar= Calendar.getInstance();
            endingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endingCalendar.set(Calendar.MINUTE, minute);
//            Toast.makeText(context, "calendar vs system: " + endingCalendar.getTime() + ", " + new Date(rightNow.getTimeInMillis()), Toast.LENGTH_LONG).show();
            System.out.println("first the toast");
        }
        context.setTimeSet(hourOfDay, minute, mode);
    }
}