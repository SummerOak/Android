package example.chedifier.talkbackdemo.talkback;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;

import example.chedifier.talkbackdemo.base.Color;
import example.chedifier.talkbackdemo.customview.VirtualView;

/**
 * Created by chedifier on 2016/11/30.
 */
public class SimpleTextView extends VirtualView implements View.OnClickListener,View.OnLongClickListener{

    private String mText;

    public SimpleTextView(Context context, int w, int h, String text) {
        super(context, w, h, Color.DARK_BLUE.value);

        setText(text);

        setOnClickListener(this);
        setOnLongClickListener(this);
    }

    public void setText(String text){
        mText = text;

        setContentDescription(mText);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(mText != null && !mText.equals("")){
            Paint paint = new Paint();
            int textSize = Math.min(getWidth()/3,getHeight()/3);
            paint.setTextSize(textSize);
            paint.setColor(Color.DEFAULT.value);
            canvas.drawText(mText,(getWidth()-textSize)/2,getHeight()/2,paint);
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mContext,mText==null?"":mText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(mContext,"long pressed " + mText==null?"":mText, Toast.LENGTH_SHORT).show();
        return true;
    }
}
