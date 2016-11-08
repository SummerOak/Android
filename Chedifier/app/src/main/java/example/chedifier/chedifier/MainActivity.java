package example.chedifier.chedifier;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.base.utils.SystemUtils;
import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.base.BaseActivity;
import example.chedifier.chedifier.module.AccsTest;
import example.chedifier.chedifier.module.BackgroundTest;
import example.chedifier.chedifier.module.CopySelf;
import example.chedifier.chedifier.module.EdittextError;
import example.chedifier.chedifier.module.FileObserverTest;
import example.chedifier.chedifier.module.HWThemeChangeTest;
import example.chedifier.chedifier.module.MultiTextTest;
import example.chedifier.chedifier.module.NotificationTest;
import example.chedifier.chedifier.module.OpenUrlByDefaultBrowser;
import example.chedifier.chedifier.module.PreWindowTest;
import example.chedifier.chedifier.module.ShortCutTest;
import example.chedifier.chedifier.module.SkyWalkerTester;
import example.chedifier.chedifier.module.StartBrowserTest;
import example.chedifier.chedifier.module.TalkbackModule;
import example.chedifier.chedifier.module.WindowLeakTest;
import example.chedifier.chedifier.multiuser.MultiUserManager;
import example.chedifier.chedifier.utils.PermissionUtils;

public class MainActivity extends BaseActivity {

    private Handler mH;

    private List<AbsModule> mModules = new ArrayList<>();

    private void prepareTestModules(){
        mModules.add(new CopySelf(this));
        mModules.add(new AccsTest(this));
        mModules.add(new BackgroundTest(this));
        mModules.add(new NotificationTest(this));
        mModules.add(new OpenUrlByDefaultBrowser(this));
        mModules.add(new MultiTextTest(this));
        mModules.add(new EdittextError(this));
        mModules.add(new PreWindowTest(this));
        mModules.add(new ShortCutTest(this));
        mModules.add(new StartBrowserTest(this));
        mModules.add(new WindowLeakTest(this));
        mModules.add(new HWThemeChangeTest(this));
        mModules.add(new TalkbackModule(this));
        mModules.add(new FileObserverTest(this));
        mModules.add(new SkyWalkerTester(this));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareTestModules();
        ScrollView scrollView = new ScrollView(this);
        LinearLayout moduleContainer = new LinearLayout(this);
        moduleContainer.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(moduleContainer);
        initModules(moduleContainer);
        setContentView(scrollView);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));

        MultiUserManager.getInstance().registerUserChangeListener(getApplicationContext());

        Log.d("cqx_lan", SystemUtils.getLanguage());

        mH = new Handler(Looper.getMainLooper());

        PermissionUtils.checkSdcardPermission(this, new String[]{
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.READ_PHONE_STATE"});
    }

    private void initModules(ViewGroup container){

        if(container != null){
            for(int i=0;i<mModules.size();i++){
                AbsModule module = mModules.get(i);
                if(module != null){
                    container.addView(module.getView(i));
                }
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("cqx","onConfigurationChanged newConfig = " + newConfig);

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PermissionUtils.PERMISSION_REQCODE_1:{
                for(int i=0;i<permissions.length;i++){
                    Log.i("pms","permission" + i + permissions[i]);
                }

                for(int i=0;i<grantResults.length;i++){
                    Log.i("pms","grantResults" + i + "" + grantResults[i]);
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,permissions[i] + "not granted.",Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }

                break;
            }


        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

//        MyApplication.stopProcess();
    }

    @Override
    public void startActivity(Intent intent) {
        try{
            super.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
