package example.chedifier.chedifier.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : HWThemeChangeTest
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class HWThemeChangeTest extends AbsModule {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"onReceive: " + intent);
        }
    };

    public HWThemeChangeTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("监听华为主题");
        return textView;
    }

    @Override
    public void onClick(View v) {

        IntentFilter filter = new IntentFilter();
        String hwThemeAction = "com.huawei.android.thememanager.applytheme";
        filter.addAction(hwThemeAction);

        mContext.registerReceiver(mReceiver, filter);
        Log.i(TAG, "listening...");
    }
}
