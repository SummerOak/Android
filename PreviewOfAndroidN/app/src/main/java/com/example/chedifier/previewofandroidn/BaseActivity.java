package com.example.chedifier.previewofandroidn;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by chedifier on 2016/3/14.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    private TextView mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: " + (savedInstanceState == null ? "savedInstanceState=null" : savedInstanceState.toString()));


        super.onCreate(savedInstanceState);

        setTitle(TAG);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "onWindowFocusChanged hasFocus:" + hasFocus);

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");

        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow");

        super.onDetachedFromWindow();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: " + outState.toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: " + savedInstanceState.toString());

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onMultiWindowChanged(boolean inMultiWindow) {
        Log.d(TAG, "onMultiWindowChanged: inMultiWindow=" + inMultiWindow);

        super.onMultiWindowChanged(inMultiWindow);
    }

    @Override
    public void onPictureInPictureChanged(boolean inPictureInPicture) {
        Log.d(TAG, "onPictureInPictureChanged: inPictureInPicture=" + inPictureInPicture);

        super.onPictureInPictureChanged(inPictureInPicture);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");

        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");

        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");

        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        Configuration oldConfig = getResources().getConfiguration();
        Log.d(TAG, "onConfigurationChanged: " + oldConfig.toString()
                + "  oldConfig.screenLayout: " + oldConfig.screenLayout
                + "  oldConfig.orientation: " + oldConfig.orientation
                + "  oldConfig.smallestScreenWidthDp"  + oldConfig.smallestScreenWidthDp);

        Log.d(TAG, "onConfigurationChanged: " + newConfig.toString()
                + "  newConfig.screenLayout: " + newConfig.screenLayout
                + "  newConfig.orientation: " + newConfig.orientation
                + "  newConfig.smallestScreenWidthDp"  + newConfig.smallestScreenWidthDp);



        super.onConfigurationChanged(newConfig);
    }

    private void log(TextView logV, String logStr) {
        if (logStr != null && logV != null) {
            String l = logV.getText().toString();
            if (l == null) {
                l = "";
            }
            logV.setText(l += logStr);
        }
    }
}