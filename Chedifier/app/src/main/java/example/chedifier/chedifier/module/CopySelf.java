package example.chedifier.chedifier.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import example.chedifier.base.utils.Md5Utils;
import example.chedifier.base.utils.StringUtils;
import example.chedifier.base.utils.SystemUtils;
import example.chedifier.chedifier.MainActivity;
import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.FloatCompare;
import example.chedifier.chedifier.test.HandlerTest;

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
public class CopySelf extends AbsModule {

    public CopySelf(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("复制");
        textView.setId(mContext.getResources().getIdentifier("chedifier_res_id_test","id",mContext.getPackageName()));
        return textView;
    }

    @Override
    public void onClick(View v) {
//        mContext.startActivity(new Intent(mContext, MainActivity.class));

        Log.i(TAG,"begin " + System.currentTimeMillis());


        HandlerTest h = new HandlerTest();
        h.start();


//        FloatCompare floatCompare = new FloatCompare();
//        for(int i=0;i<1000000000;i++){
//            floatCompare.test();
//        }
//        Log.i(TAG,"end " + System.currentTimeMillis());
//
//
//        long t = System.currentTimeMillis();
//        String m = Md5Utils.getMD5("a");
//        Log.i(TAG,m + "md5 cost: " + (System.currentTimeMillis() - t));
//        m = Md5Utils.getMD5("Jjfkdsljjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjkkds");
//        Log.i(TAG,m + "md5 cost: " + (System.currentTimeMillis() - t));
//
//        for(int i=0;i<100000;i++){
//            testArrayConcur();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    int worker = 0;
    private synchronized void addWorker(){
        ++worker;
    }

    private synchronized void decWorker(){
        --worker;
    }

    private List<Integer> mTest = Collections.synchronizedList(new ArrayList<Integer>());
    private void testArrayConcur(){
        if(worker != 0){
            return;
        }

        mTest = new ArrayList<>(0);
        for(int i=0;i<5;i++){
            addWorker();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j = 0;j<100;j++){
                        mTest.add(j);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        String content = "content ";
                        for(int e:mTest){
                            content += " " + e;
                        }

                        Log.i("arr_c","" + content);

                        mTest.remove(0);
                    }

                    decWorker();
                }
            }).start();
        }
    }
}
