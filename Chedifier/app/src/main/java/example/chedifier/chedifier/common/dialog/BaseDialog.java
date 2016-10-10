package example.chedifier.chedifier.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;

import example.chedifier.chedifier.common.BackgroundTaskMgr;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : BaseDialog
 * <p/>
 * Creation    : 2016/9/13
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/13, chengqianxing, Create the file
 * ****************************************************************************
 */
public class BaseDialog extends Dialog {

    LinearLayout mCurrentRow;
    LinearLayout mRoot;

    // row相关的margin值
    public LinearLayout.LayoutParams mRowParams = null;
    public int DEFAULT_ROW_MARGIN_LEFT;
    public int DEFAULT_ROW_MARGIN_RIGHT;
    public int DEFAULT_ROW_MARGIN_TOP = 0;
    public int DEFAULT_ROW_MARGIN_BOTTOM = 0;

    Context mContext;

    public BaseDialog(Context context) {
        super(context);

        mContext = context;

        mRowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRowParams.setMargins(DEFAULT_ROW_MARGIN_LEFT, DEFAULT_ROW_MARGIN_TOP,
                DEFAULT_ROW_MARGIN_RIGHT, DEFAULT_ROW_MARGIN_BOTTOM);

        mRoot = new LinearLayout(context);
        mRoot.setPadding(50, 50, 50, 50);
        mRoot.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // set background darker when dialog show
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        this.setContentView(mRoot, params);

    }

    public BaseDialog addNewRow(){
        mCurrentRow = new LinearLayout(mContext);
        mCurrentRow.setOrientation(LinearLayout.VERTICAL);
        mRoot.addView(mCurrentRow, mRowParams);
        return this;
    }

    public BaseDialog addEditText(CharSequence txt,int id){
        final EditText e = createEditText(id);
        e.setText(txt);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 100);
        params.weight = 1;
        params.setMargins(0, 0, 0, 0);
        mCurrentRow.addView(e, params);

        return this;
    }

    public EditText createEditText(int id) {
        EditText e = new EditText(mContext);
        e.setId(id);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            try {
//                Field mEditor = getField_(EditText.class,"mEditor",true);
//                if (mEditor != null) {
//                    Object o = mEditor.get(e);
//                    if (o != null) {
//                        Field popu = getField_(o.getClass(),"mErrorPopup",true);
//                        if (popu != null) {
//                            popu.setAccessible(true);
//                            PopupWindow popupWindow = (PopupWindow) popu.get(o);
//                            popupWindow.getContentView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
//                        }
//                    }
//                }
//            }catch(NoSuchFieldException e1){
//                e1.printStackTrace();
//            }catch(IllegalAccessException e1){
//                e1.printStackTrace();
//            }catch(Exception e1){
//                e1.printStackTrace();
//            }
//        }

        return e;
    }

    public static Field getField_(Class<?> targetClass, String fieldName, boolean resolveParent) throws IllegalAccessException, IllegalArgumentException, NoSuchFieldException {
        NoSuchFieldException noSuchFieldExceptionOccor = null;
        Field rsField = null;

        Field e;
        try {
            e = targetClass.getDeclaredField(fieldName);
            rsField = e;
            if(!resolveParent) {
                e.setAccessible(true);
                return e;
            }
        } catch (NoSuchFieldException var7) {
            noSuchFieldExceptionOccor = var7;
        }

        if(noSuchFieldExceptionOccor != null) {
            if(!resolveParent) {
                throw noSuchFieldExceptionOccor;
            }

            while(true) {
                targetClass = targetClass.getSuperclass();
                if(targetClass == null) {
                    break;
                }

                try {
                    e = targetClass.getDeclaredField(fieldName);
                    e.setAccessible(true);
                    return e;
                } catch (NoSuchFieldException var8) {
                    if(targetClass.getSuperclass() == null) {
                        throw var8;
                    }
                }
            }
        }

        return rsField;
    }

    public BaseDialog addButton(CharSequence text,int id){
        Button b = new Button(mContext);
        b.setId(id);
        b.setText(text);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                100);
        params.weight = 2;
        params.setMargins(50, 50, 50, 50);
        mCurrentRow.setGravity(Gravity.CENTER);
        mCurrentRow.addView(b, params);
        return this;
    }

}
