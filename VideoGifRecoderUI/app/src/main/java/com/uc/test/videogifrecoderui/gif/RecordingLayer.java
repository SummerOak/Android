package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.uc.framework.resources.ResTools;
import com.uc.test.videogifrecoderui.R;

/**
 * Created by uc on 30/11/2017.
 */

public class RecordingLayer extends RelativeLayout implements View.OnClickListener{

    private static final String TAG = "RecordingLayer";

    private final int VID_STOP = 2;

    private Context mContext;

    private final int FRAME_RATE = 60;
    private float mFrameInterval = 1000f/FRAME_RATE;
    private Runnable mFramer;
    private int mProgress;
    private int mMinRecordTime;
    private int mMaxRecordTime;
    private ProgressBar mProgressBar;

    private StopTips mStopTips;
    private StopButton mStopButton;

    private IEventHandler mViewEventHandler;

    protected RecordingLayer(Context context, IEventHandler handler){
        super(context);
        mContext = context;
        this.mViewEventHandler = handler;
        setBackgroundColor(Color.TRANSPARENT);

        initView();
    }

    private void initView(){

        RelativeLayout.LayoutParams lp;
        //progress bar
        mProgressBar = new ProgressBar(mContext);
        mProgressBar.setStubWidth(ResTools.dpToPxI(10f));
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ResTools.getDimenInt(R.dimen.video_gif_progress_height));
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        addView(mProgressBar,lp);

        //tips
        mStopTips = new StopTips(mContext);
        mStopTips.setTextSize(13);
        mStopTips.setGravity(Gravity.CENTER);
        mStopTips.setPadding(ResTools.getDimenInt(R.dimen.video_gif_tips_left_padding), 0,
                ResTools.getDimenInt(R.dimen.video_gif_tips_right_padding),0);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ResTools.getDimenInt(R.dimen.video_gif_tips_height));
        lp.topMargin = ResTools.getDimenInt(R.dimen.video_gif_tips_top_margin);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);

        addView(mStopTips,lp);

        //stop button
        mStopButton = new StopButton(mContext);
        mStopButton.setId(VID_STOP);
        mStopButton.setOnClickListener(this);
        mStopButton.setRingRadius(ResTools.getDimenInt(R.dimen.video_gif_stop_ring_radius));
        mStopButton.setRectRadius(ResTools.getDimenInt(R.dimen.video_gif_stop_rect_radius));
        lp = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.video_gif_stop_size),
                ResTools.getDimenInt(R.dimen.video_gif_stop_size));
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        lp.rightMargin = ResTools.getDimenInt(R.dimen.video_gif_stop_right_margin);
        addView(mStopButton,lp);

        onThemeChanged();
    }

    public void attachTo(ViewGroup anchor){
        if(anchor != null && anchor.indexOfChild(this) < 0){
            detach();
            anchor.addView(this, ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            invalidate();
        }
    }

    public void detach(){
        ViewGroup parent = (ViewGroup) getParent();
        if(parent != null){
            parent.removeView(this);
        }

        reset();
    }

    public void setMinRecordTime(int milSec){
        mMinRecordTime = milSec;
        mProgressBar.setStubs(mMinRecordTime);
    }

    public void setMaxRecordTime(int milSec){
        mMaxRecordTime = milSec;
        mProgressBar.setMax(milSec);
    }

    public void start(){
        mProgress = 0;
        if(mFramer != null){
            return;
        }

        mFramer = new Runnable() {
            @Override
            public void run() {
                int p = mProgress;
                mProgressBar.setProgress(mProgress+= mFrameInterval);
                if(p < mMinRecordTime && mProgress >= mMinRecordTime
                        || p <= 0 && mProgress <= mMinRecordTime){
                    updateStopState();
                }else{
                    mProgressBar.invalidate();
                }

                if(mProgress > mMaxRecordTime){
                    stop();
                }else{
                    postDelayed(this, (int)mFrameInterval);
                }
            }
        };

        postDelayed(mFramer, (int)mFrameInterval);
    }

    public void stop(){

        if(mFramer != null){
            removeCallbacks(mFramer);
            mFramer = null;
        }

        if(mViewEventHandler != null){
            mViewEventHandler.onStopEvent(mProgress);
        }
    }

    public int getProgress(){
        return mProgress;
    }

    private void reset(){
        mProgress = 0;
        mProgressBar.setProgress(0);
        mStopButton.setEnabled(false);
    }

    public void onThemeChanged(){
        updateStopState();
        mStopTips.setTextColor(ResTools.getColor("video_gif_text_color"));
        mStopButton.postInvalidate();
    }



    private void updateStopState(){
        boolean canStop = mProgress >= mMinRecordTime;
        mProgressBar.setColor(ResTools.getColor(canStop?"video_gif_progress_can_stop":"video_gif_progress_cant_stop"),
                ResTools.getColor("video_gif_background"),
                ResTools.getColor("video_gif_progress_cant_stop"));
        mProgressBar.setStubs(canStop?0:mMinRecordTime);
        mProgressBar.invalidate();

        mStopButton.setEnabled(canStop);
        mStopTips.setCanStop(canStop);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case VID_STOP:
                stop();
                break;
        }
    }

}
