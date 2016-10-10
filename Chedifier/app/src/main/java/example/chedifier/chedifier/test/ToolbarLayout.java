package example.chedifier.chedifier.test;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by chedifier on 2016/9/26.
 */
public class ToolbarLayout extends LinearLayout {
    public ToolbarLayout(Context context) {
        super(context);
    }

    public ToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        boolean ret = super.performAccessibilityAction(action,arguments);

        int a = 1;

        return ret;
    }

}
