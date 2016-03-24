package com.example.chedifier.previewofandroidn.common;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class DragableImagePagerAdapter extends PagerAdapter {

    private static final String TAG = DragableImagePagerAdapter.class.getSimpleName();

    private Context mContext;
    private List<Uri> mUrisOfImages = new ArrayList<>();

    public DragableImagePagerAdapter(Context ctx, List<Uri> uris){
        mContext = ctx;
        mUrisOfImages = uris;
    }

    @Override
    public int getCount() {
        return mUrisOfImages ==null?0: mUrisOfImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView imageView = new ImageView(mContext);
        container.addView(imageView);

        imageView.setImageURI(mUrisOfImages.get(position));
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipData.Item item = new ClipData.Item(mUrisOfImages.get(position));
                ClipData data = new ClipData("chedifier.lable",new String[]{ClipDescription.MIMETYPE_TEXT_INTENT},item);


                View.DragShadowBuilder shadowBuilder = new MyDragShadowBuilder(view);

                view.startDragAndDrop(data,shadowBuilder,null,
                        View.DRAG_FLAG_GLOBAL
                );//|View.DRAG_FLAG_GLOBAL_URI_READ

                return true;
            }
        });

        return imageView;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);

    }

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        private static Bitmap mBitMap;

        public MyDragShadowBuilder(View v) {

            super(v);

            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            Bitmap bm = v.getDrawingCache();
            mBitMap = Bitmap.createScaledBitmap(bm,bm.getWidth()/2,bm.getHeight()/2,true);

        }

        @Override
        public void onProvideShadowMetrics (Point size, Point touch){

        int width, height;
        width = getView().getWidth() / 2;
        height = getView().getHeight() / 2;

        size.set(width, height);
        touch.set(width/2, height / 2);
    }

    @Override
    public void onDrawShadow(Canvas canvas) {

        canvas.drawBitmap(mBitMap,0,0,null);

    }
}
}
