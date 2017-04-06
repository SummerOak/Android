package example.chedifier.chedifier.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/1/11.
 */

public class QSChouModule extends AbsModule {
    public QSChouModule(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView t = new TextView(mContext);
        t.setText("轻松筹");

        return t;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://m.qschou.com/"));
        mContext.startActivity(intent);
    }
}
