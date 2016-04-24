package com.mdb.wyn.stayfocused;

/**
 * Created by Wilbur on 4/2/2016.
 */
public class Time {
    private int hour;
    private int minute;
    private int second;
    private String type;

    public Time(int h, int m, int s, String t) {
        hour = h;
        minute = m;
        second = s;
        type= t;
    }

    public static long minutesToMs(double min) {
        return (long) (min * 60000);
    }
    public static int msToMinutes(long ms) {
        return (int) (ms / 60000);
    }

    public boolean isZero() {
        return hour == 0 && minute == 0 && second == 0;
    }
    public void reset() {
        hour = 0;
        minute = 0;
        second = 0;
    }
    public void setHour(int h) {
        hour = h;
        if (hour < 0) {
            hour = 0;
        }
    }
    public void setMinute(int m) {
        minute = m;
        if (minute > 59) {
            minute = 0;
            setHour(hour + 1);
        }
        if (minute < 0) {
            minute = 59;
            setHour(hour - 1);
        }
    }
    public void addSecond(int s) {
        second += s;
        if (second > 59) {
            second = 0;
            setMinute(minute + 1);
        }
        if (second < 0) {
            second = 59;
            setMinute(minute - 1);
        }
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public long totalTimeInMs() {
        return minutesToMs(hour * 60 + minute + (double) second / 60);
    }

    public String toString() {
        if (type.equals("timer")){
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        if (hour>12){
        return String.format("%02d:%02d", hour-12, minute) + "PM";}
        else if (hour==12){
            return String.format("%02d:%02d", hour, minute) + "PM";
        }
        else if (hour==0)
            return String.format("%02d:%02d", 12, minute)+ "AM";
        else {
            return String.format("%02d:%02d", hour, minute) + "AM";
        }

    }
}
