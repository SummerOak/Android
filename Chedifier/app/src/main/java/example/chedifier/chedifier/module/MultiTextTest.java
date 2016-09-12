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


    public MultiTextTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        int padding = (int) ScreenUtils.dipToPixels(10);

        TextView textView = new TextView(mContext);
        textView.setPadding(padding,0,0,padding);
        linearLayout.addView(textView);

        String html = "<a href=\"http://www.zhihu.com\"> http://www.zhihu.com </a> （指定包名）";
        Spanned spaned = Html.fromHtml(html);
        URLSpan spans[] = spaned.getSpans(0, spaned.length(), URLSpan.class);
        if(spans != null){
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spaned);
            stringBuilder.clearSpans();

            for(int i=0;i<spans.length;i++){
                URLSpan t = spans[i];
                URLSpan s = new MyURLSpan(t.getURL());//replace
                stringBuilder.setSpan(s,spaned.getSpanStart(t),spaned.getSpanEnd(t),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(stringBuilder);
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());

        textView = new TextView(mContext);
        textView.setPadding(padding,padding,0,0);
        linearLayout.addView(textView);

        String html1 = "<a href=\"http://www.zhihu.com\"> http://www.zhihu.com </a> （未指定包名）";
        textView.setText(Html.fromHtml(html1));
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return linearLayout;
    }

    @Override
    public void onClick(View v) {

    }

    private class MyURLSpan extends URLSpan{

        public MyURLSpan(String url) {
            super(url);
        }

        public MyURLSpan(Parcel src) {
            super(src);
        }

        @Override
        public void onClick(View widget) {
            Uri uri = Uri.parse(getURL());
            Context context = widget.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.android.browser");
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Log.w("URLSpan", "Actvity was not found for intent, " + intent.toString());
            }
        }

    }

}
