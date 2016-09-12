package example.chedifier.chedifier.module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
public class PreWindowTest extends AbsModule {

    public PreWindowTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("测试启动窗口");
        return textView;
    }

    @Override
    public void onClick(View v) {
        String pkg = "com.UCMobile";
        String act = "com.uc.urlfilter.UrlFilterLocalBlackListActivity";

//                String pkg = "helper.example.chedifier.chedifierhelper";
//                String act = "helper.example.chedifier.chedifierhelper.base.PreWindowTest";

        try{
            Intent it = new Intent();
            it.setPackage(pkg);
            it.setComponent(new ComponentName(pkg,act));
            mContext.startActivity(it);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
