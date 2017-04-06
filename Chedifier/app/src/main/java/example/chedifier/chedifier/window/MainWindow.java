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
import example.chedifier.chedifier.module.AccsTest;
import example.chedifier.chedifier.module.BackgroundTest;
import example.chedifier.chedifier.module.BlockMainThread;
import example.chedifier.chedifier.module.BrowserProvider;
import example.chedifier.chedifier.module.CloudSyncOrderTest;
import example.chedifier.chedifier.module.CopySelf;
import example.chedifier.chedifier.module.CoverPanel;
import example.chedifier.chedifier.module.EatMemory;
import example.chedifier.chedifier.module.EdittextError;
import example.chedifier.chedifier.module.ExtHandlerText;
import example.chedifier.chedifier.module.FileObserverTest;
import example.chedifier.chedifier.module.GetPackageSignatureSha1;
import example.chedifier.chedifier.module.HWAccTest;
import example.chedifier.chedifier.module.HWBlackListTest;
import example.chedifier.chedifier.module.HWThemeChangeTest;
import example.chedifier.chedifier.module.MultiTextTest;
import example.chedifier.chedifier.module.NativeTestModule;
import example.chedifier.chedifier.module.NotificationTest;
import example.chedifier.chedifier.module.OOMTest;
import example.chedifier.chedifier.module.OpenGLTest;
import example.chedifier.chedifier.module.OpenUrlByDefaultBrowser;
import example.chedifier.chedifier.module.PreWindowTest;
import example.chedifier.chedifier.module.QSChouModule;
import example.chedifier.chedifier.module.RemoveViewInSubThread;
import example.chedifier.chedifier.module.ShortCutTest;
import example.chedifier.chedifier.module.SkyWalkerTester;
import example.chedifier.chedifier.module.StartBrowserTest;
import example.chedifier.chedifier.module.TalkbackModule;
import example.chedifier.chedifier.module.WindowLeakTest;
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
        mModules.add(new CopySelf(mContext));
        mModules.add(new CloudSyncOrderTest(mContext));
        mModules.add(new GetPackageSignatureSha1(mContext));
        mModules.add(new OpenGLTest(mContext));
        mModules.add(new RemoveViewInSubThread(mContext));
        mModules.add(new HWBlackListTest(mContext));
        mModules.add(new CoverPanel(mContext));
        mModules.add(new HWAccTest(mContext));
        mModules.add(new QSChouModule(mContext));
        mModules.add(new EatMemory(mContext));
        mModules.add(new BrowserProvider(mContext));
        mModules.add(new ShortCutTest(mContext));
        mModules.add(new AccsTest(mContext));
        mModules.add(new ExtHandlerText(mContext));
        mModules.add(new OOMTest(mContext));
        mModules.add(new NativeTestModule(mContext));
        mModules.add(new BackgroundTest(mContext));
        mModules.add(new NotificationTest(mContext));
        mModules.add(new OpenUrlByDefaultBrowser(mContext));
        mModules.add(new MultiTextTest(mContext));
        mModules.add(new EdittextError(mContext));
        mModules.add(new PreWindowTest(mContext));
        mModules.add(new StartBrowserTest(mContext));
        mModules.add(new WindowLeakTest(mContext));
        mModules.add(new HWThemeChangeTest(mContext));
        mModules.add(new TalkbackModule(mContext));
        mModules.add(new FileObserverTest(mContext));
        mModules.add(new SkyWalkerTester(mContext));
        mModules.add(new BlockMainThread(mContext));
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
