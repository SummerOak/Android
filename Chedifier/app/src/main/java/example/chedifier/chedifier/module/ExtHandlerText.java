package example.chedifier.chedifier.module;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.ExtHandlerUser;

/**
 * Created by chedifier on 2016/12/19.
 */
public class ExtHandlerText extends AbsModule {

    public ExtHandlerText(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("ExtHandler");
        return textView;
    }

    ExtHandlerUser mUser;

    @Override
    public void onClick(View v) {

        if(mUser == null){
            mUser = new ExtHandlerUser();
            mUser.start();
        }else{
            mUser = null;
        }

    }
}
