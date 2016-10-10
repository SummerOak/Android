package example.chedifier.chedifier;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

/**
 * Created by chedifier on 2016/9/24.
 */
public class ChedifierAccessibilityService extends AccessibilityService {

    private static final String TAG = "ChedifierAccessibilityService";

    public static int index = 0;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();


    }

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event != null){
            Log.d(TAG,  "  package : " + event.getPackageName() +
                    "  event type : " + AccessibilityEvent.eventTypeToString(event.getEventType()) +
                    "  event time : " + event.getEventTime()
            );

            AccessibilityNodeInfo nodeInfo = event.getSource();
            if(nodeInfo != null){
                Log.d(TAG,"node name: " + nodeInfo.getClassName()
                        + " ContentDescription " + nodeInfo.getContentDescription()
                + " nodeInfo.getViewIdResourceName() " + nodeInfo.getViewIdResourceName());


//                nodeInfo.setError("jjjdj");

//                if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
//                    TextView textView = new TextView(this);
//
//                    textView.setText(String.valueOf(++index));
//                    textView.setTextSize(33);
//                    textView.setLayoutParams(new ViewGroup.LayoutParams(200,200));
//
//                    nodeInfo.addChild(textView);
//                }

            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
