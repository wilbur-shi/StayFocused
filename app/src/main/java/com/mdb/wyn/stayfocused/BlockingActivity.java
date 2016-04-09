package com.mdb.wyn.stayfocused;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Young on 4/9/2016.
 */
public class BlockingActivity extends AppCompatActivity{
    static boolean isBlockingOpen= false;
    TextView backToWork;
    TextView giveUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocking_activity);
        isBlockingOpen= true;

        backToWork= (TextView) findViewById(R.id.backToWork);
        giveUp= (TextView) findViewById(R.id.giveUp);





    }

    @Override
    protected void onStop() {
        super.onStop();
        isBlockingOpen= false;
    }
}
