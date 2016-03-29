package com.example.chedifier.previewofandroidn.multiwindows;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;

/**
 * Created by chedifier on 2016/3/26.
 */
public class FixOrientationSupportActivity extends BaseActivity{

    private boolean mLocked = false;
    private boolean mScrHor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fix_orientation_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClickFixOrientation(View view){
        Log.d(TAG,"onClickFixOrientation");

        int currentRequestOri = getRequestedOrientation();
        Log.d(TAG,"currentRequestOri " + currentRequestOri);

        mLocked = !mLocked;

//        if(mLocked){
//            int ori = getRequestedOrientation();
//            setRequestedOrientation(ori == Configuration.ORIENTATION_PORTRAIT?
//                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
//            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }else{
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//        }

        ((TextView)view).setText(mLocked?"解锁":"锁定屏幕");
    }

    public void onClickChangeScreenOrientation(View view){
        int ori = getRequestedOrientation();
        Log.d(TAG,"onClickChangeScreenOrientation ori：" + ori);

        switch (ori){
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;

            case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:

                break;
        }

        updateOrientationChangeBtn((TextView)view);
    }

    private void updateOrientationChangeBtn(TextView v){

        int ori = getRequestedOrientation();
        Log.d(TAG,"updateOrientationChangeBtn ori：" + ori);

        switch (ori){
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                v.setText("转横屏");
                break;

            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                v.setText("转竖屏");
                break;

            case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:

                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }
}
