/**
 *****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-7-2
 *
 * Description : IOUtil.java
 *
 * Creation    : 2012-11-10
 * Author      : raorq@ucweb.com
 * History     : Creation, 2012-11-10, raorq, Create the file
 *****************************************************************************
 */
package example.chedifier.base.utils;

import android.database.Cursor;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * <b>IOUtil简介:</b>
 * <p>维护IO操作相关的接口</p>
 *
 * <b>功能描述:</b>
 * <p>
 *  <ol>
 *   <li>从一个字节数组的2个字节中中读取一个short整型</li>
 *   <li>把一个short整型数据写入一个字节数组的连续2个字节</li>
 *   <li>从一个字节数组的2个字节中中读取一个int整型</li>
 *   <li>把一个int整型数据写入一个字节数组的连续4个字节</i>
 *   <li>从一个字节数组的连续8个字节中读出一个long整型值</li>
 *   <li>把一个long整型数据写入一个字节数组的连续8个字节</li>
 *   <li>从数据输入流中读取utf字符串对应的字节数组</li>
 *   <li>把utf字符串对应的字节数组写入到数据输出流</li>
 *   <li>从输入流中读取部份数据(字节数组)</li>
 *   <li>从char数组读int数据</li>
 *   <li>将int数据写入char数组</li>
 *   <li>指字符串的utf字节系列写到字节数组中</li>
 *   <li>关闭输入流</li>
 *   <li>关闭输出流</li>
 *  </ol>
 * </p>
 *
 * <b>修改历史</b>
 * <p>
 *  <ol>
 *   <li>创建（Added by luogw on 2011-11-22）</li>
 *  </ol> 
 * </p>
 * @author luogw
 * @version 1.0
 */
public final class IOUtil {
    
    //The default buffer size to use in get byte[] from input stream
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    
    /**
     * 
     * <p>从一个字节数组的2个字节中中读取一个short整型</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aSrcArray 源字节数组
     * @param aOffset 读取的偏移值
     * @return 读取的值
     */
    public static int readShort(byte[] aSrcArray, int aOffset) {
        return (aSrcArray[aOffset] & 0xff) << 8 | aSrcArray[aOffset + 1] & 0xff;
    }

    /**
     * 
     * <p>把一个short整型数据写入一个字节数组的连续2个字节</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aDestArray 目标数组
     * @param aOffset 写入偏移值
     * @param aValue 写入的short整型值
     */
    public static void writeShort(byte[] aDestArray, int aOffset, short aValue) {
        aDestArray[aOffset] = (byte)((aValue >> 8) & 0xFF);
        aDestArray[aOffset+1] = (byte)((aValue >> 0) & 0xFF);
    }
    /**
     * 
     * <p>从一个字节数组的2个字节中中读取一个int整型</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aSrcArray 源字节数组
     * @param aOffset 读取的偏移值
     * @return 读取的值
     */
    public static int readInt(byte[] aSrcArray, int aOffset) {
        return (int) (((aSrcArray[aOffset] & 0xff) << 24) + ((aSrcArray[aOffset + 1] & 0xff) << 16) + ((aSrcArray[aOffset + 2] & 0xff) << 8) + ((aSrcArray[aOffset + 3] & 0xff) << 0));
    }
    
    /**
     * 
     * <p>把一个int整型数据写入一个字节数组的连续4个字节</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aDestArray 目标数组
     * @param aOffset 写入偏移值
     * @param aValue 写入的int整型值
     */
    public static void writeInt(byte[] aDestArray, int aOffset, int aValue){
        aDestArray[aOffset] = (byte)((aValue >> 24)&0xFF);
        aDestArray[aOffset+1] = (byte)((aValue >> 16)&0xFF);
        aDestArray[aOffset+2] = (byte)((aValue >> 8)&0xFF);
        aDestArray[aOffset+3] = (byte)((aValue >> 0)&0xFF);
    }
    
