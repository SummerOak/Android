package example.chedifier.chedifier.module;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

public class BlockMainThread extends AbsModule {

    public BlockMainThread(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("block main thread");
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void onClick(View v) {
        while(true);
    }

}
