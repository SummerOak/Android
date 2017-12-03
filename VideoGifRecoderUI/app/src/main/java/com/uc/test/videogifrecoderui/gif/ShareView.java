package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by uc on 02/12/2017.
 */

public class ShareView {

    private Context mContext;

    private RelativeLayout mView;

    protected ShareView(Context context){
        mContext = context;
    }

    public View getView(){
        return mView;
    }
}