    /**
     * 
     * <p>从一个字节数组的连续8个字节中读出一个long整型值</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aSrcArray 源字节数组
     * @param aOffset 读取的偏移值
     * @return 读取到的值
     */
    public static long readLong(byte[] aSrcArray, int aOffset) {
        return (((long)(aSrcArray[aOffset  ] & 255) << 56) +
                ((long)(aSrcArray[aOffset+1] & 255) << 48) +
                ((long)(aSrcArray[aOffset+2] & 255) << 40) +
                ((long)(aSrcArray[aOffset+3] & 255) << 32) +
                ((long)(aSrcArray[aOffset+4] & 255) << 24) +
                ((long)(aSrcArray[aOffset+5] & 255) << 16) +
                ((long)(aSrcArray[aOffset+6] & 255) <<  8) +
                ((long)(aSrcArray[aOffset+7] & 255) <<  0));
    }
    
    /**
     * 
     * <p>把一个long整型数据写入一个字节数组的连续8个字节</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-23）</li>
     * </ol>
     * @param aDestArray
     * @param aOffset
     * @param aValue
     */
    public static void writeLong(byte[] aDestArray, int aOffset, long aValue) {
        aDestArray[aOffset+0] = (byte)(aValue >>> 56);
        aDestArray[aOffset+1] = (byte)(aValue >>> 48);
        aDestArray[aOffset+2] = (byte)(aValue >>> 40);
        aDestArray[aOffset+3] = (byte)(aValue >>> 32);
        aDestArray[aOffset+4] = (byte)(aValue >>> 24);
        aDestArray[aOffset+5] = (byte)(aValue >>> 16);
        aDestArray[aOffset+6] = (byte)(aValue >>>  8);
        aDestArray[aOffset+7] = (byte)(aValue >>>  0);
    }
    
    /**
     * 
     * <p>从数据输入流中读取utf字符串对应的字节数组</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aInput 输入流
     * @return
     * @throws IOException
     */
    public static byte[] readUTFBytes(DataInput aInput) throws IOException {
        int sUtfLength = aInput.readUnsignedShort();
        byte sByteArray[] = new byte[sUtfLength];
        aInput.readFully(sByteArray, 0, sUtfLength);
        return sByteArray;
    }
    
    /**
     * 
     * <p>把utf字符串对应的字节数组写入到数据输出流</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-23）</li>
     * </ol>
     * @param aOutput 输出流
     * @param aSrcArray 源字节数组
     * @throws Exception
     */
    public static void writeUTFBytes(DataOutputStream aOutput, byte[] aSrcArray) throws Exception {
        if (aSrcArray != null) {
            aOutput.writeShort(aSrcArray.length);
            aOutput.write(aSrcArray, 0, aSrcArray.length);
        } else
            aOutput.writeShort(0);
    }
    
    /**
     * 
     * <p>从输入流中读取部份数据(字节数组)</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aReadLength 读取的长度
     * @param aBufferLength 读数据时的缓冲长度
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream aInput, int aReadLength, int aBufferLength) throws IOException {
        if (aInput == null || aReadLength <= 0)
            return null;

        byte[] sData = new byte[aReadLength];

        // mod by raorq 2010-4-19 : 不能灵活控制缓冲区长度
        // aBufferLength = Math.max(aBufferLength,2048);
        if (aBufferLength <= 0)
            aBufferLength = 2048;
        // mod by raorq 2010-4-19 end

        int sLength = 0;
        for (int i = 0; i < aReadLength;) {
            if (aReadLength - i < aBufferLength)
                sLength = aInput.read(sData, i, aReadLength - i);
            else
                sLength = aInput.read(sData, i, aBufferLength);

            if (sLength == -1)
                break;
            i += sLength;
        }

        return sData;
    }
    
    /**
     * 
     * <p>从输入流中读取部份数据(字节数组)</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-22）</li>
     * </ol>
     * @param aInput
     * @param aReadLength
     * @param aBufferLength
     * @return
     * @throws IOException
     * @throws OutOfMemoryError
     */
    public static byte[] readBytesEx(InputStream aInput, int aReadLength, int aBufferLength) throws IOException, OutOfMemoryError {
        if (aInput == null)
            return null;

        // mod by raorq 2010-4-19 : 不能灵活控制缓冲区长度，如果小于0则默认使用2k的缓冲区。
        // aBufferLength = Math.max(aBufferLength,2048);
        if (aBufferLength <= 0)
            aBufferLength = 2048;
        // mod by raorq 2010-4-19 end

        if (aReadLength <= 0) {
            // mod by raorq 2010-4-19: 如果已知道固定长度，则使用固定长度作为缓冲区，如果不知道则默认是使用2k的缓冲
            // sReadLength = aIs.available() + 2048;
            aReadLength = 1024; // sReadLength = Math.max(aIs.available(),
                                // 2048); // 直接修改1024，因为三星E2652W会有兼容性问题。
            // mod by raorq 2010-4-19 end

            ByteArrayOutputStream sBos = new ByteArrayOutputStream(aReadLength);

            byte[] sBuffer = new byte[aBufferLength];
            int sPos = 0;
            // int sHasRead = 0; //del by raorq 2010-4-19 : 无用的代码
            while ((sPos = aInput.read(sBuffer, 0, aBufferLength)) != -1) {
                // sHasRead += sPos; //del by raorq 2010-4-19 : 无用的代码
                sBos.write(sBuffer, 0, sPos);
            }

            byte[] sBytesArray = sBos.toByteArray();
            sBos.close();
            sBos = null;
            return sBytesArray;

        } else {
            return IOUtil.readBytes(aInput, aReadLength, aBufferLength);
        }
    }
    
