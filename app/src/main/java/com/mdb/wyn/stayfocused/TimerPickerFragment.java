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
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Wilbur on 4/2/2016.
 */
public class TimerPickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int mode;
    private Calendar startingCalendar;
    private Calendar endingCalendar;
    public MainActivity context;

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
            return new TimePickerDialog(getActivity(), this, context.startingTime.getHour(), context.startingTime.getMinute(), true);
        }
        return new TimePickerDialog(getActivity(), this, context.endingTime.getHour(), context.endingTime.getMinute(), false);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        System.out.println("ran nigga" + hourOfDay + " " + mode);

        if (mode==1){
//            startingCalendar= Calendar.getInstance();
            startingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            startingCalendar.set(Calendar.MINUTE, minute);
//            Toast.makeText(context, "Scheduled alarm successfully, will begin at " + startingCalendar.getTime(), Toast.LENGTH_LONG).show();
            if (startingCalendar.getTimeInMillis()< System.currentTimeMillis()){
            startingCalendar.add(Calendar.DATE, 1);}
        }
        else if (mode==2){
//            endingCalendar= Calendar.getInstance();
            endingCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            endingCalendar.set(Calendar.MINUTE, minute);
//            Toast.makeText(context, "calendar vs system: " +  endingCalendar.getTimeInMillis() + ", " + System.currentTimeMillis(), Toast.LENGTH_LONG).show();
            if (endingCalendar.getTimeInMillis()< System.currentTimeMillis()){
            endingCalendar.add(Calendar.DATE, 1);}
        }
        context.setTimeSet(hourOfDay, minute, mode);
    }
}