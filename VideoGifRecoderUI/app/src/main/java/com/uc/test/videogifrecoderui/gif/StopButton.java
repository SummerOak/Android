package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.uc.framework.resources.ResTools;

/**
 * Created by uc on 01/12/2017.
 */

public class StopButton extends View {

    private int mX,mY;// center coordinate
    private int mRingRadius;  // the radius of outer circle
    private int mRectRadius; // the radius of inner rect
    private RectF mRectF; // store the area of inner rect

    private int mRipple = 2; // the number of ripples in one turn
    private int mTime = 400; //time of a full ripple
    private int mHz = 30; //frequency of ui update
    private float mMark = 0f; //distance of the first ripple has run
    private float mStep = 0f; //how long we can take at one step
    private float mDist = 0f; //the distance between two ripple

    private Paint mPaintOfRipple;
    private Paint mPaintOfRing;
    private Paint mPaintOfRect;

    private Runnable mFramer;

    public StopButton(Context context) {
        super(context);

        mPaintOfRect = new Paint();
        mPaintOfRect.setStyle(Paint.Style.FILL);
        mPaintOfRect.setAntiAlias(true);

        mPaintOfRing = new Paint();
        mPaintOfRing.setStyle(Paint.Style.FILL);
        mPaintOfRing.setAntiAlias(true);

        mPaintOfRipple = new Paint();
        mPaintOfRipple.setStyle(Paint.Style.STROKE);
        mPaintOfRipple.setAntiAlias(true);

    }

    public void setRingRadius(int radius){
        mRingRadius = radius;
    }

    public void setRectRadius(int radius){
        mRectRadius = radius;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        showRipple(isEnabled());
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        showRipple(hasWindowFocus);
    }

    public synchronized void showRipple(boolean show){
        if(show){
            if(mFramer == null){
                mFramer = new Runnable() {
                    @Override
                    public void run() {
                        invalidate();

                        mMark += mStep;
                        if(mMark > ((mX - mRingRadius) << 1)){
                            mMark = 0;
                            postDelayed(mFramer,1000);
                        }else{
                            postDelayed(this,1000/mHz);
                        }
                    }
                };

                prepareRipples();
                post(mFramer);
            }
        }else{
            if(mFramer != null){
                removeCallbacks(mFramer);
                mFramer = null;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mX = getMeasuredWidth() >> 1;
        mY = getMeasuredHeight() >> 1;
        mRectF = new RectF(mX- mRectRadius,mY- mRectRadius,mX+ mRectRadius,mY+ mRectRadius);
    }

    private void prepareRipples(){
        mStep = ((float)(mX- mRingRadius) / mTime) * (1000f / mHz);
        mDist = ((float)(mX - mRingRadius) / mRipple);
        mMark = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaintOfRing.setColor(ResTools.getColor(isEnabled()?"video_gif_stop_enable":"video_gif_stop_disable"));
        canvas.drawCircle(mX,mY, mRingRadius,mPaintOfRing);

        mPaintOfRect.setColor(ResTools.getColor("video_gif_stop_rect"));
        canvas.drawRoundRect(mRectF, mRectRadius >> 2, mRectRadius >> 2,mPaintOfRect);

        drawRipples(canvas);
    }

    private final void drawRipples(Canvas canvas){
        if(!isEnabled()){
            return;
        }

        for(int i = 0; i < mRipple; i++){
            int p = (int)(mMark - i * mDist) + mRingRadius;
            if(mRingRadius < p && p < mX){
                int color = ResTools.getColor("video_gif_stop_enable") & 0x00FFFFFF;
                int alpha = (int)(0x66 * (1 - ((float)(p- mRingRadius) / mX - mRingRadius)));
                mPaintOfRipple.setColor((alpha << 24) | color);
                float sw = p - mRingRadius;
                mPaintOfRipple.setStrokeWidth(sw);
                canvas.drawCircle(mX,mY,p-sw/2, mPaintOfRipple);
            }
        }
    }




}
