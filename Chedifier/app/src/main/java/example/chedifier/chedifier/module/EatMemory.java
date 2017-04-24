package example.chedifier.chedifier.module;

import android.content.Context;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.NativeTest;

/**
 * Created by Administrator on 2017/1/6.
 */

public class EatMemory extends AbsModule {

    private static final int EAT_JAVA_HEAP = 1;
    private static final int EAT_NATIVE_HEAP = 2;

    private List<byte[]> mMemoryHolder = new ArrayList<byte[]>();

    public EatMemory(Context context) {
        super(context);
    }

    @Override
    protected boolean rootViewDecoratable(){
        return false;
    }

    @Override
    protected View createView(int pos) {

        Trace.beginSection("chedifier_createView");

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView eatJavaHeap = new TextView(mContext);
        decorateItem(eatJavaHeap);
        eatJavaHeap.setId(EAT_JAVA_HEAP);
        eatJavaHeap.setOnClickListener(this);
        eatJavaHeap.setOnLongClickListener(this);
        eatJavaHeap.setText("eat JavaHeap 10 MB");
        linearLayout.addView(eatJavaHeap);

        TextView eatNativeHeap = new TextView(mContext);
        decorateItem(eatNativeHeap);
        eatNativeHeap.setId(EAT_NATIVE_HEAP);
        eatNativeHeap.setOnClickListener(this);
        eatNativeHeap.setOnLongClickListener(this);
        eatNativeHeap.setText("eat NativeHeap 50 MB");
        linearLayout.addView(eatNativeHeap);

        Trace.endSection();

        return linearLayout;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG,"eatMemory ");

        switch (v.getId()){
            case EAT_JAVA_HEAP:{
                mMemoryHolder.add(new byte[1024*1024*10]);
                break;
            }

            case EAT_NATIVE_HEAP:{
                NativeTest.eatMemory(1024*1024*50);
                break;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {



        return false;
    }
}
