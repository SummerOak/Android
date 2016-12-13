package example.chedifier.chedifier.test.talkback;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import example.chedifier.chedifier.base.Color;
import example.chedifier.chedifier.customview.VirtualView;

/**
 * Created by chedifier on 2016/11/17.
 */
public class SimpleColorPieceWithDesc extends VirtualView implements View.OnClickListener,View.OnLongClickListener{

    private Color mColor;

    public SimpleColorPieceWithDesc(Context context, int w, int h, int bg) {
        super(context, w, h, bg);

        mColor = Color.valueOf(bg);

        setOnClickListener(this);
        setOnLongClickListener(this);

        setContentDescription(mColor.desc);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext,mColor.desc,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(mContext,mColor.desc,Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getAdapterViewParent().performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,null);
        }

        return true;
    }
}
