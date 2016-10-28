package example.chedifier.chedifier.module;

import android.content.Context;
import android.os.Environment;
import android.os.FileObserver;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by chedifier on 2016/10/28.
 */
public class FileObserverTest extends AbsModule {

    private FileObserver mFileObserver;

    public FileObserverTest(Context context) {
        super(context);

        start();
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("文件监听");
        return textView;
    }

    private void start(){
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        f = new File(f, "ScreenShots");
        String path = f.getAbsolutePath();
        Log.i(TAG, "testFileObserver path = " + path);
        int event = FileObserver.ATTRIB | FileObserver.CLOSE_NOWRITE | FileObserver.CLOSE_WRITE |
                FileObserver.CREATE | FileObserver.DELETE | FileObserver.DELETE_SELF |
                FileObserver.MOVE_SELF | FileObserver.MOVED_FROM | FileObserver.MOVED_TO | FileObserver.OPEN;

        mFileObserver = new FileObserver(path, event) {
            @Override
            public void onEvent(int i, String s) {
                Log.i(TAG, "  >>>path = " + s + "  event = " + getEventType(i));
            }
        };
        mFileObserver.startWatching();
    }

    private String getEventType(int event){

        String type = "";
        if((event & FileObserver.ATTRIB) > 0){
            type += "ATTRIB,";
        }
        if((event & FileObserver.CLOSE_NOWRITE) > 0){
            type += "CLOSE_NOWRITE,";
        }
        if((event & FileObserver.CLOSE_WRITE) > 0){
            type += "CLOSE_WRITE,";
        }
        if((event & FileObserver.CREATE) > 0){
            type += "CREATE,";
        }
        if((event & FileObserver.DELETE) > 0){
            type += "DELETE,";
        }if((event & FileObserver.DELETE_SELF) > 0){
            type += "DELETE_SELF,";
        }
        if((event & FileObserver.MOVE_SELF) > 0){
            type += "MOVE_SELF,";
        }
        if((event & FileObserver.MOVED_FROM) > 0){
            type += "MOVED_FROM,";
        }
        if((event & FileObserver.MOVED_TO) > 0){
            type += "MOVED_TO,";
        }
        if((event & FileObserver.OPEN) > 0){
            type += "OPEN,";
        }

        return event + " :"+type;
    }

    @Override
    public void onClick(View v) {
        String flagFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/chedifier/yunos_flag1";
        Log.d(TAG,flagFile);

        File file = new File(flagFile);
        Log.d(TAG,(file.exists()?"exist":"not exist") + "  " + flagFile);

    }
}
