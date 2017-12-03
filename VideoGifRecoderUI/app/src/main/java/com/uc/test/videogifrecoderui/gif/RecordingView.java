package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uc.framework.resources.ResTools;
import com.uc.test.videogifrecoderui.R;

/**
 * Created by uc on 30/11/2017.
 */

public class RecordingView implements View.OnClickListener{

    private static final String TAG = "RecordingView";

    private final int VID_BACK = 1;
    private final int VID_STOP = 2;

    private Context mContext;
    private RelativeLayout mView;

    private TextView mBackButton;
    private ShapeDrawable mBackButtonBackground;


    private final int FRAME_RATE = 60;
    private float mFrameInterval = 1000f/FRAME_RATE;
    private Runnable mFramer;
    private int mProgress;
    private int mMinRecordTime;
    private int mMaxRecordTime;
    private ProgressBar mProgressBar;

    private StopTips mStopTips;
    private StopButton mStopButton;

    private IListener mListener;

    protected RecordingView(Context context){
        mContext = context;
    }

    private void initView(){
        if(mView != null){
            return;
        }

        mView = new RelativeLayout(mContext);
        mView.setBackgroundColor(Color.WHITE);

        RelativeLayout.LayoutParams lp;

        //back button
        mBackButton = new TextView(mContext);
        mBackButton.setId(VID_BACK);
        mBackButton.setOnClickListener(this);
        mBackButton.setTextSize(14);
        mBackButton.setGravity(Gravity.CENTER);
        mBackButton.setPadding(0,0,0,0);
        mBackButton.setText(ResTools.getUCString(R.string.video_gif_cancel));
        lp = new RelativeLayout.LayoutParams(ResTools.getDimenInt(R.dimen.video_gif_back_width),
                ResTools.getDimenInt(R.dimen.video_gif_back_height));
        lp.leftMargin = ResTools.getDimenInt(R.dimen.video_gif_back_left_margin);
        lp.topMargin = ResTools.getDimenInt(R.dimen.video_gif_back_top_margin);
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        int cr = ResTools.getDimenInt(R.dimen.video_gif_back_radius);
        float[] r = new float[]{cr,cr,cr,cr,cr,cr,cr,cr};
        mBackButtonBackground = new ShapeDrawable(new RoundRectShape(r,null,null));
        VideoGifHelper.setDrawable(mBackButton,mBackButtonBackground);
        mView.addView(mBackButton,lp);

        //progress bar
        mProgressBar = new ProgressBar(mContext);
        mProgressBar.setStubWidth(ResTools.dpToPxI(10f));
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ResTools.getDimenInt(R.dimen.video_gif_progress_height));
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mView.addView(mProgressBar,lp);

        //tips
        mStopTips = new StopTips(mContext);
        mStopTips.setGravity(Gravity.CENTER);
        mStopTips.setPadding(ResTools.getDimenInt(R.dimen.video_gif_tips_left_padding), 0,
                ResTools.getDimenInt(R.dimen.video_gif_tips_right_padding),0);
        lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ResTools.getDimenInt(R.dimen.video_gif_tips_height));
        lp.topMargin = ResTools.getDimenInt(R.dimen.video_gif_tips_top_margin);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mView.addView(mStopTips,lp);

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
        mView.addView(mStopButton,lp);


        onThemeChanged();
    }

    public View getView(){
        initView();
        return mView;
    }

    public void setMinRecordTime(int milSec){
        initView();

        mMinRecordTime = milSec;
        mProgressBar.setStubs(mMinRecordTime);
    }

    public void setMaxRecordTime(int milSec){
        initView();

        mMaxRecordTime = milSec;
        mProgressBar.setMax(milSec);
    }


    public void setListener(IListener listener){
        mListener = listener;
    }

    public void start(){
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
                    getView().postDelayed(this, (int)mFrameInterval);
                }
            }
        };

        getView().postDelayed(mFramer, (int)mFrameInterval);
    }

    public void stop(){

        if(mFramer != null){
            getView().removeCallbacks(mFramer);
            mFramer = null;
        }

        if(mListener != null){
            mListener.onStop();
        }
    }


    public void onThemeChanged(){
        updateStopState();
        mBackButton.setTextColor(ResTools.getColor("video_gif_text_color"));
        mBackButtonBackground.getPaint().setColor(ResTools.getColor("video_gif_background"));
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

    private void exit(){
        if(mListener != null){
            mListener.onExit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case VID_BACK:
                exit();
                break;

            case VID_STOP:
                stop();
                break;
        }
    }

    protected interface IListener{
        void onStop();
        void onExit();
    }
}
