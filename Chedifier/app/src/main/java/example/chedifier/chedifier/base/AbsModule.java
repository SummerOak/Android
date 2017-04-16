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

package example.chedifier.chedifier.base;

import android.content.Context;
import android.view.View;

import example.chedifier.base.utils.ScreenUtils;
import example.chedifier.chedifier.MyApplication;
import example.chedifier.chedifier.R;


public abstract class AbsModule implements View.OnClickListener,View.OnLongClickListener{

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    public AbsModule(Context context){
        mContext = context;
    }

    protected abstract View createView(int pos);

    public View getView(int pos){
        View view = createView(pos);
        view.setId(1);

        if(rootViewDecoratable()){
            decorateItem(view);
        }

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return view;
    }

    protected boolean rootViewDecoratable(){
        return true;
    }

    protected int getCommonItemBackground(){
        return R.drawable.item_bg;
    }

    protected void decorateItem(View view){
        if(view != null){
            int padding = (int) ScreenUtils.dipToPixels(MyApplication.getAppContext(),20f);
            view.setPadding(padding,padding,padding,padding);
            view.setBackgroundResource(getCommonItemBackground());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

}
