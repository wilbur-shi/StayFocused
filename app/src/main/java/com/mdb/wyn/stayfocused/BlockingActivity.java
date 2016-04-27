package com.mdb.wyn.stayfocused;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
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
                int style;
                if (Build.VERSION.SDK_INT > 20) {
                    style = R.style.MyAlertDialogStyle;
                } else {
                    style = 0;
                }
                new AlertDialog.Builder(BlockingActivity.this, style)
                        .setIcon(R.drawable.ic_block_24dp)
                        .setTitle("RESET ALL VALUES")
                        .setMessage("Are you sure you want to give up?")
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendBroadcastReceiver();
                                finish();
                            }
                        })
                        .setPositiveButton("No", null)
                        .show();
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
