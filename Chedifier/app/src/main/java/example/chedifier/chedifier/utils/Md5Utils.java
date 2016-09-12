package example.chedifier.chedifier.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5Utils {
    public static String byteToHexString(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        int i = 0;
        while (i < bArr.length) {
            if ((bArr[i] & 255) < 16) {
                stringBuffer.append("0");
            }
            stringBuffer.append(Long.toString((long) (bArr[i] & 255), 16));
            i++;
        }
        return stringBuffer.toString();
    }

    public static String getMD5(File file) {
        FileInputStream fileInputStream = null;
        String str = null;
        MessageDigest messageDigest = null;
        if (file != null) {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read != -1) {
                        messageDigest.update(bArr, 0, read);
                    } else {
                        str = byteToHexString(messageDigest.digest());
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e2) {
                            }
                        }
                        return str;
                    }
                }
            } catch (FileNotFoundException e3) {
            	IOUtil.safeClose(fileInputStream);
                fileInputStream = null;
            } catch (IOException e4) {
            	IOUtil.safeClose(fileInputStream);
                fileInputStream = null;
            } catch (Throwable th) {
            	IOUtil.safeClose(fileInputStream);
                fileInputStream = null;
            }
        }
        
        return str;
    }

    public static String getMD5(String str) {
        if (str == null) {
            return null;
        }
        try {
            byte[] bytes = str.getBytes();
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bytes);
            return byteToHexString(instance.digest());
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] getMD5(InputStream inputStream) {
        byte[] bArr = null;
        if (inputStream != null) {
            MessageDigest instance;
            try {
                instance = MessageDigest.getInstance("MD5");
                if (instance != null) {
                    byte[] bArr2 = new byte[8192];
                    while (true) {
                        int read = inputStream.read(bArr2);
                        if (read != -1) {
                            instance.update(bArr2, 0, read);
                        } else {
                            bArr = instance.digest();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return bArr;
    }

    public static byte[] getMD5(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr);
            return instance.digest();
        } catch (Exception e) {
            return null;
        }
    }
}