package example.chedifier.hook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

import example.chedifier.hook.base.BaseActivity;
import example.chedifier.hook.ptrace.PTrace;
import example.chedifier.hook.ptrace.PTraceService;
import example.chedifier.hook.utils.Utils;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private EditText mPTraceIdInput;

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

        mPTraceIdInput = (EditText)findViewById(R.id.trace_edit);
        ((TextView)findViewById(R.id.trace_pid)).setOnClickListener(this);

        ((TextView)findViewById(R.id.trace_self)).setOnClickListener(this);
        ((TextView)findViewById(R.id.stop_trace_self)).setOnClickListener(this);
        ((TextView)findViewById(R.id.start_target)).setOnClickListener(this);
        ((TextView)findViewById(R.id.start_activity)).setOnClickListener(this);
    }

    public void testSimple(int i) {
        Toast.makeText(this,
                "testcall >>> \n"
                        + "  i=" + i
                , Toast.LENGTH_LONG).show();
        return;
    }

    public static void testCall(int i,int i1,int i2,int i3,int i4,String s,String s1,byte b,byte b1,char c,char c1,float f,float f1) {
        Toast.makeText(HookApplication.getAppContext(),
                "testcall >>> \n"
                        + "  i=" + i + " i1=" + i1 + " i2=" + i2 + " i3=" + i3 + " i4=" + i4 + "\n"
                        + "  s=" + s + " s1=" + s1 + "\n"
                        + "  b=" + b + " b1=" + b1 + "\n"
                        + "  c=" + c + " c1=" + c1 + "\n"
                        + "  f=" + f + " f1=" + f1
                        , Toast.LENGTH_LONG).show();
        return;
    }

    public static void testStaticCall(int i,int i1,int i2,int i3,int i4,String s,String s1,byte b,byte b1,char c,char c1,float f,float f1) {
        Toast.makeText(HookApplication.getAppContext(),
                "testStaticCall >>> \n"
                        + "  i=" + i + " i1=" + i1 + " i2=" + i2 + " i3=" + i3 + " i4=" + i4 + "\n"
                        + "  s=" + s + " s1=" + s1 + "\n"
                        + "  b=" + b + " b1=" + b1 + "\n"
                        + "  c=" + c + " c1=" + c1 + "\n"
                        + "  f=" + f + " f1=" + f1
                , Toast.LENGTH_LONG).show();
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

    public void testPrivData(PrivData data){
        Toast.makeText(HookApplication.getAppContext(),
                "testPrivData >>> \n"
                        + "  data.i1=" + data.i1
                        + "  data.s1=" + data.s1
                , Toast.LENGTH_LONG).show();
        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test:

                testSimple(886);
                //testCall(1,22,33,44,5,"str1","str2",(byte)3,(byte)4,'c','d',0.33f,0.22f);

                break;

            case R.id.test2:

                testCall2(new String[] {"cheng","qian","xing"});

                break;

            case R.id.call_static_method:
                testStaticCall(1,22,33,44,5,"str1","str2",(byte)3,(byte)4,'c','d',0.33f,0.22f);
                break;

            case R.id.call_native:

                break;

            case R.id.call_instance_method:
                testPrivData(new PrivData(226,"337"));
                break;

            case R.id.post_message:
                Utils.animateViewLoop(v);
                break;

            case R.id.set_text:

                ((TextView)v).setText("chedifier");

//                Utils.copy(new File(this.getDir()));

                Log.d(TAG,"" + v);

                break;
            case R.id.trace_pid:

                if(mPTraceIdInput != null){
                    int pid = Integer.valueOf(mPTraceIdInput.getText().toString());
                    if(pid > 0){
                        PTrace.pTrace(pid);
                    }
                }

                break;

            case R.id.trace_self:
                PTraceService.startPTrace(this, Process.myPid());
                break;

            case R.id.stop_trace_self:
                PTraceService.stopPTrace(this);
                break;

            case R.id.start_target:

                new Thread(new Runnable() {
                    int i = 0;
                    @Override
                    public void run() {
                        while(true){
                            System.out.println("chedifier hook test " + (i++));

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                break;

            case R.id.start_activity:
                startActivity(new Intent(this,MainActivity.class));
                break;

        }
    }

    private class PrivData{
        public int i1 = 1;
        public String s1 = "abc";

        public PrivData(int i1,String s1){
            this.i1 = i1;
            this.s1 = s1;
        }
    }
}
