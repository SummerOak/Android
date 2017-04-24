package example.chedifier.chedifier.module;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/4/20.
 */

public class ProviderTest extends AbsModule {

//    private final String AUTHORITY = "com.android.browser";
    private final String AUTHORITY = "com.uc.cts.BrowserProvider";

    private final int QUERY = 1;
    private final int DELETE = 2;

    public ProviderTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView query = new TextView(mContext);
        decorateItem(query);
        query.setId(QUERY);
        query.setOnClickListener(this);
        query.setOnLongClickListener(this);
        query.setText("query");
        linearLayout.addView(query);

        TextView delete = new TextView(mContext);
        decorateItem(delete);
        delete.setId(DELETE);
        delete.setOnClickListener(this);
        delete.setOnLongClickListener(this);
        delete.setText("delete");
        linearLayout.addView(delete);

        return linearLayout;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case QUERY:
                queryBookmark();
                break;
            case DELETE:
                deleteBookmarks();
                break;
        }

    }

    private void deleteBookmarks(){
        Uri uri = new Uri.Builder()
                .authority(AUTHORITY)
                .scheme("content")
                .appendPath("bookmarks")
                .build();

        try {
            int del = mContext.getContentResolver().delete(uri,"",null);
            Log.i(TAG,"delete " + del + " bookmarks");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void queryBookmark(){
        final String CLOUMN_LUID = "title";
        String[] projection = new String[]{CLOUMN_LUID};
        Cursor cursor = null;

        Uri uri = new Uri.Builder()
                .authority(AUTHORITY)
                .scheme("content")
                .appendPath("bookmarks")
                .build();

        try {
            cursor = mContext.getContentResolver().query(uri,projection,null,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (cursor != null) {
            while(cursor.moveToNext()){
                int column_index = cursor.getColumnIndexOrThrow(CLOUMN_LUID);
                Log.i(TAG,cursor.getString(column_index));
            }
        }else{
            Log.i(TAG,"query failed!");
        }
    }
}
