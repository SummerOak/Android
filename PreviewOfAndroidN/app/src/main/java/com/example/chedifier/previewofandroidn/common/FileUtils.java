package com.example.chedifier.previewofandroidn.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.example.chedifier.previewofandroidn.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/3/16.
 */
public class FileUtils {

    public static final String FILE_AUTHORITY = "com.example.chedifier.previewofandroidn.fileprovider";


    public static final String saveDrawableToFile(Context ctx,int resId,String fileName,boolean bReplace){
        Bitmap bm = BitmapFactory.decodeResource( ctx.getResources(), resId);

        String dir = ctx.getFilesDir().toString() + "/images";
        File file = new File(dir, fileName);

        if(file.getParentFile() != null && !file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        if(!bReplace && file.exists()){
            return dir + "/" + fileName;
        }

        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {

        }

        return dir + "/" + fileName;
    }

    public static void saveDrawable2Internal(final Context ctx){
        new Thread(new Runnable() {
            @Override
            public void run() {

                FileUtils.saveDrawableToFile(ctx,R.drawable.image1,"image1.png",false);
                FileUtils.saveDrawableToFile(ctx,R.drawable.image2,"image2.png",false);
                FileUtils.saveDrawableToFile(ctx,R.drawable.image3,"image3.png",false);
                FileUtils.saveDrawableToFile(ctx,R.drawable.image4,"image4.png",false);
                FileUtils.saveDrawableToFile(ctx,R.drawable.image5,"image5.png",false);
                FileUtils.saveDrawableToFile(ctx,R.drawable.image6,"image6.png",false);

            }
        }).start();

    }


    public static List<Uri> getImagesFromFileProvider(Context ctx){

        List<Uri> uris = new ArrayList<>();
        File dir = new File(ctx.getFilesDir()+ "/images");
        if(dir.exists() && dir.isDirectory()){

            File[] files = dir.listFiles();
            if(files != null){
                for(int i=0;i<files.length;i++){
                    if(files[i].isFile()){
                        uris.add(FileProvider.getUriForFile(ctx,FILE_AUTHORITY,files[i]));
                    }
                }
            }

        }

        return uris;
    }

    public static boolean createFile(String path){
        if(path == null || path.equals("")){
            return false;
        }

        File f = new File(path);
        if(!f.exists()){
            try {
                f.getParentFile().mkdirs();
                f.getParentFile().mkdir();
                f.getParentFile().mkdirs();
                f.getParentFile().mkdir();
                return f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public static boolean deleteFile(String path){
        if(path == null || path.equals("")){
            return false;
        }

        File f = new File(path);
        if(f.exists()){
            f.delete();
        }

        return false;
    }

}
