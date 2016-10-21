package example.chedifier.hook.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by chedifier on 2016/10/20.
 */
public class Utils {

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static File createFile(String path){
        if(path == null || path.equals("")){
            return null;
        }

        File f = new File(path);
        if(f.exists()){
            return f;
        }

        try {
            if(f.createNewFile()){
                return f;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
