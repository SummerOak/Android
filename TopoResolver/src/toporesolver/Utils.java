package toporesolver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class Utils {

    @SuppressWarnings("rawtypes")
    public static boolean listEmpty(List list){
        return list == null || list.isEmpty();
    }

    public static boolean stringEmpty(String val){
        return val == null || val.equals("");
    }

    public static String sha1(byte[] convertme) {
        long t = System.currentTimeMillis();
        String sha1 = null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            sha1 = byteArrayToHexString(md.digest(convertme));
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

//        Log.i("ccc","sha1 cost: " + (System.currentTimeMillis() - t));
        return sha1;
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString(( b[i] & 0xff ) + 0x100, 16).substring(1);
        }
        return result;
    }

}
