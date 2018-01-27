package example.chedifier.chedifier.module;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import example.chedifier.chedifier.base.AbsModule;

public class PhoneInfo extends AbsModule {

    public PhoneInfo(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("brand: " + Build.BRAND + "\n" + " sdk level: " + Build.VERSION.SDK_INT);
        return textView;
    }

    @Override
    public void onClick(View v) {

    }

}
