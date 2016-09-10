package example.chedifier.hook;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import example.chedifier.hook.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.test)).setOnClickListener(this);
        ((TextView)findViewById(R.id.set_text)).setOnClickListener(this);
        ((TextView)findViewById(R.id.call_static_method)).setOnClickListener(this);
        ((TextView)findViewById(R.id.call_native)).setOnClickListener(this);
        ((TextView)findViewById(R.id.call_instance_method)).setOnClickListener(this);
        ((TextView)findViewById(R.id.post_message)).setOnClickListener(this);
    }

    public void test(int i,String conent,byte b,char c){
        Toast.makeText(this,
                "test " + i
                + " ,content: " + conent
                + " ,byte: " + b
                + " ,char: " + c,Toast.LENGTH_SHORT).show();
    }

    public void testHooked(int i){
        Toast.makeText(this,"test " + i,Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context ctx,String content){
        Toast.makeText(ctx,content,Toast.LENGTH_SHORT).show();
    }

    public static void toastHooked(Context ctx,String content){
        Toast.makeText(ctx,content + " hooked!",Toast.LENGTH_SHORT).show();
    }

    public void toast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }

    public void toastHooked(String content){
        Toast.makeText(this,content + " hooked",Toast.LENGTH_SHORT).show();
    }

    public void logcatHook(String content){
        Log.d(TAG,"force closed!" + content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test:

                test(668,"chedifier_hook",(byte)3,'c');

                break;

            case R.id.call_static_method:
                toast(this,"static toast!");
                break;

            case R.id.call_native:

                break;

            case R.id.call_instance_method:
                toast("instance method!");
                break;

            case R.id.post_message:

                break;

            case R.id.set_text:

                ((TextView)v).setText("chedifier");

                break;


        }
    }
}
