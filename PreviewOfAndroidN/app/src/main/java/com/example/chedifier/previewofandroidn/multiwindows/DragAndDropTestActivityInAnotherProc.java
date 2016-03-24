package com.example.chedifier.previewofandroidn.multiwindows;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.DropPermissions;
import android.view.View;
import android.widget.ImageView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by chedifier on 2016/3/15.
 */
public class DragAndDropTestActivityInAnotherProc extends BaseActivity{

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageView = new ImageView(this);
        mImageView.setPadding(50,50,50,50);
        mImageView.setImageResource(R.drawable.image1);

        setContentView(mImageView);


        mImageView.setOnDragListener(new MyDragListener());

    }

    private class MyDragListener implements View.OnDragListener{

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d(TAG,"onDrag: ACTION_DRAG_STARTED");

                    if(dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_INTENT)){
                        Log.d(TAG,"onDrag: hited");

                        mImageView.setBackgroundColor(0xffffdead);
                        mImageView.invalidate();

                        return true;
                    }

                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d(TAG,"onDrag: ACTION_DRAG_ENTERED");

                    mImageView.setBackgroundColor(0xff00ff00);

                    break;

                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.d(TAG,"onDrag: ACTION_DRAG_LOCATION");

                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d(TAG,"onDrag: ACTION_DRAG_EXITED");

                    mImageView.setBackgroundColor(0xffffdead);

                    break;

                case DragEvent.ACTION_DROP:
                    Log.d(TAG,"onDrag: ACTION_DROP");

                    ClipData data = dragEvent.getClipData();
                    if(data != null){
//                        ClipData.Item item = data.getItemAt(0);
//                        if(item!=null&&item.getIntent()!=null){
//                            Intent it = item.getIntent();
//                            byte[] bytes = it.getExtras().getByteArray("image");
//                            if(bytes != null){
//                                Log.d(TAG,"onDrag: ACTION_DROP");
//                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                                mImageView.setImageBitmap(bitmap);
//                            }
//                        }

                        ClipData.Item item = data.getItemAt(0);
                        if(item != null){
                            Uri uri = item.getUri();

                            Log.d(TAG,uri.toString());
                            Log.d(TAG,"uri.getEncodedAuthority(): "+uri.getEncodedAuthority());
                            Log.d(TAG,"uri.getHost(): " + uri.getHost());
                            Log.d(TAG,"uri.getPath(): " + uri.getPath());
                            Log.d(TAG,"uri.getQuery(): " + uri.getQuery());
                            Log.d(TAG,"uri.getQueryParameter(\"imagename\"): " + uri.getQueryParameter("imagename"));
                            Log.d(TAG,"uri.getScheme(): " + uri.getScheme());
                            Log.d(TAG,"uri.getUserInfo(): " + uri.getUserInfo());

                            try {
                                InputStream inputStream = getContentResolver().openInputStream(uri);
                                Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());
                                mImageView.setImageDrawable(drawable);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d(TAG,"onDrag: ACTION_DRAG_ENDED");

                    mImageView.setBackgroundColor(0xffededed);

                    break;

                default:
                    Log.e(TAG,"Unknown action type received by OnDragListener.");
                    break;
            }

            return true;
        }
    }
}
