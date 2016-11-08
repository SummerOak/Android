package example.chedifier.hook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by chedifier on 2016/10/22.
 */
public class HookActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setText("HookActivity");
        textView.setTextSize(32);
        setContentView(textView);
    }
}
