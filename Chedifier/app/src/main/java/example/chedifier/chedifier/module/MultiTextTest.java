package example.chedifier.chedifier.module;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.utils.ScreenUtils;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : MultiTextTest
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class MultiTextTest extends AbsModule {

//    private String url = "http://www.baidu.com";
    private String url = "http://www.youku.com";

    private String pkg = "com.android.browser";

//    private String pkg = "com.android.browser";

    public MultiTextTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        linearLayout.addView(createMultiTextWithDefalutBrowser());//（未指定包名，默认）
        linearLayout.addView(createMultiTextWithSpecifyBrowser());//（指定包名，默认）
        linearLayout.addView(createMultiTextWithUCPolicy("UCM_NEW_WINDOW"));//（指定包名，指定新建窗口）
        linearLayout.addView(createMultiTextWithUCPolicy("UCM_REUSE_WHEN_MAX"));//（指定包名，指定复用窗口当达到上限）
        linearLayout.addView(createMultiTextWithUCPolicy("UCM_NEW_WINDOW|UCM_REUSE_WHEN_MAX"));//（指定包名，指定新建窗口）

        return linearLayout;
    }

    private TextView createMultiTextWithDefalutBrowser(){
        int padding = (int) ScreenUtils.dipToPixels(10);
        TextView textView = new TextView(mContext);
        textView.setPadding(padding,0,0,padding);

        String html1 = "<a href=\"" + url + "\"> " + url + " </a> （未指定包名，默认）";
        textView.setText(Html.fromHtml(html1));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return textView;
    }

    private TextView createMultiTextWithSpecifyBrowser(){
        int padding = (int) ScreenUtils.dipToPixels(10);
        TextView textView = new TextView(mContext);
        textView.setPadding(padding,0,0,padding);

        String html = "<a href=\"" + url + "\"> " + url + " </a> （指定包名，默认）";
        Spanned spaned = Html.fromHtml(html);
        URLSpan spans[] = spaned.getSpans(0, spaned.length(), URLSpan.class);
        if(spans != null){
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spaned);
            stringBuilder.clearSpans();

            for(int i=0;i<spans.length;i++){
                final URLSpan t = spans[i];
                MyURLSpan s = new MyURLSpan(t.getURL());//replace
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(t.getURL());
                        Context context = v.getContext();
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage(pkg);
                        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());

                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
                        }
                    }
                });
                stringBuilder.setSpan(s,spaned.getSpanStart(t),spaned.getSpanEnd(t),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(stringBuilder);
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return textView;
    }

    private TextView createMultiTextWithUCPolicy(final String policy){
        int padding = (int) ScreenUtils.dipToPixels(10);
        TextView textView = new TextView(mContext);
        textView.setPadding(padding,0,0,padding);

        String html2 = "<a href=\"" + url + "\"> " + url + " </a> （指定包名，" + policy + "）";
        Spanned spaned2 = Html.fromHtml(html2);
        URLSpan spans2[] = spaned2.getSpans(0, spaned2.length(), URLSpan.class);
        if(spans2 != null){
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spaned2);
            stringBuilder.clearSpans();

            for(int i=0;i<spans2.length;i++){
                final URLSpan t = spans2[i];
                MyURLSpan s = new MyURLSpan(t.getURL());//replace
                s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(t.getURL());
                        Context context = v.getContext();
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage(pkg);
                        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                        intent.putExtra("policy",policy);

                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
                        }
                    }
                });
                stringBuilder.setSpan(s,spaned2.getSpanStart(t),spaned2.getSpanEnd(t),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(stringBuilder);
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return textView;
    }

    @Override
    public void onClick(View v) {

    }

    private class MyURLSpan extends URLSpan{

        private View.OnClickListener mOnClickListener;

        public MyURLSpan(String url) {
            super(url);
        }

        public MyURLSpan(Parcel src) {
            super(src);
        }

        @Override
        public void onClick(View widget) {

            if(mOnClickListener != null){
                mOnClickListener.onClick(widget);
            }


        }

        private void setOnClickListener(View.OnClickListener l){
            mOnClickListener = l;
        }

    }

}
