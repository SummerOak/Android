package com.uc.test.videogifrecoderui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.uc.framework.resources.ResTools;
import com.uc.test.videogifrecoderui.gif.IVideoGifRecordImpl;
import com.uc.test.videogifrecoderui.gif.VideoGifRecoder;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements VideoGifRecoder.IListener{

    public static Context sContext;

    private VideoGifRecoder mRecoder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sContext = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);

        FrameLayout root = new FrameLayout(this);
        root.setBackgroundResource(R.drawable.video_bg);
        setContentView(root);

        mRecoder = new VideoGifRecoder(root, new IVideoGifRecordImpl() {
            @Override
            public Drawable getCurrentVideoSnapture() {
                return ResTools.getDrawable("video_bg");
            }

            @Override
            public boolean startVideoRecode(String filePath) {
                return true;
            }

            @Override
            public boolean stopVideoRecord() {
                return true;
            }
        },this);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ResTools.dpToPxI(60),ResTools.dpToPxI(60));
        lp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;
        root.addView(mRecoder.getEntry(), lp);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        hideSystemUI();
    }

    private void hideSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    @Override
    public void onGifRecordStart() {
        mRecoder.getEntry().setVisibility(View.INVISIBLE);
    }

    @Override
    public void onGifRecordEnd() {

    }

    @Override
    public void onGifRecordExit() {
        mRecoder.getEntry().setVisibility(View.VISIBLE);
    }
}
