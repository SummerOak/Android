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
        ((TextView)findViewById(R.id.test2)).setOnClickListener(this);
        ((TextView)findViewById(R.id.set_text)).setOnClickListener(this);
        ((TextView)findViewById(R.id.call_static_method)).setOnClickListener(this);
        ((TextView)findViewById(R.id.call_native)).setOnClickListener(this);
        ((TextView)findViewById(R.id.call_instance_method)).setOnClickListener(this);
        ((TextView)findViewById(R.id.post_message)).setOnClickListener(this);
    }

    public void testCall(int i,int i1,int i2,int i3,int i4,String s,String s1,byte b,byte b1,char c,char c1,float f,float f1){
        Toast.makeText(this,
                "testcall >>> \n"
                + "  i=" + i + " i1=" + i1 + " i2=" + i2 + " i3=" + i3 + " i4=" + i4 + "\n"
                + "  s=" + s + " s1=" + s1 + "\n"
                + "  b=" + b + " b1=" + b1 + "\n"
                + "  c=" + c + " c1=" + c1 + "\n"
                + "  f=" + f + " f1=" + f1
                ,Toast.LENGTH_LONG).show();

        return;
    }

    public void testCall2(String[] strings){

        String content = "";
        if(strings != null){
            for(int i=0;i<strings.length;i++){
                content += strings[i];
            }
        }

        Toast.makeText(this,
                "testcall2 >>> " + content,Toast.LENGTH_LONG).show();

        return;
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

                testCall(1,22,33,44,5,"str1","str2",(byte)3,(byte)4,'c','d',0.33f,0.22f);

                break;

            case R.id.test2:

                testCall2(new String[] {"cheng","qian","xing"});

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
