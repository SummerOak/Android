package example.chedifier.chedifier.module;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.TalkbackTest;

/**
 * Created by chedifier on 2016/9/24.
 */
public class TalkbackModule extends AbsModule{
    public TalkbackModule(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {

        TextView textView = new TextView(mContext);
        textView.setText("进入talkback界面");

        return textView;
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(new Intent(mContext, TalkbackTest.class));
    }
}
