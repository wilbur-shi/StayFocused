package com.mdb.wyn.stayfocused;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Wilbur on 4/2/2016.
 */
public class TimerPickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int mode;
    public static Calendar startingCalendar= Calendar.getInstance();
    public static Calendar endingCalendar= Calendar.getInstance();
    public MainActivity context;

    public void setMode(int num) {
        mode = num;
        //0: timer, 1: alarm start, 2: alarm end
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = (MainActivity) getActivity();
        if (mode==0) {
            return new TimePickerDialog(getActivity(), this, 0, 0, true);
        }
        return new TimePickerDialog(getActivity(), this, 0, 0, false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        System.out.println("ran nigga" + hourOfDay + " " + mode);
        if (mode == 0) {
            context.setTimeSet(hourOfDay, minute, mode);
        }
        if (mode==1){
            startingCalendar= Calendar.getInstance();
            startingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startingCalendar.set(Calendar.MINUTE, minute);
            if (startingCalendar.getTimeInMillis()< System.currentTimeMillis()){
            startingCalendar.roll(Calendar.DATE,1);}
            context.setTimeSet(hourOfDay, minute, mode);

        }
        else if (mode==2){
            endingCalendar= Calendar.getInstance();
            endingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endingCalendar.set(Calendar.MINUTE,minute);
            if (endingCalendar.getTimeInMillis()< System.currentTimeMillis()){
            endingCalendar.roll(Calendar.DATE,1);}
            context.setTimeSet(hourOfDay, minute, mode);
//            context.calculateAlarmTimeLeft();
        }
    }
}