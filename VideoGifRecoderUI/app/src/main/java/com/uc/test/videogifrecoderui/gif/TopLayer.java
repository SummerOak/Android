package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.uc.framework.resources.ResTools;
import com.uc.test.videogifrecoderui.R;

/**
 * Created by uc on 03/12/2017.
 */

public class TopLayer extends FrameLayout implements View.OnClickListener{

    private final int VID_BACK = 1;
    private TextView mBackButton;
    private ShapeDrawable mBackButtonBackground;
    private IEventHandler mViewEventHandler;

    public TopLayer(@NonNull Context context, IEventHandler handler) {
        super(context);

        this.mViewEventHandler = handler;

        setBackgroundColor(Color.TRANSPARENT);

        //back button
        mBackButton = new TextView(context);
        mBackButton.setId(VID_BACK);
        mBackButton.setOnClickListener(this);
        mBackButton.setTextSize(14);
        mBackButton.setGravity(Gravity.CENTER);
        mBackButton.setPadding(0,0,0,0);
        mBackButton.setText(ResTools.getUCString(R.string.video_gif_cancel));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ResTools.getDimenInt(R.dimen.video_gif_back_width),
                ResTools.getDimenInt(R.dimen.video_gif_back_height));
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.leftMargin = ResTools.getDimenInt(R.dimen.video_gif_back_left_margin);
        lp.topMargin = ResTools.getDimenInt(R.dimen.video_gif_back_top_margin);

        int cr = ResTools.getDimenInt(R.dimen.video_gif_back_radius);
        float[] r = new float[]{cr,cr,cr,cr,cr,cr,cr,cr};
        mBackButtonBackground = new ShapeDrawable(new RoundRectShape(r,null,null));
        VideoGifHelper.setDrawable(mBackButton,mBackButtonBackground);
        mBackButton.setOnClickListener(this);
        addView(mBackButton,lp);
    }

    public void attachTo(ViewGroup anchor){
        if(anchor != null && anchor.indexOfChild(this) < 0){
            detach();
            anchor.addView(this, ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);

            onThemeChanged();
        }
    }

    public void detach(){
        ViewGroup parent = (ViewGroup)getParent();
        if(parent != null){
            parent.removeView(this);
        }
    }

    public void onThemeChanged(){
        mBackButton.setTextColor(ResTools.getColor("video_gif_text_color"));
        mBackButtonBackground.getPaint().setColor(ResTools.getColor("video_gif_background"));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == VID_BACK){
            if(mViewEventHandler != null){
                mViewEventHandler.onExitEvent();
            }
        }
    }
}
