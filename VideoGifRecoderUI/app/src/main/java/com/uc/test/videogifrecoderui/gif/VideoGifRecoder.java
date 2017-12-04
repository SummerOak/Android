package com.uc.test.videogifrecoderui.gif;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class VideoGifRecoder implements IEventHandler {
    private static final String TAG = "VideoGifRecoder";

    private Context mContext;
    private ViewGroup mAnchor;
    private IListener mListener;

    private ImageButton mEntryView;

    private TopLayer mBaseLayer;
    private RecordingLayer mRecordView;
    private ShareGifLayer mShareView;
    private IVideoGifRecordImpl mRecordHandler;

    private byte mState;
    private final byte INIT = 0;
    private final byte RECORDING = 1;
    private final byte RECORDED = 2;

    /**
     * the min time length limit of recording.
     */
    private final int MIN_GIF_TIME_LENGTH = 1000;

    /**
     * the max time length limit of recording.
     */
    private final int MAX_GIF_TIME_LENGTH = 10000;

    public VideoGifRecoder(ViewGroup anchor, IVideoGifRecordImpl handler, IListener listener){
        mAnchor = anchor;
        mContext = mAnchor.getContext();
        mRecordHandler = handler;
        mListener = listener;
        mState = INIT;
    }

    public View getEntry(){
        if(mEntryView == null){
            mEntryView = new ImageButton(mContext);
            mEntryView.setBackgroundColor(Color.TRANSPARENT);
            mEntryView.setImageDrawable(new ColorDrawable(Color.BLACK));
            mEntryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRecord();
                }
            });
        }

        return mEntryView;
    }

    private void startRecord(){
        if(mRecordHandler == null){
            Log.e(TAG,"record impl is null");
            return;
        }

        getBaseLayer().attachTo(mAnchor);
        getRecordView().attachTo(mAnchor);
        getRecordView().start();
        mRecordHandler.startVideoRecode(generateGifFilePath());
        mState = RECORDING;

        if(mListener != null){
            mListener.onGifRecordStart();
        }
    }

    private void stopRecord(int recordTime){
        if(recordTime > MIN_GIF_TIME_LENGTH){
            if(mRecordHandler != null){
                mRecordHandler.stopVideoRecord();
            }

            getRecordView().detach();
            getShareView().setGifPreview(mRecordHandler.getCurrentVideoSnapture());
            getShareView().attachTo(mAnchor);
            mAnchor.bringChildToFront(getBaseLayer());

            if(mListener != null){
                mListener.onGifRecordEnd();
            }
            mState = RECORDED;
        }
    }

    private void exit(){
        if(mState == RECORDING && mRecordHandler != null){
            mRecordHandler.stopVideoRecord();
        }

        getRecordView().detach();
        getShareView().detach();
        getBaseLayer().detach();
        mState = INIT;

        if(mListener != null){
            mListener.onGifRecordExit();
        }
    }

    private TopLayer getBaseLayer(){
        if(mBaseLayer == null){
            mBaseLayer = new TopLayer(mContext,this);
        }

        return mBaseLayer;
    }

    private RecordingLayer getRecordView(){
        if(mRecordView == null){
            mRecordView = new RecordingLayer(mContext,this);
            mRecordView.setMaxRecordTime(MAX_GIF_TIME_LENGTH);
            mRecordView.setMinRecordTime(MIN_GIF_TIME_LENGTH);
        }

        return mRecordView;
    }

    private ShareGifLayer getShareView(){
        if(mShareView == null){
            mShareView = new ShareGifLayer(mContext,this);
            mShareView.addMenu(1,"保存本地",new ColorDrawable(Color.GREEN));
            mShareView.addMenu(2,"微信好友",new ColorDrawable(Color.RED));
            mShareView.addMenu(3,"微博",new ColorDrawable(Color.YELLOW));
            mShareView.addMenu(4,"QQ空间",new ColorDrawable(Color.BLUE));
        }

        return mShareView;
    }

    private String generateGifFilePath(){
        return "g_" + System.currentTimeMillis() + ".gif";
    }

    @Override
    public void onStopEvent(int recordTime) {
        stopRecord(recordTime);
    }

    @Override
    public void onExitEvent() {
        exit();
    }

    @Override
    public void onGifMenuClickEvent(ShareGifLayer.MenuItem item) {

    }

    public interface IListener{
        void onGifRecordStart();
        void onGifRecordEnd();
        void onGifRecordExit();
    }


}
