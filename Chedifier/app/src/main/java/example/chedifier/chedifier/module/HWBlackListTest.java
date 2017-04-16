package example.chedifier.chedifier.module;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/2/6.
 */

public class HWBlackListTest extends AbsModule {

    final int DISABLE = 1;
    final int ENABLE = 2;

    public HWBlackListTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView disable = new TextView(mContext);
        decorateItem(disable);
        disable.setId(DISABLE);
        disable.setOnClickListener(this);
        disable.setText("disable hw black list");
        linearLayout.addView(disable);

        TextView enable = new TextView(mContext);
        decorateItem(enable);
        enable.setId(ENABLE);
        enable.setOnClickListener(this);
        enable.setText("enable hw black list");
        linearLayout.addView(enable);

        return linearLayout;
    }

    protected boolean rootViewDecoratable(){
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case ENABLE:
                enable(true);
                break;

            case DISABLE:
                enable(false);
                break;
        }

    }

    private void enable(boolean enable){
        Intent intent = new Intent("com.huawei.intent.action.mdm_network_blacklist_changed");
        intent.setPackage("com.UCMobile");
        if(enable){
            intent.addFlags(1);
        }

        mContext.sendBroadcast(intent);
    }
}
