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
        if (mode==1){
            startingCalendar= Calendar.getInstance();
            startingCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            startingCalendar.set(Calendar.MINUTE, minute);
            if (startingCalendar.getTimeInMillis()< System.currentTimeMillis()){
            startingCalendar.roll(Calendar.DATE,1);}
            ((MainActivity)getActivity()).setTimeSet(hourOfDay, minute, mode);

//            calculatealarmtimeleft();

        }
        else if (mode==2){
            endingCalendar= Calendar.getInstance();
            endingCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            endingCalendar.set(Calendar.MINUTE,minute);
            if (endingCalendar.getTimeInMillis()< System.currentTimeMillis()){
            endingCalendar.roll(Calendar.DATE,1);}
            ((MainActivity)getActivity()).setTimeSet(hourOfDay, minute, mode);
            calculatealarmtimeleft();
        }
//        ((TimerActivity)getActivity()).setTimeSet(hourOfDay, minute);
    }




 private void calculatealarmtimeleft() {
//    int hours= endingCalendar.HOUR_OF_DAY-startingCalendar.HOUR_OF_DAY;
//    int min= endingCalendar.MINUTE-startingCalendar.MINUTE;

     int hours = context.endingTime.getHour() - context.startingTime.getHour();
     int min = context.endingTime.getMinute() - context.startingTime.getMinute();

     if (min<0){
         hours= hours-1;
         min=min+60;
     }
     System.out.println("Hours " + hours);
     System.out.println("Minutes " + min);

     if (hours<0 || (hours==0 && min<=0)){
         MainActivity.alarmTimeLeft= new Time(0,0,0,"timer");
              }
     else{MainActivity.alarmTimeLeft= new Time(hours,min,0,"timer");
     }
    }

}