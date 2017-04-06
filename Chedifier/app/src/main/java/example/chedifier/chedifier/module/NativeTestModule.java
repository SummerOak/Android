package example.chedifier.chedifier.module;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.NativeTest;

/**
 * Created by chedifier on 2016/12/13.
 */
public class NativeTestModule extends AbsModule {

    public NativeTestModule(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("Native Test");
        return textView;
    }

    @Override
    public void onClick(View v) {

        NativeTest.test("thread1");

        new Thread(new Runnable() {
            @Override
            public void run() {
                NativeTest.test("thread2");
            }
        }).start();

    }

}
