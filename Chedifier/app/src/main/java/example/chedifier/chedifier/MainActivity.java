package example.chedifier.chedifier;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import example.chedifier.base.utils.SystemUtils;
import example.chedifier.chedifier.base.BaseActivity;
import example.chedifier.chedifier.multiuser.MultiUserManager;
import example.chedifier.chedifier.utils.PermissionUtils;
import example.chedifier.chedifier.window.MainWindow;
import example.chedifier.chedifier.window.common.UIEnvironment;

public class MainActivity extends BaseActivity {

    private Handler mH;

    private static UIEnvironment sEnv;

    private boolean pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        sEnv = new UIEnvironment(this);
        setContentView(sEnv.getView());

//        setContentView(R.layout.talkback_testact);

        getEnv().pushWindow(new MainWindow(this));

        MultiUserManager.getInstance().registerUserChangeListener(getApplicationContext());

        Log.d("cqx_lan", SystemUtils.getLanguage());

        mH = new Handler(Looper.getMainLooper());

        PermissionUtils.checkSdcardPermission(this, new String[]{
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.READ_PHONE_STATE"});

//        getWindow().getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                if(pre){
//                    Log.i("cqx_bp","onPreDraw false");
//                    return false;
//                }
//
//                mH.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mH.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                pre = true;
//                                Log.i("cqx_bp","pre = true");
//                            }
//                        });
//                    }
//                },5000);
//
//                Log.i("cqx_bp","onPreDraw true");
//                return true;
//            }
//        });

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
    public void onBackPressed() {
        if(sEnv != null && sEnv.popWindow()){
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pre = false;
        Log.i("cqx_bp","pre = false");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MyApplication.stopProcess();

        sEnv = null;
    }

    @Override
    public void startActivity(Intent intent) {
        try{
            super.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static UIEnvironment getEnv(){
        return sEnv;
    }

}