    /**
     * <p>从char数组读int数据</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by cairq on 2010-12-15）</li>
     * </ol>
     * @param aSrcArray
     * @param aOffset
     * @return
     */
    public static int readIntFormCharArray(char[] aSrcArray, int aOffset){
        return ((aSrcArray[aOffset] & 0xFFFF) << 16) + (aSrcArray[aOffset + 1] & 0xFFFF);
    }
    
    /**
     * <p>将int数据写入char数组</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by cairq on 2010-12-15）</li>
     * </ol>
     * @param aSrcArray
     * @param aOffset
     * @param aValue
     */
    public static void writeInt2CharArray(char[] aSrcArray, int aOffset, int aValue){
        aSrcArray[aOffset] = (char)(aValue >> 16 & 0xFFFF);
        aSrcArray[aOffset + 1] = (char)(aValue & 0xFFFF);
    }
    
    /**
     * 
     * <p>指字符串的utf字节系列写到字节数组中</p>
     *
     * <b>修改历史</b>
     * <ol>
     * <li>创建（Added by luogw on 2011-11-23）</li>
     * </ol>
     * @param aDestArray 目标数组
     * @param aOffset 写入的偏移值
     * @param aSrcUTFBytes 源数组
     */
    public static void writeUTFBytes(byte[] aDestArray,int aOffset , byte[] aSrcUTFBytes){
        if (aSrcUTFBytes == null) {
            IOUtil.writeShort(aDestArray, aOffset, (short) 0);
        } else {
            IOUtil.writeShort(aDestArray, aOffset, (short) aSrcUTFBytes.length);
            System.arraycopy(aSrcUTFBytes, 0, aDestArray, aOffset + 2, aSrcUTFBytes.length);
        }
    }
    

 
    public static byte[] readByteArrayFromInputStream(InputStream aInputStream){
	    byte[] res = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
		    int len = 0;
		    int size = 1024 * 4;
		    byte[] buf = new byte[size];
			while ((len = aInputStream.read(buf, 0, size)) != -1)
				bos.write(buf, 0, len);
			res = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		    safeClose(bos);
		}

		return res;
    }
    
    public static byte[] readFullBytes(InputStream input) {
        if (input == null) {
            return null;
        }
        
        byte[] inputStreamBuffer = new byte[32 * 1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        try {
            int offset = 0;
            while ((offset = input.read(inputStreamBuffer, 0, inputStreamBuffer.length)) > 0) {
                baos.write(inputStreamBuffer, 0, offset);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            safeClose(baos);
            baos = null;
        }

        return null;
    }
    
    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void safeClose(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 从input stream获取byte[]
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        if (input != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            if (-1 != copy(input, output)) {
                return output.toByteArray();
            }
        }
        return null;
    }
    
    /**
     * stream copy
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        if (input == null || output == null) {
            return -1;
        }
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }
    
    /**
     * 
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    private static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        if (input == null || output == null) {
            return -1;
        }
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

}
