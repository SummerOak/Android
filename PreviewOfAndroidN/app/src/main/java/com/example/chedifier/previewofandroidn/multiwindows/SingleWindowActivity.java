package com.example.chedifier.previewofandroidn.multiwindows;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;

/**
 * Created by chedifier on 2016/3/15.
 */
public class SingleWindowActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.singlew_act);

        ((Button)findViewById(R.id.start_adjacentw_difftask)).setOnClickListener(this);
        ((Button)findViewById(R.id.start_currentw_difftask)).setOnClickListener(this);
        ((Button)findViewById(R.id.start_currentw_sametask)).setOnClickListener(this);
        ((Button)findViewById(R.id.start_adjacentw_sametask)).setOnClickListener(this);
    }


    /**
     * 邻窗口不同task
     */
    public void onClickStartAdjacentDiffTask(){

        Intent intent = new Intent(this, MultiWindowSampleActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 邻窗口相同task
     */
    public void onClickStartAdjacentSameTask(){

        Intent intent = new Intent(this, MultiWindowSampleActivity3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        startActivity(intent);
    }

    /**
     * 当前窗口不同task
     */
    public void onClickStartCurrentDiffTask(){
        Intent intent = new Intent(this, MultiWindowSampleActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 当前窗口相同task
     */
    public void onClickStartCurrentSameTask(){
        Intent intent = new Intent(this, MultiWindowSampleActivity3.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_adjacentw_difftask:

                onClickStartAdjacentDiffTask();

                break;

            case R.id.start_currentw_difftask:

                onClickStartCurrentDiffTask();

                break;

            case R.id.start_currentw_sametask:

                onClickStartCurrentSameTask();

                break;

            case R.id.start_adjacentw_sametask:
                onClickStartAdjacentSameTask();

                break;
        }
    }
}
