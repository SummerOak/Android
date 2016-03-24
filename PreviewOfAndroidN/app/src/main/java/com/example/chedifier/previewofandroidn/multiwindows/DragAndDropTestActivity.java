package com.example.chedifier.previewofandroidn.multiwindows;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;
import com.example.chedifier.previewofandroidn.common.FileUtils;
import com.example.chedifier.previewofandroidn.common.DragableImagePagerAdapter;
import com.example.chedifier.previewofandroidn.common.MyContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/15.
 */
public class DragAndDropTestActivity extends BaseActivity implements View.OnDragListener{

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drag_and_drop_activity);


        initView();
    }

    private void initView(){

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);



        List<Uri> imagesUris = new ArrayList<>();

        imagesUris.addAll(MyContentProvider.createUris(this));
        imagesUris.addAll(FileUtils.getImagesFromFileProvider(this));

        imagesUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image1));
        imagesUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image2));
        imagesUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image3));
        imagesUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image4));
        imagesUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image5));
        imagesUris.add(Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.image6));

        viewPager.setAdapter(new DragableImagePagerAdapter(this, imagesUris));

        mImageView = (ImageView)findViewById(R.id.container);
        mImageView.setOnDragListener(this);

    }

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
                    ClipData.Item item = data.getItemAt(0);
                    if(item != null){
                        Uri uri = item.getUri();


                        Log.d(TAG,uri.toString());
                        Log.d(TAG,"uri.getEncodedAuthority(): "+uri.getEncodedAuthority());


                        mImageView.setImageURI(uri);
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
