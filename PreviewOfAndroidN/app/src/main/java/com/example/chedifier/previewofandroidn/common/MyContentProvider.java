package com.example.chedifier.previewofandroidn.common;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/16.
 */
public class MyContentProvider extends ContentProvider{

    private static final String TAG = MyContentProvider.class.getSimpleName();

    protected static final String AUTHORITY = "com.example.chedifier.previewofandroidn.mycontentprovider";
    private static final String CONTENT_URI = "content://" + AUTHORITY + "/provider/images";

    private static final String PARAM_KEY = "image_path";

    @Override
    public boolean onCreate() {
        Log.d(TAG,"onCreate");

        return false;
    }

    public static Uri createUriByName(Context ctx,String imageName){
        return Uri.parse(CONTENT_URI + "?" + PARAM_KEY + "=" + ctx.getFilesDir() + "/images/" + imageName);
    }

    public static Uri createUriByPath(String path){
        return Uri.parse(CONTENT_URI + "?" + PARAM_KEY + "=" + path);
    }

    public static List<Uri> createUris(Context ctx){
        List<Uri> uris = new ArrayList<>();
        File dir = new File(ctx.getFilesDir()+ "/images");
        if(dir.exists() && dir.isDirectory()){

            File[] files = dir.listFiles();
            if(files != null){
                for(int i=0;i<files.length;i++){
                    if(files[i].isFile()){
                        uris.add(createUriByName(ctx,files[i].getName()));
                    }
                }
            }

        }

        return uris;
    }

    private File getFileForUri(Uri uri){
        String path = uri.getQueryParameter(PARAM_KEY);
        return new File(path);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Log.d(TAG,"query uri: " + uri.toString());

        String path = uri.getQueryParameter(PARAM_KEY);
        Log.d(TAG,"path: " + path);

        Bitmap bm = BitmapFactory.decodeFile(path);
        Bitmap[] bitmaps = new Bitmap[1];
        bitmaps[0] = bm;

        MatrixCursor cursor = new MatrixCursor(new String[]{"bitmap"},1);
        cursor.addRow(bitmaps);

        return cursor;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        Log.d(TAG,"openFile uri " + uri);

        String path = uri.getQueryParameter(PARAM_KEY);
        File f = new File(path);
        return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d(TAG,"getType uri " + uri);

        // ContentProvider has already checked granted permissions
        final File file = getFileForUri(uri);

        final int lastDot = file.getName().lastIndexOf('.');
        if (lastDot >= 0) {
            final String extension = file.getName().substring(lastDot + 1);
            final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
        }

        return "application/octet-stream";
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
