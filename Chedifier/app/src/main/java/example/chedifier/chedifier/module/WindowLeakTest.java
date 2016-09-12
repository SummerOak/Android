package example.chedifier.chedifier.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : CopySelf
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class WindowLeakTest extends AbsModule {

    public WindowLeakTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("测试窗口泄漏");
        return textView;
    }

    @Override
    public void onClick(View v) {
        testWindowLeak();
    }

    private void testWindowLeak(){

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("title")
                .setMessage("message")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).create();


//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        dialog.show();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        mH.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("cqx","show");
//                while(true){
//                    dialog.show();
//                }
//            }
//        },200);

        if(mContext instanceof Activity){
            ((Activity)mContext).finish();
        }
    }
}
