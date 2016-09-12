package example.chedifier.chedifier.test;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import example.chedifier.chedifier.utils.FileUtils;
import example.chedifier.chedifier.utils.Md5Utils;
import example.chedifier.chedifier.utils.StringUtils;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : ExternalFileTest
 * <p/>
 * Creation    : 2016/6/18
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/6/18, chengqianxing, Create the file
 * ****************************************************************************
 */
public class ExternalFileTest {

    private static final String TAG = ExternalFileTest.class.getSimpleName();

    /**
     * 通道数据文件名
     */
    private static String CHANNEL_TABLE_NAME = ".B7CD4609DB526FBD";

    /**
     * 约定好的总目录
     */
    private static final String PROTOCOL_DIR = "/data/data/com.UCMobile/sdk_res/cms";

    private static final String CHAR_SET_NAME = "UTF-8";

    public static boolean writeExternalFile(Context ctx,String fileName,String content){
        if(StringUtils.isEmpty(fileName) || ctx == null){
            return false;
        }

        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            fos = ctx.openFileOutput(fileName,Context.MODE_WORLD_READABLE);
            pw = new PrintWriter(fos);
            pw.write(content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return false;
        } finally {
            if(pw != null){
                pw.close();
                pw = null;
            }

            if(fos != null){
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return true;
    }

    //for test
    public static void readTest(){
        Log.i(TAG, "readTest>>>");

        byte[] bytes = FileUtils.readBytes(PROTOCOL_DIR
                +File.separator+ CHANNEL_TABLE_NAME);

        if(bytes == null){
            Log.i(TAG,"bytes is empty.");
            return;
        }

        try {
            String table = new String(Base64.decode(bytes, Base64.NO_WRAP),CHAR_SET_NAME);
            Log.i(TAG,"table: " + table);
            JSONObject jo = new JSONObject(table);
            Iterator itr = jo.keys();
            while(itr.hasNext()){

                String id = itr.next().toString();
                String path = jo.optString(id);

                Log.i(TAG,"id: " + id + " path: " + path);

                try {
                    String[] content = FileUtils.readTextFile(path);
                    if(content != null){
                        StringBuilder sb = new StringBuilder();
                        for(int i=0;i<content.length;i++){
                            sb.append(content[i]);
                        }

                        Log.i(TAG,"data: " + sb.toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
