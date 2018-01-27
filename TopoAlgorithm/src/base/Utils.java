package base;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Utils {

	
	public static String getMD5(String originString){
		String result = "";
        if (originString != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte bytes[] = md.digest(originString.getBytes());
                for (int i = 0; i < bytes.length; i++) {
                    String str = Integer.toHexString(bytes[i] & 0xFF);
                    if (str.length() == 1) {
                        str += "F";
                    }
                    result += str;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
        return result.toUpperCase();
	}
	
	
	@SuppressWarnings("rawtypes")
	public static boolean listEmpty(List list){
		return list == null || list.isEmpty();
	}
	
	public static boolean stringEmpty(String val){
		return val == null || val.equals("");
	}
	
}
