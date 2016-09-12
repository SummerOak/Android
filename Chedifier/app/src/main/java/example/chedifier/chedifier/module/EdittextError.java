package example.chedifier.chedifier.module;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import example.chedifier.chedifier.base.AbsModule;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : EdittextError
 * <p/>
 * Creation    : 2016/9/10
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/10, chengqianxing, Create the file
 * ****************************************************************************
 */
public class EdittextError extends AbsModule{

    private EditText mEditText;
    private Button mButton;

    public EdittextError(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER);

        mEditText = new EditText(mContext);
        mEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 80));
        mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        mButton = new Button(mContext);
        mButton.setText("ok");
        mButton.setOnClickListener(this);
        linearLayout.addView(mButton);

        linearLayout.addView(mEditText);

        return linearLayout;
    }

    @Override
    public void onClick(View v) {
        mEditText.setError("当前网址不符合规范");
    }
}
