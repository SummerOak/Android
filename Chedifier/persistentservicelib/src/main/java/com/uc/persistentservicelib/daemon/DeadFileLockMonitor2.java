package com.uc.persistentservicelib.daemon;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chedifier on 2016/5/23.
 */
public class DeadFileLockMonitor2 extends DaemonStrategy.DeathMonitor {

    private static final String TAG = DeadFileLockMonitor.class.getSimpleName();

    private final String BINARY_DEST_DIR_NAME = "bin";
    private final String BINARY_FILE_NAME = "daemon_proc";

    private final String LOCK_DIR = "daemon";
    private final String HOST_LOCK_FILE = "host_l";
    private final String DAEMON_LOCK_FILE = "daemon_l";
    private final String HOST_TEMP_FILE = "host_t";
    private final String DAEMON_TEMP_FILE = "daemon_t";

    public DeadFileLockMonitor2(IDeathListener listener,boolean runInTargetProc) {
        super(listener, runInTargetProc);
    }

    @Override
    public void start(Context ctx, final String serviceName) {

        if(!initFiles(ctx)){
            Log.e(TAG, "init files failed!");
            return;
        }

        if(!installBinary(ctx)){
            Log.e(TAG, "install daemon proc failed!");
            return;
        }

        final String packageName = ctx.getPackageName();

        final File indicatorDir = ctx.getDir(LOCK_DIR, Context.MODE_PRIVATE);
        final File daemonProcBin = new File(ctx.getDir(BINARY_DEST_DIR_NAME, Context.MODE_PRIVATE), BINARY_FILE_NAME);

        new Thread(new Runnable() {
            @Override
            public void run() {
                startNative(packageName,
                        serviceName,
                        daemonProcBin.getAbsolutePath(),
                        new File(indicatorDir, HOST_LOCK_FILE).getAbsolutePath(),
                        new File(indicatorDir, DAEMON_LOCK_FILE).getAbsolutePath(),
                        new File(indicatorDir, HOST_TEMP_FILE).getAbsolutePath(),
                        new File(indicatorDir, DAEMON_TEMP_FILE).getAbsolutePath());

//                try {
//                    Log.d(TAG, "Runtime " + daemonProcBin.getAbsolutePath());
//                    Runtime.getRuntime().exec(new String[]{daemonProcBin.getAbsolutePath(),
//                            packageName,serviceName,
//                            new File(indicatorDir, DAEMON_LOCK_FILE).getAbsolutePath(),
//                            new File(indicatorDir, HOST_LOCK_FILE).getAbsolutePath(),
//                            new File(indicatorDir, DAEMON_TEMP_FILE).getAbsolutePath(),
//                            new File(indicatorDir, HOST_TEMP_FILE).getAbsolutePath()}).waitFor();
//
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();

    }

    private boolean installBinary(Context context){
        String binaryDirName = null;
        String abi = Build.CPU_ABI;
        if (abi.startsWith("armeabi-v7a")) {
            binaryDirName = "armeabi-v7a";
        }else if(abi.startsWith("x86")) {
            binaryDirName = "x86";
        }else{
            binaryDirName = "armeabi";
        }
        return install(context, BINARY_DEST_DIR_NAME, binaryDirName, BINARY_FILE_NAME);
    }


    private boolean install(Context context, String destDirName, String assetsDirName, String filename) {
        File file = new File(context.getDir(destDirName, Context.MODE_PRIVATE), filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            copyAssets(context, (TextUtils.isEmpty(assetsDirName) ? "" : (assetsDirName + File.separator)) + filename, file, "777");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void copyAssets(Context context, String assetsFilename, File file, String mode) throws IOException, InterruptedException {
        AssetManager manager = context.getAssets();
        final InputStream is = manager.open(assetsFilename);
        copyFile(file, is, mode);
    }

    private void copyFile(File file, InputStream is, String mode) throws IOException, InterruptedException {
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        final String abspath = file.getAbsolutePath();
        final FileOutputStream out = new FileOutputStream(file);
        byte buf[] = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        is.close();
        Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
    }

    private boolean initFiles(Context context){
        File dirFile = context.getDir(LOCK_DIR, Context.MODE_PRIVATE);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        try {
            createNewFile(dirFile, HOST_LOCK_FILE);
            createNewFile(dirFile, DAEMON_LOCK_FILE);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createNewFile(File dirFile, String fileName) throws IOException{
        File file = new File(dirFile, fileName);
        if(!file.exists()){
            file.createNewFile();
        }
    }

    protected native void startNative(String packageName,
                                      String serviceName,
                                      String daemonProcPath,
                                      String hostL,String daemonL,
                                      String hostT,String daemonT);
}
