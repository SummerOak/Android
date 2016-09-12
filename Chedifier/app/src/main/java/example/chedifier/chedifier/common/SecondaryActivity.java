package example.chedifier.chedifier.common;

import android.os.Bundle;
import android.widget.TextView;

import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.BaseActivity;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : SecondaryActivity
 * <p/>
 * Creation    : 2016/9/5
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/5, chengqianxing, Create the file
 * ****************************************************************************
 */
public class SecondaryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("hello");
        setContentView(textView);
    }
}
