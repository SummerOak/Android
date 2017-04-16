package example.chedifier.chedifier.module;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.MainActivity;
import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.window.OpenGLTestWindow;

/**
 * Created by Administrator on 2017/3/6.
 */

public class OpenGLTest extends AbsModule {

    public OpenGLTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("OpenGL test");
        return textView;
    }

    @Override
    public void onClick(View v) {

        MainActivity.getEnv().pushWindow(new OpenGLTestWindow(mContext));

    }



}
