package com.uc.test.videogifrecoderui.gif;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.uc.framework.resources.ResTools;


public class VideoGifRecoder implements RecordingView.IListener{

    private Context mContext;
    private ViewGroup mAnchor;
    private IListener mListener;

    private ImageButton mEntryView;

    private RecordingView mRecordView;
    private IVideoGifHandler mRecordHandler;

    /**
     * the min time length limit of recording.
     */
    private final int MIN_GIF_TIME_LENGTH = 5000;

    /**
     * the max time length limit of recording.
     */
    private final int MAX_GIF_TIME_LENGTH = 10000;

    public VideoGifRecoder(ViewGroup anchor){
        this(anchor,null);
    }

    public VideoGifRecoder(ViewGroup anchor, IListener listener){
        mAnchor = anchor;
        mContext = mAnchor.getContext();
        mListener = listener;
    }

    public void setRecordHandler(IVideoGifHandler handler){
        mRecordHandler = handler;
    }

    public View getEntry(){
        if(mEntryView == null){
            mEntryView = new ImageButton(mContext);
            mEntryView.setBackgroundColor(Color.TRANSPARENT);
            mEntryView.setImageDrawable(ResTools.getDrawable("video_gif_4.svg"));
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
        if(mAnchor != null && mAnchor.indexOfChild(getRecordView().getView()) < 0){
            mAnchor.addView(getRecordView().getView(), ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
        }

        getRecordView().start();

        if(mListener != null){
            mListener.onGifRecordStart();
        }
    }

    private RecordingView getRecordView(){
        if(mRecordView == null){
            mRecordView = new RecordingView(mContext);
            mRecordView.setListener(this);
            mRecordView.setMaxRecordTime(MAX_GIF_TIME_LENGTH);
            mRecordView.setMinRecordTime(MIN_GIF_TIME_LENGTH);
        }

        return mRecordView;
    }

    @Override
    public void onStop() {
        if(mRecordHandler != null){
            mRecordHandler.handleGifAction(IVideoGifHandler.STOP_RECORD);
        }
    }

    @Override
    public void onExit() {
        if(mAnchor != null){
            mAnchor.removeView(mRecordView.getView());
        }

        if(mListener != null){
            mListener.onGifRecordEnd();
        }
    }

    public interface IListener{
        void onGifRecordStart();
        void onGifRecordEnd();
        void onGifRecordExit();
    }

}
