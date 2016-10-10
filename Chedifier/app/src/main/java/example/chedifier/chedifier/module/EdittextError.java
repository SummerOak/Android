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
import example.chedifier.chedifier.common.dialog.BaseDialog;

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

//        LinearLayout contain1 = new LinearLayout(mContext);
//        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        linearLayout.addView(contain1,lp1);

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

        BaseDialog dialog = new BaseDialog(mContext);

        dialog.addNewRow().addEditText("test", 111);
        dialog.addNewRow().addButton("ok", 112);

        final EditText editText = (EditText) dialog.findViewById(111);
        Button button = (Button)dialog.findViewById(112);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setError("当前网址不符合规范");
            }
        });

        dialog.show();
    }
}
