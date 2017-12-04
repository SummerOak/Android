package com.uc.test.videogifrecoderui.gif;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.uc.framework.resources.ResTools;
import com.uc.test.videogifrecoderui.R;

/**
 * Created by uc on 03/12/2017.
 */

public class StopTips extends TextView{


    private ShapeDrawable mTipsBackground;
    private ShapeDrawable mTipsLeftPoint;
    private ValueAnimator mIndicatorAnimator;


    protected StopTips(Context context){
        super(context);

        int cr = ResTools.getDimenInt(R.dimen.video_gif_tips_radius);
        mTipsBackground = new ShapeDrawable(new RoundRectShape(new float[]{cr,cr,cr,cr,cr,cr,cr,cr},null,null));
        VideoGifHelper.setDrawable(this,mTipsBackground);
        mTipsLeftPoint = new ShapeDrawable(new OvalShape());
        mTipsLeftPoint.getPaint().setColor(Color.RED);
        int indicatorSize = ResTools.getDimenInt(R.dimen.video_gif_tips_indicator_radius);
        mTipsLeftPoint.setBounds(0,0,indicatorSize,indicatorSize);
        setCompoundDrawablePadding(ResTools.getDimenInt(R.dimen.video_gif_tips_indicator_padding));
        setCompoundDrawables(mTipsLeftPoint,null,null,null);
        setTextSize(13);

        mIndicatorAnimator = ValueAnimator.ofInt(0,255);
        mIndicatorAnimator.setInterpolator(new AccelerateInterpolator());
        mIndicatorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mIndicatorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mIndicatorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int)animation.getAnimatedValue();
                Log.i("cqx","alpha: " + alpha);
                mTipsLeftPoint.setAlpha(alpha);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(hasWindowFocus()){
            mIndicatorAnimator.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIndicatorAnimator.end();
    }

    public void setCanStop(boolean canStop){
        mTipsLeftPoint.getPaint().setColor(ResTools.getColor(canStop?"video_gif_progress_can_stop":"video_gif_progress_cant_stop"));
        mTipsBackground.getPaint().setColor(ResTools.getColor("video_gif_background"));
        setText(ResTools.getUCString(canStop?R.string.video_gif_stop_enabled_tips:R.string.video_gif_min_time_limit_tips));
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus){
            mIndicatorAnimator.start();
        }else{
            mIndicatorAnimator.end();
        }
    }
}
