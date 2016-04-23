package com.mdb.wyn.stayfocused;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Young on 4/9/2016.
 */
public class BlockingActivity extends AppCompatActivity{
    Button backToWork;
    Button giveUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocking_activity);
        MainActivity.isBlockingOpen= true;

        backToWork= (Button) findViewById(R.id.backToWork);
        giveUp= (Button) findViewById(R.id.giveUp);

        //back to the home page
        backToWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMain);
                finish();
            }

        });
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(BlockingActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Reset values")
                        .setMessage("Are you sure you want to reset values?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               sendBroadcastReceiver();finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                // TODO: Failed s
                //reset the timer
                //we have to decide what we want to do after this
            }
        });
    }

    private void sendBroadcastReceiver() {
        Intent closeMainActivity = new Intent("finish_activity");
        sendBroadcast(closeMainActivity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.isBlockingOpen = false;
    }
}
