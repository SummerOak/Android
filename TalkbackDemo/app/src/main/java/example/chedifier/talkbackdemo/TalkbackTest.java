package example.chedifier.talkbackdemo;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import example.chedifier.talkbackdemo.base.BaseActivity;
import example.chedifier.talkbackdemo.base.Color;
import example.chedifier.talkbackdemo.customview.VirtualView;
import example.chedifier.talkbackdemo.talkback.CustomView;
import example.chedifier.talkbackdemo.talkback.EmptyLinearLayout;
import example.chedifier.talkbackdemo.talkback.EmptyView;
import example.chedifier.talkbackdemo.talkback.GridLayoutVirtualView;
import example.chedifier.talkbackdemo.talkback.SimpleTextView;
import example.chedifier.talkbackdemo.utils.ScreenUtils;

/**
 * Created by chedifier on 2016/9/24.
 */
public class TalkbackTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "this:" + this + " onCreate: " + (savedInstanceState == null ? "savedInstanceState=null" : savedInstanceState.toString()));

        super.onCreate(savedInstanceState);

        setContentView(getVirtulView());
    }

    private View getVirtulView(){

        LinearLayout linearLayout = new EmptyLinearLayout(this);
        linearLayout.setId(1000);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.weight = 9f;
        View view = createCustomView();
        view.setId(2001);
        linearLayout.addView(view,lp);
//        view.setVisibility(View.INVISIBLE);

        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.weight = 1f;
        final EmptyView view1  = new EmptyView(this);
        view1.setBackgroundColor(Color.DARK_GREEN.value);
        view1.setContentDescription("2002");
        view1.setId(2002);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TalkbackTest.this,"2002",Toast.LENGTH_SHORT).show();
            }
        });
        linearLayout.addView(view1,lp);

        lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.weight = 1f;
        final EmptyView view2  = new EmptyView(this);
        view2.setBackgroundColor(Color.ORANGE.value);
        view2.setContentDescription("2003");
        view2.setId(2003);
//        ((EmptyView)view).block_access_focus = true;
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TalkbackTest.this,"2003",Toast.LENGTH_SHORT).show();
            }
        });
        linearLayout.addView(view2,lp);

        /*final Handler h = new Handler(Looper.getMainLooper());
        h.postDelayed(new Runnable() {

            boolean f = false;
            @Override
            public void run() {
                if(f){
                    view1.performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,null);
//                    view2.performAccessibilityAction(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS,null);
                }else{
                    view2.performAccessibilityAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS,null);
//                    view1.performAccessibilityAction(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS,null);
                }

                f = !f;

                h.postDelayed(this,1000);
            }
        },1000);*/

        return linearLayout;
    }

    private View createCustomView(){
        GridLayoutVirtualView baseView = new GridLayoutVirtualView(this,5);
        int screenWidth = ScreenUtils.getScreenWidth();
        int margin = screenWidth/10;
        int w = (screenWidth-(margin<<1))/5;
        int h = w;

        int index = 1;
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));
        baseView.addChild(new SimpleTextView(this,w,h, String.valueOf(index++)));

        baseView.setMargin(margin,margin,margin,margin);

        baseView.setWidth(VirtualView.MATCH_PARENT);
        baseView.setHeight(h*((index+3)/5));

        View ret = new CustomView(this,baseView);
        ret.setContentDescription("自定义控件");

        return ret;
    }
}
