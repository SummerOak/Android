package com.example.chedifier.previewofandroidn.multiwindows;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.chedifier.previewofandroidn.R;
import com.example.chedifier.previewofandroidn.common.DragableImagePagerAdapter;
import com.example.chedifier.previewofandroidn.common.FileUtils;
import com.example.chedifier.previewofandroidn.common.MyContentProvider;
import com.example.chedifier.previewofandroidn.common.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/29.
 */
public class MultiWindowMainListHeader {

    private ViewPager mView;

    public MultiWindowMainListHeader(Context ctx){

        mView = new ViewPager(ctx);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.dip2px(ctx,300));
        mView.setLayoutParams(lp);
        List<Uri> imagesUris = new ArrayList<>();

        imagesUris.addAll(MyContentProvider.createUris(ctx));
        imagesUris.addAll(FileUtils.getImagesFromFileProvider(ctx));

        imagesUris.add(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.image1));
        imagesUris.add(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.image3));
        imagesUris.add(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.image4));
        imagesUris.add(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.image5));
        imagesUris.add(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.drawable.image6));

        mView.setAdapter(new DragableImagePagerAdapter(ctx, imagesUris));
    }

    public View getView(){
        return mView;
    }
}
