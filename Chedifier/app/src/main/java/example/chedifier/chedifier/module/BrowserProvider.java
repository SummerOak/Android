package example.chedifier.chedifier.module;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import example.chedifier.base.utils.SystemUtils;
import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/1/6.
 */

public class BrowserProvider extends AbsModule {


//    public final static String CTS_AUTHORITIES = "com.uc.cts.BrowserProvider";
    public final static String CTS_AUTHORITIES = "com.android.browser";


    public BrowserProvider(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("读取BrowserProvider");
        return textView;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG,"query BrowserProvider");
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = new Uri.Builder()
                .authority(CTS_AUTHORITIES)
                .scheme("content")
                .appendPath("search_suggest_query")
//                .appendPath("history")
                .build();

        try {
            Cursor cursor = resolver.query(uri,null,null,null,null);
            if(cursor == null){
                Log.i(TAG,"cursor is null");
            }
        }catch (Throwable t){

            Toast.makeText(mContext,t.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
            t.printStackTrace();
        }
    }


}
