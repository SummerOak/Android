package example.chedifier.chedifier.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : BlankActivity
 * <p/>
 * Creation    : 2016/6/29
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/6/29, chengqianxing, Create the file
 * ****************************************************************************
 */
public class BlankActivity extends Activity{

    private static final String TAG = BlankActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        finish();

        Log.i(TAG, "onCreate");
    }


    @Override
    protected void onStart(){
        super.onStart();

//        moveTaskToBack(true);//这个会很快触发自身的 onPause() 和  onStop() 生命周期
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

    }

    public static void startSelf(Context ctx){
        Log.i(TAG,"startSelf");
        if(ctx != null){
            Intent it = new Intent(ctx,BlankActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(it);
        }
    }
}
