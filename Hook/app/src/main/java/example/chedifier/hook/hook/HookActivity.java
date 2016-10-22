package example.chedifier.hook.hook;

import android.os.Bundle;
import android.widget.TextView;

import example.chedifier.hook.base.BaseActivity;

/**
 * Created by chedifier on 2016/10/22.
 */
public class HookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("HookActivity");
        textView.setTextSize(32);
        setContentView(textView);
    }
}
