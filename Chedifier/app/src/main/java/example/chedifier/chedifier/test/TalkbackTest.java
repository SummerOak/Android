package example.chedifier.chedifier.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.BaseActivity;
import example.chedifier.chedifier.utils.ScreenUtils;

/**
 * Created by chedifier on 2016/9/24.
 */
public class TalkbackTest extends BaseActivity {

    private RelativeLayout mRoot;
    private LinearLayout mToolBar;

    private MyAccessibilityDelegate mAccessibilityDelegate;


    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "this:" + this + " onCreate: " + (savedInstanceState == null ? "savedInstanceState=null" : savedInstanceState.toString()));


        super.onCreate(savedInstanceState);


        setContentView(R.layout.talkback_testact);
        mWebView = (WebView) findViewById(R.id.web_webview);

        String htmlContent = "<html><head><title>Title of the page</title></head><body><h1>Heading of some awesome article</h1><p>Some awesome paragraph to display.</p></body></html>";
        mWebView.loadData(htmlContent, "text/html", "utf-8");

//        mAccessibilityDelegate = new MyAccessibilityDelegate();
//
//        mRoot = new RelativeLayout(this);
//        mRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            mRoot.setAccessibilityDelegate(mAccessibilityDelegate);
//        }
//
//        mToolBar = new ToolbarLayout(this);
//        mToolBar.setBackgroundColor(0xff000066);
//        mToolBar.setContentDescription("jjjjjjj");
//
//        LinearLayout.LayoutParams tLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        tLp.weight = 1;
//
//        mToolBar.addView(createToolbarItem("1"),tLp);
//        mToolBar.addView(createToolbarItem("2"),tLp);
//        mToolBar.addView(createToolbarItem("3"),tLp);
//
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                (int)ScreenUtils.dipToPixels(40));
//        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//
//        lp.bottomMargin = (int)ScreenUtils.dipToPixels(16);
//        lp.leftMargin = (int)ScreenUtils.dipToPixels(16);
//        lp.rightMargin = (int)ScreenUtils.dipToPixels(16);
//        mToolBar.setLayoutParams(lp);
//
//
//        mRoot.addView(mToolBar);
//
//        setContentView(mRoot);
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    class MyAccessibilityDelegate extends View.AccessibilityDelegate {

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View child, AccessibilityEvent event) {

            Log.d(TAG,"viewGroup " + viewGroup + " child " + child + " event " + event);

            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                //Do stuff in here!  Maybe also do different stuff when focus is cleared!
            }

            return super.onRequestSendAccessibilityEvent(viewGroup, child, event);
        }


    }

}
