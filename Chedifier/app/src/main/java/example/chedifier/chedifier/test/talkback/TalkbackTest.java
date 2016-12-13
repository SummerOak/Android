package example.chedifier.chedifier.test.talkback;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.chedifier.chedifier.base.BaseActivity;
import example.chedifier.chedifier.base.Color;
import example.chedifier.chedifier.test.ToolbarItem;
import example.chedifier.hook.hook.HookParaser;

/**
 * Created by chedifier on 2016/9/24.
 */
public class TalkbackTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "this:" + this + " onCreate: " + (savedInstanceState == null ? "savedInstanceState=null" : savedInstanceState.toString()));


        super.onCreate(savedInstanceState);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                HookParaser.parseAndHook(HookTalkbackProxy.class);
            }
        },2000);

        setContentView(getVirtulView());
    }

    private int a = 0;
    private View getVirtulView(){

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp1.weight = 1f;

        final View v1 = createEmptyView(Color.DARK_GREEN);
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"kkkkkkkkkk");
                v1.setContentDescription("第 " + a + "次点击");
                v1.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
                v1.sendAccessibilityEvent(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            }
        });
        v1.setId(1001);
        linearLayout.addView(v1,lp1);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        lp.weight = 5f;
        View customView = createCustomView();
        customView.setId(2001);
        linearLayout.addView(customView,lp);

        View v2 = createEmptyViewGroup(Color.DARK_PURPLE);
        v2.setId(1002);
        linearLayout.addView(v2,lp1);

        View v3 = createEmptyViewGroup(Color.DARK_BLUE);
        v3.setId(1003);
        linearLayout.addView(v3,lp1);

        View v4 = createEmptyViewGroup(Color.TEAL);
        v4.setId(1004);
        linearLayout.addView(v4,lp1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            v1.setAccessibilityTraversalAfter(v3.getId());
            v3.setAccessibilityTraversalAfter(v2.getId());
            v2.setAccessibilityTraversalAfter(v4.getId());
            v4.setAccessibilityTraversalAfter(customView.getId());
            customView.setAccessibilityTraversalAfter(v1.getId());
        }

        return linearLayout;
    }

    private View createCustomView(){
        GridLayoutVirtualView baseView = new GridLayoutVirtualView(this,3);
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.BLUE.value));
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.TEAL.value));
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.GREEN.value));
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.DARK_GREEN.value));
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.MAGENTA.value));
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.DARK_BLUE.value));
        baseView.addChild(new SimpleColorPieceWithDesc(this,200,200, Color.YELLOR.value));

        baseView.setContentDescription("测试控件1");
        baseView.setMargin(200,150,200,150);

        View ret = new CustomView(this,baseView);
        ret.setContentDescription("自定义控件");

        return ret;
    }

    private View createEmptyViewGroup(Color color){
        View viewGroup = new EmptyViewGroup(this);
        viewGroup.setContentDescription(color.desc);
        viewGroup.setBackgroundColor(color.value);
        return viewGroup;
    }

    private View createEmptyView(Color color){
        View v = new EmptyView(this);
        v.setContentDescription(color.desc);
        v.setBackgroundColor(color.value);
        return v;
    }

    private ToolbarItem createToolbarItem(String name){
        TextView toolBarItem = new TextView(this);
        toolBarItem.setText(name);
        toolBarItem.setContentDescription("1");
        toolBarItem.setGravity(Gravity.CENTER);

        ToolbarItem item = new ToolbarItem(this);
        item.addView(toolBarItem,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        item.setEnabled(true);
        item.setFocusable(true);
        item.setClickable(true);
        item.setSoundEffectsEnabled(false);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        return item;
    }

}
