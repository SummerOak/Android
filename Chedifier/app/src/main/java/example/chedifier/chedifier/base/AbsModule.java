package example.chedifier.chedifier.base;

import android.content.Context;
import android.view.View;

import example.chedifier.chedifier.utils.ScreenUtils;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : AbsModule
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public abstract class AbsModule implements View.OnClickListener{

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    public AbsModule(Context context){
        mContext = context;
    }

    protected abstract View createView(int pos);

    public View getView(int pos){
        View view = createView(pos);

        decorateModule(view);

        view.setOnClickListener(this);

        return view;
    }

    private void decorateModule(View view){
        if(view != null){
            int padding = (int)ScreenUtils.dipToPixels(20f);
            view.setPadding(padding,padding,padding,padding);
        }
    }

}
