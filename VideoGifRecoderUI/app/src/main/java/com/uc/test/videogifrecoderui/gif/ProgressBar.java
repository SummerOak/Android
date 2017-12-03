package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by uc on 03/12/2017.
 */

/**package**/public class ProgressBar extends View {


    private int mMax;
    private int mProgress;
    private int mStub;
    private int mProgressColor;
    private int mNormalColor;
    private int mStubColor;
    private int mStubWidth;
    private Paint mPaint;

    protected ProgressBar(Context context) {
        super(context);

        mPaint = new Paint();
    }

    public void setMax(int max){
        mMax = max;
    }

    public void setProgress(int progress){
        mProgress = progress;
    }

    public void setStubs(int stub){
        mStub = stub;
    }

    public void setStubWidth(int width){
        mStubWidth = width;
    }

    public void setColor(int progressColor,int normalColor,int stubColor){
        mProgressColor = progressColor;
        mNormalColor = normalColor;
        mStubColor = stubColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int p = getMeasuredWidth() * mProgress/mMax;
        mPaint.setColor(mProgressColor);
        canvas.drawRect(0,0,p,getMeasuredHeight(),mPaint);
        mPaint.setColor(mNormalColor);
        canvas.drawRect(p,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        mPaint.setColor(mStubColor);
        if(0 < mStub && mStub < mMax){
            int s = getMeasuredWidth() * mStub / mMax;
            canvas.drawRect(s - (mStubWidth >> 1),0,s + (mStubWidth >> 1),getMeasuredHeight(),mPaint);
        }
    }
}
