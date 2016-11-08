package example.chedifier.chedifier.module;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.SkyWalkerTestActivity;

/**
 * Created by chedifier on 2016/11/7.
 */
public class SkyWalkerTester extends AbsModule {

    public SkyWalkerTester(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("SkyWalkerLibTest");
        return textView;
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(new Intent(mContext, SkyWalkerTestActivity.class));
    }

}
