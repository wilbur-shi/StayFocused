package com.mdb.wyn.stayfocused;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by Young on 4/22/2016.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Received");
        int requestCode = intent.getExtras().getInt("requestcode");
        if (requestCode==1){
            MainActivity.handleStartButton("alarm");
            System.out.println("received first oneeeeeeeeee");


            //do handleStartButton(), only problem is that i dont know how to make it accessible
//            if i make it static, down the line there are non static built in methods
        }
        else{
            System.out.println("received second oneeeeeeeeee");
            //stop the ontick shit

        }
    }
}
