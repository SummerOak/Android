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

        ((Button)findViewById(R.id.start_difftask_adjacentw)).setOnClickListener(this);
        ((Button)findViewById(R.id.start_difftask_currentw)).setOnClickListener(this);
        ((Button)findViewById(R.id.start_sametask)).setOnClickListener(this);

    }


    /**
     * 邻窗口不同task
     */
    public void onClickStartAdjacentW(){

        Intent intent = new Intent(this, MultiWindowSampleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 当前窗口不同task
     */
    public void onClickStartCurrentW(){
        Intent intent = new Intent(this, MultiWindowSampleActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 相同task
     */
    public void onClickStartInSameTask(){
        Intent intent = new Intent(this, MultiWindowSampleActivity3.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_difftask_adjacentw:

                onClickStartAdjacentW();

                break;

            case R.id.start_difftask_currentw:

                onClickStartCurrentW();

                break;

            case R.id.start_sametask:

                onClickStartInSameTask();

                break;
        }
    }
}
