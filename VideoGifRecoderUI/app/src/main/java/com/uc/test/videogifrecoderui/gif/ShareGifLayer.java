package com.uc.test.videogifrecoderui.gif;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uc.framework.resources.ResTools;
import com.uc.test.videogifrecoderui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uc on 30/11/2017.
 */

public class ShareGifLayer extends FrameLayout implements View.OnClickListener{

    private TextView mTitle;
    private FrameLayout mPhotoFrame;
    private ShapeDrawable mFrameDrawable;
    private ImageView mGifViewer;
    private LinearLayout mBottomMenu;
    private IEventHandler mMenuHandler;
    private List<MenuItem> mMenuItems = new ArrayList<>();
    private final int ANIMATION_DURATION = 400;
    private final int MENU_ANIMATION_DEALY = 200;

    private Drawable mGifPreview;
    private String mGifFilePath;

    public ShareGifLayer(@NonNull Context context, IEventHandler handler) {
        super(context);

        this.mMenuHandler = handler;
        initView();
    }

    private void initView(){
        FrameLayout.LayoutParams lp;

        // title
        mTitle = new TextView(getContext());
        mTitle.setTextSize(19);
        mTitle.setText(ResTools.getUCString(R.string.video_gif_share_title));
        mTitle.setBackgroundColor(Color.TRANSPARENT);
        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        lp.topMargin = ResTools.getDimenInt(R.dimen.video_gif_share_title_top_margin);
        addView(mTitle,lp);

        //gif viewer
        mPhotoFrame = new FrameLayout(getContext());
        mFrameDrawable = new ShapeDrawable(new RectShape());
        mFrameDrawable.getPaint().setStyle(Paint.Style.STROKE);
        int frameMargin = ResTools.dpToPxI(1);
        mFrameDrawable.getPaint().setStrokeWidth(frameMargin);
        mPhotoFrame.setPadding(frameMargin,frameMargin,frameMargin,frameMargin);
        VideoGifHelper.setDrawable(mPhotoFrame,mFrameDrawable);
        mGifViewer = new ImageView(getContext());
        mPhotoFrame.addView(mGifViewer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp = new LayoutParams(ResTools.getDimenInt(R.dimen.video_gif_share_gif_view_width),
                ResTools.getDimenInt(R.dimen.video_gif_share_gif_view_height));
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        lp.topMargin = ResTools.getDimenInt(R.dimen.video_gif_share_gif_view_top_margin);
        addView(mPhotoFrame,lp);

        //bottom menus
        mBottomMenu = new LinearLayout(getContext());
        mBottomMenu.setOrientation(LinearLayout.HORIZONTAL);
        mBottomMenu.setGravity(Gravity.CENTER);
        mBottomMenu.setBackgroundColor(Color.TRANSPARENT);
        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        lp.bottomMargin = ResTools.getDimenInt(R.dimen.video_gif_share_menu_bottom_margin);
        addView(mBottomMenu,lp);
    }

    public void addMenu(int id,String name, Drawable drawable){
        MenuItem itemView = new MenuItem(getContext(),id,name,drawable);
        itemView.setOnClickListener(this);
        mBottomMenu.addView(itemView);
        mMenuItems.add(itemView);
    }

    public void setGifPreview(Drawable drawable){
        mGifPreview = drawable;
    }

    public void setGifFilePath(String gifFilePath){
        mGifFilePath = gifFilePath;
    }

    public void attachTo(ViewGroup anchor){
        if(anchor != null && anchor.indexOfChild(this) < 0){
            anchor.addView(this, ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
        }

        onThemeChanged();
        requestLayout();
    }

    public void detach(){
        ViewGroup parent = (ViewGroup)getParent();
        if(parent != null){
            parent.removeView(this);
        }

        mGifPreview = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        show();
    }

    private void show(){
        float x = 1f / (1f/(mPhotoFrame.getX() / (getMeasuredWidth() - mPhotoFrame.getX() - mPhotoFrame.getMeasuredWidth())) + 1);
        float y = 1f / (1f/(mPhotoFrame.getY() / (getMeasuredHeight() - mPhotoFrame.getY() - mPhotoFrame.getMeasuredHeight())) +1);
        ScaleAnimation scaleAnimation = new ScaleAnimation(((float)getMeasuredWidth())/mPhotoFrame.getMeasuredWidth(), 1f,
                ((float)getMeasuredHeight())/mPhotoFrame.getMeasuredHeight(),1f,
                Animation.RELATIVE_TO_SELF,x,Animation.RELATIVE_TO_SELF,y);
        scaleAnimation.setDuration(ANIMATION_DURATION);

        mGifViewer.setImageDrawable(mGifPreview);
        mPhotoFrame.setAnimation(scaleAnimation);
        scaleAnimation.start();

        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE,0f,
                Animation.ABSOLUTE,0f,
                Animation.ABSOLUTE,getMeasuredHeight() - mBottomMenu.getY(),
                Animation.ABSOLUTE,0f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f,1f);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setStartOffset(MENU_ANIMATION_DEALY);
        animationSet.setDuration(ANIMATION_DURATION);

        mBottomMenu.setAnimation(animationSet);
        animationSet.start();
    }

    public void onThemeChanged(){
        mTitle.setTextColor(ResTools.getColor("video_gif_text_color"));
        mFrameDrawable.getPaint().setColor(Color.WHITE);
        setBackgroundColor(0xC0000000);

        for(MenuItem menuItem:mMenuItems){
            menuItem.onThemeChanged();
        }
    }

    @Override
    public void onClick(View v) {
        if(mMenuHandler != null){
            mMenuHandler.onGifMenuClickEvent((MenuItem)v);
        }
    }

    public static class MenuItem extends LinearLayout{
        public final int id;
        public final String name;
        public final Drawable drawable;

        private TextView mName;

        protected MenuItem(Context context,int id,String name,Drawable drawable){
            super(context);
            this.id = id;
            this.name = name;
            this.drawable = drawable;

            setOrientation(LinearLayout.VERTICAL);
            ImageView image = new ImageView(getContext());
            image.setImageDrawable(this.drawable == null?new ColorDrawable(Color.WHITE):this.drawable);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ResTools.getDimenInt(R.dimen.video_gif_share_menu_size),
                    ResTools.getDimenInt(R.dimen.video_gif_share_menu_size));
            lp.leftMargin = ResTools.getDimenInt(R.dimen.video_gif_share_menu_horizontal_padding);
            lp.rightMargin = ResTools.getDimenInt(R.dimen.video_gif_share_menu_horizontal_padding);
            lp.bottomMargin = ResTools.getDimenInt(R.dimen.video_gif_share_menu_title_top_margin);
            this.addView(image,lp);

            mName = new TextView(getContext());
            mName.setGravity(Gravity.CENTER);
            mName.setTextSize(12);
            mName.setText(this.name);
            this.addView(mName, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }


        protected void onThemeChanged(){
            setBackgroundColor(Color.TRANSPARENT);
            mName.setTextColor(ResTools.getColor("video_gif_text_color"));
        }


    }
}
