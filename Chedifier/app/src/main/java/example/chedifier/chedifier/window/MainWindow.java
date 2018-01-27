package example.chedifier.chedifier.window;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.module.EatMemory;
import example.chedifier.chedifier.module.GetPackageSignatureSha1;
import example.chedifier.chedifier.module.NativeTestModule;
import example.chedifier.chedifier.module.OpenGLTest;
import example.chedifier.chedifier.module.PhoneInfo;
import example.chedifier.chedifier.module.SkyWalkerTester;
import example.chedifier.chedifier.module.TalkbackModule;
import example.chedifier.chedifier.window.common.AbsWindow;

/**
 * Created by Administrator on 2017/3/6.
 */

public class MainWindow extends AbsWindow {

    private Context mContext;
    private List<AbsModule> mModules = new ArrayList<>();


    public MainWindow(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected View onCreateContent() {
        prepareTestModules();
        ScrollView scrollView = new ContentView(mContext);
        LinearLayout moduleContainer = new LinearLayout(mContext);
        moduleContainer.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(moduleContainer);
        initModules(moduleContainer);
        return scrollView;
    }


    private void prepareTestModules(){
        mModules.add(new PhoneInfo(mContext));
        mModules.add(new GetPackageSignatureSha1(mContext));
        mModules.add(new OpenGLTest(mContext));
        mModules.add(new EatMemory(mContext));
        mModules.add(new NativeTestModule(mContext));
        mModules.add(new TalkbackModule(mContext));
        mModules.add(new SkyWalkerTester(mContext));
    }

    private void initModules(ViewGroup container){

        if(container != null){
            for(int i=0;i<mModules.size();i++){
                AbsModule module = mModules.get(i);
                if(module != null){
                    container.addView(module.getView(i));

                    View divider = new View(mContext);
                    divider.setBackgroundColor(0x22ededed);
                    container.addView(divider,ViewGroup.LayoutParams.MATCH_PARENT,1);
                }
            }
        }

    }

    private static class ContentView extends ScrollView{

        public ContentView(Context context) {
            super(context);

            post(new Runnable() {
                @Override
                public void run() {
                    ViewParent p = ContentView.this;
                    while(p != null){
                        Log.d("cqx","" + p);
                        p = p.getParent();
                    }
                }
            });
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);


        }

        @Override
        public void draw(Canvas canvas) {
//            Log.d("ContentView","draw");
            super.draw(canvas);
        }
    }

}
