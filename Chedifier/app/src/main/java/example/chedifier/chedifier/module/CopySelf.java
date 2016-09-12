package example.chedifier.chedifier.module;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.MainActivity;
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
public class CopySelf extends AbsModule {

    public CopySelf(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("复制");
        return textView;
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(new Intent(mContext, MainActivity.class));
    }
}
