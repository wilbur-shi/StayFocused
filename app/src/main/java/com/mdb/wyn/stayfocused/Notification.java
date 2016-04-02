package com.mdb.wyn.stayfocused;

/**
 * Created by Wilbur on 4/2/2016.
 */
public class Notification {
    private int maxNumNotifications;
    private int numNotifies;

    public Notification(int max) {
        maxNumNotifications = max;
        numNotifies = 0;
    }

    public void createNotification() {
        numNotifies += 1;
        if (numNotifies < 4) {
            // TODO: Do notification stuff
        }
    }
}
