package com.mdb.wyn.stayfocused;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Young on 4/9/2016.
 */
public class BlockingActivity extends AppCompatActivity{
    TextView backToWork;
    TextView giveUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocking_activity);
        MainActivity.isBlockingOpen= true;

        backToWork= (TextView) findViewById(R.id.backToWork);
        giveUp= (TextView) findViewById(R.id.giveUp);

        //back to the home page
        backToWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }

        });
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset the timer
                //we have to decide what we want to do after this
            }
        });





    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.isBlockingOpen= false;
    }
}
