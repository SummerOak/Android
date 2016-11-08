/**
 *****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-7-2
 *
 * Description : TrafficData.java
 *
 * Creation    : 2012-11-13
 * Author      : raorq@ucweb.com
 * History     : Creation, 2012-11-13, raorq, Create the file
 *****************************************************************************
 */
package example.chedifier.chedifier.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import example.chedifier.base.utils.IOUtil;
import example.chedifier.base.utils.StringUtils;

/**
 * 
 *
 * @author raorq
 * @version 1.0
 * 修改记录 zhanglk 把原来 fileoperator相关的代码移到这里
 */

public final class FileUtils {
    public final static int TRAFFIC_FEED_RATE = 1024; // 流量单位进率
    public final static long PRICISION = 1; // 精度，因为只显示两位，精确到0.001即可。

    /**
     * write mode, only for RandomAccessFile
     */
    public static final byte WRITE_POS_CURRENT_POS = 0;
    public static final byte WRITE_POS_BEGIN = 1;
    public static final byte WRITE_POS_END = 2;
    public static final byte WRITE_POS_SPECIFIED = 3;

    private final static String TAG = "FileUtils";
    public static final String DEFAULT_NAME = "index.html";

    public final static String EXT_BAK = ".bak";

    /**
     * 计算文件夹大小
     * @param item
     * @param stack：递归传值栈，用于临时记录文件夹大小
     * @return
     */
    public static long caculateFileSize(File item, long stack) {
        if (item == null || !item.exists()) {
            return stack;
        }
        if (item.isDirectory()) {
            long temp = 0;
            File[] fileList = item.listFiles();
            if (null != fileList) {
                for (File subitem : fileList) {
                    temp += caculateFileSize(subitem, 0);
                }
            }
            return stack + temp;
        } else {
            return stack + item.length();
        }
    }

    /**
     * Reads a text file.
     *  
     * @param fileName the name of the text file
     * @return the lines of the text file
     * @throws FileNotFoundException when the file was not found
     * @throws IOException when file could not be read.
     */
    public static String[] readTextFile(String fileName) throws FileNotFoundException, IOException {
        return readTextFile(new File(fileName));
    }

    /**
     * Reads a text file.
     *  
     * @param file the text file
     * @return the lines of the text file
     * @throws FileNotFoundException when the file was not found
     * @throws IOException when file could not be read.
     */
    public static String[] readTextFile(File file) throws FileNotFoundException, IOException {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        in.close();
        return (String[]) lines.toArray(new String[lines.size()]);
    }

    /**
     * Reads a text file.
     *  
     * @param file the text file
     * @param encoding the encoding of the textfile
     * @return the lines of the text file
     * @throws FileNotFoundException when the file was not found
     * @throws IOException when file could not be read.
     */
    public static String[] readTextFile(File file, String encoding) throws FileNotFoundException, IOException {
        return readTextFile(new FileInputStream(file), encoding);
    }

    /**
     * Reads the text from the given input stream in the default encoding.
     * 
     * @param in the input stream
     * @return the text contained in the stream
     * @throws IOException when stream could not be read.
     */
    public static String[] readTextFile(InputStream in) throws IOException {
        return readTextFile(in, null);
    }

    /**
     * Reads the text from the given input stream in the default encoding.
     * 
     * @param in the input stream
     * @param encoding the encoding of the textfile
     * @return the text contained in the stream
     * @throws IOException when stream could not be read.
     */
    public static String[] readTextFile(InputStream in, String encoding) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader bufferedIn;
        if (encoding != null) {
            bufferedIn = new BufferedReader(new InputStreamReader(in, encoding));
        } else {
            bufferedIn = new BufferedReader(new InputStreamReader(in));
        }
        String line;
        while ((line = bufferedIn.readLine()) != null) {
            lines.add(line);
        }
        bufferedIn.close();
        in.close();
        return (String[]) lines.toArray(new String[lines.size()]);
    }

    /**
     * Writes (and creates) a text file.
     * 
     * @param file the file to which the text should be written
     * @param lines the text lines of the file in a collection with String-values
     * @throws IOException when there is an input/output error during the saving
     */
    public static void writeTextFile(File file, Collection<String> lines) throws IOException {
        writeTextFile(file, (String[]) lines.toArray(new String[lines.size()]), false);
    }

    /**
     * Writes (and creates) a text file.
     * @param file
     * @param lines
     * @param isAppend, use true to append lines to the end of the file, or false to overrwrite the file
     * @throws IOException
     */
    public static void writeTextFile(File file, Collection<String> lines, boolean isAppend) throws IOException {
        writeTextFile(file, (String[]) lines.toArray(new String[lines.size()]), isAppend);
    }

    /**
     * Writes (and creates) a text file.
     * 
     * @param file the file to which the text should be written
     * @param lines the text lines of the file
     * @throws IOException when there is an input/output error during the saving
     */
    public static void writeTextFile(File file, String[] lines, boolean isAppend) throws IOException {
        File parentDir = file.getParentFile();
        if ((parentDir != null) && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if(!file.exists()){
            file.createNewFile();
        }

        PrintWriter out = new PrintWriter(new FileWriter(file, isAppend));
        for (int i = 0; i < lines.length; i++) {
            out.println(lines[i]);
        }

        out.close();
    }

    /**
     * Copies the given files to the specified target directory.
     * 
     * @param files The files which should be copied, when an array element is null, it will be ignored.
     * @param targetDir The directory to which the given files should be copied to.
     * @throws FileNotFoundException when the source file was not found
     * @throws IOException when there is an error while copying the file.
     * @throws NullPointerException when files or targetDir is null.
     */
    public static void copy(File[] files, File targetDir) throws FileNotFoundException, IOException {
        copy(files, targetDir, false);
    }

    /**
     * Copies the given files to the specified target directory.
     * 
     * @param files The files which should be copied, when an array element is null, it will be ignored.
     * @param targetDir The directory to which the given files should be copied to.
     * @param overwrite true when existing target files should be overwritten even when they are newer
     * @throws FileNotFoundException when the source file was not found
     * @throws IOException when there is an error while copying the file.
     * @throws NullPointerException when files or targetDir is null.
     */
    public static void copy(File[] files, File targetDir, boolean overwrite) throws FileNotFoundException, IOException {
        String targetPath = targetDir.getAbsolutePath() + File.separatorChar;
        byte[] buffer = new byte[64 * 1024];
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file != null) {
                File targetFile = new File(targetPath + file.getName());
                if (!overwrite && targetFile.exists() && targetFile.lastModified() > file.lastModified()) {
                    continue;
                }
                copy(file, targetFile, buffer);
            }
        }
    }

    /**
     * Copies a file.
     * 
     * @param source The file which should be copied
     * @param target The file or directory to which the source-file should be copied to.
     * @throws FileNotFoundException when the source file was not found
     * @throws IOException when there is an error while copying the file.
     */
    public static void copy(File source, File target) throws FileNotFoundException, IOException {
        copy(source, target, new byte[64 * 1024]);
    }

    /**
     * Copies a file.
     * 
     * @param source The file which should be copied
     * @param target The file or directory to which the source-file should be copied to.
     * @param buffer A buffer used for the copying.
     * @throws FileNotFoundException when the source file was not found
     * @throws IOException when there is an error while copying the file.
     */
    private static void copy(File source, File target, byte[] buffer) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(source);
        // create parent directory of target-file if necessary:
        File parent = target.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (target.isDirectory()) {
            target = new File(target, source.getName());
        }
        OutputStream out = new FileOutputStream(target);
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            in.close();
            out.close();
        }
    }

    /**
     * Writes the properties which are defined in the given HashMap into a textfile.
     * The notation in the file will be [name]=[value]\n for each defined property.
     * 
     * @param file the file which should be created or overwritten
     * @param properties the properties which should be written. 
     * @throws IOException when there is an input/output error during the saving
     */
    public static void writePropertiesFile(File file, Map<String, String> properties) throws IOException {
        writePropertiesFile(file, '=', properties);
    }

    /**
     * Writes the properties which are defined in the given HashMap into a textfile.
     * The notation in the file will be [name]=[value]\n for each defined property.
     * 
     * @param file the file which should be created or overwritten
     * @param delimiter the character that separates a property-name from a property-value.
     * @param properties the properties which should be written. 
     * @throws IOException when there is an input/output error during the saving
     */
    public static void writePropertiesFile(File file, char delimiter, Map<String, String> properties)
            throws IOException {
        Object[] keys = properties.keySet().toArray();
        Arrays.sort(keys);
        String[] lines = new String[keys.length];
        for (int i = 0; i < lines.length; i++) {
            Object key = keys[i];
            Object value = properties.get(key);
            lines[i] = key.toString() + delimiter + value.toString();
        }
        writeTextFile(file, lines, false);
    }

    /**
     * Copies the contents of a directory to the specified target directory.
     * 
     * @param directory the directory containing files
     * @param targetDirName the directory to which the files should be copied to
     * @param update is true when files should be only copied when the source files
     *  are newer compared to the target files.
     * @throws IOException when a file could not be copied
     * @throws IllegalArgumentException when the directory is not a directory.
     */
    public static void copyDirectoryContents(File directory, String targetDirName, boolean update) throws IOException {
        if (targetDirName == null || targetDirName.length() == 0) {
            return;
        }
        copyDirectoryContents(directory, new File(targetDirName), update);
    }

    /**
     * Copies the contents of a directory to the specified target directory.
     * 
     * @param directory the directory containing files
     * @param targetDir the directory to which the files should be copied to
     * @param update set to true when files should be only copied when the source files
     *  are newer compared to the target files.
     * @throws IOException when a file could not be copied
     * @throws IllegalArgumentException when the directory is not a directory.
     */
    public static void copyDirectoryContents(File directory, File targetDir, boolean update) throws IOException {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Cannot copy contents of the file [" + directory.getAbsolutePath()
                    + "]: specify a directory instead.");
        }
        String[] fileNames = directory.list();
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            File file = new File(directory.getAbsolutePath(), fileName);
            if (file.isDirectory()) {
                copyDirectoryContents(file, targetDir.getAbsolutePath() + File.separatorChar + fileName, update);
            } else {
                File targetFile = new File(targetDir, fileName);
                if (update) {
                    // update only when the source file is newer:
                    if ((!targetFile.exists()) || (file.lastModified() > targetFile.lastModified())) {
                        copy(file, targetFile);
                    }
                } else {
                    // copy the file in all cases:
                    copy(file, targetFile);
                }
            }
        }
    }

    // private static String getPath( File file ) {
    // String path = file.getAbsolutePath();
    // int buildIndex = path.indexOf("build");
    // if (buildIndex != -1) {
    // path = path.substring( buildIndex );
    // }
    // return path;
    // }

    /**
     * Deletes a file or a directory.
     * 
     * @param file the file or directory which should be deleted.
     * @return true when the file could be deleted
     */
    public static boolean delete(File file) {
        if (file.isDirectory()) {
            String[] children = file.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = delete(new File(file, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        boolean suc = false;
        try {
            // The directory is now empty so delete it
            suc = file.delete();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return suc;
    }

    /**
     * Deletes a file or a directory.
     *
     * @param file the file or directory which should be deleted.
     * @return true when the file could be deleted
     */
    public static boolean delete(File file, FilenameFilter filenameFilter) {
        if (file.isDirectory()) {
            String[] children = file.list(filenameFilter);
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = delete(new File(file, children[i]), filenameFilter);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        boolean suc = true;
        try {
            String[] children = file.list();
            if (children == null || children.length == 0) {
                // The directory is now empty so delete it
                suc = file.delete();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return suc;
    }

    /**
     * Writes the given textlines into the specified file.
     * 
     * @param file the file to which the text should be written
     * @param lines the text lines of the file
     * @param encoding the encoding, e.g. "UTF8", null when the default encoding should be used
     * @throws IOException when there is an input/output error during the saving
     */
    public static void writeTextFile(File file, String[] lines, String encoding) throws IOException {

        File parentDir = file.getParentFile();
        if ((parentDir != null) && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        PrintWriter out;
        if (encoding != null) {
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
        } else {
            out = new PrintWriter(new FileWriter(file));
        }

        for (int i = 0; i < lines.length; i++) {
            out.println(lines[i]);
        }
        out.close();
    }

    /**
     * List all files in the given directory satisfy the filter.
     * 
     * @param dir
     *          Directory to list.
     * @param filter
     *          A file filter.
     * @param recursive
     *          If true, all the sub directories will be listed too.
     * @return
     *          Array of files in the given directory and satisfy the filter.
     */
    public static List<File> listFiles(File dir, FileFilter filter, boolean recursive) {
        List<File> result = new ArrayList<File>();

        if (!dir.exists() || dir.isFile()) {
            return result;
        }

        File[] fArray = dir.listFiles(filter);

        if (fArray == null) {
            return result;
        }

        List<File> fList = Arrays.asList(fArray);

        if (!recursive) {
            return fList;
        }

        LinkedList<File> linkedList = new LinkedList<File>(fList);

        while (!linkedList.isEmpty()) {
            File f = linkedList.removeFirst();
            result.add(f);

            if (f.isDirectory()) {
                File[] array = f.listFiles(filter);

                if (array == null) {
                    continue;
                }

                for (int i = 0; i < array.length; i++) {
                    linkedList.addLast(array[i]);
                }
            }
        }

        return result;
    }

    /**
     * Extracts the bytes from a file.
     * @param file the file from which the bytes should be extracted from
     * @return a byte arry corresponding to the file. Is never null.
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            byte[] buffer = new byte[4096];
            inputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream();
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            byte[] byteArray = null;
            byteArray = outputStream.toByteArray();
            return byteArray;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static String genBackupFilePath(String filePath) {
        return filePath + EXT_BAK;
    }

    public static boolean writeBytes(String filePath, String fileName, byte[] data) {
        if (data == null) {
            return false;
        }
        return writeBytes(filePath, fileName, data, 0, data.length);
    }

    /**
     * 请尽量将这个函数放入ThreadManager的后台线程执行，防止引入严重卡顿
     * @param filePath
     * @param fileName
     * @param headData
     * @param bodyData
     * @param bodyOffset
     * @param bodyLen
     * @param forceFlush 请慎重使用这个参数，如果设置为true可能导致严重卡顿，甚至ANR，如果不是极为重要的数据，请设置为false。
     * @return
     */
    public static boolean writeBytes(String filePath, String fileName, byte[] headData, byte[] bodyData,
            int bodyOffset, int bodyLen, boolean forceFlush) throws FileNotFoundException, IOException {
        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(fileName) || bodyData == null) {
            return false;
        }

        String tempFileName = System.currentTimeMillis() + fileName;

        File tempFile = createNewFile(filePath + tempFileName);

        boolean result = writeBytesBase(tempFile, headData, bodyData, bodyOffset, bodyLen, forceFlush);
        if (!result) {
            return false;
        }

        String srcPath = filePath + fileName;
        if (!rename(tempFile, srcPath)) {
            // rename srcPath到bakPath后再 delete bakPath，替代直接 delete srcPath
            String bakPath = genBackupFilePath(srcPath);
            delete(bakPath);
            rename(new File(srcPath), bakPath);

            result = rename(tempFile, srcPath);
            if (!result) {
                return false;
            }

            delete(bakPath);
        }

        return true;
    }

    /**
     * 请尽量将这个函数放入ThreadManager的后台线程执行，防止引入严重卡顿
     * @param raf, RandomAccessFile object
     * @param mode
     * @param specifiedPos, the position start to write
     * @param data, byte[] type
     * @return
     */
    public static boolean writeBytes(RandomAccessFile raf, byte mode, int specifiedPos, byte[] data) {
        if (null == raf || data == null || data.length == 0) {
            return false;
        }

        try {
            switch (mode) {
            case WRITE_POS_CURRENT_POS:
                break;
            case WRITE_POS_BEGIN:
                raf.seek(0);
                break;
            case WRITE_POS_END: {
                long len = raf.length();
                raf.seek(len);
                break;
            }
            case WRITE_POS_SPECIFIED: {
                raf.seek(specifiedPos);
                break;
            }
            default:
                break;
            }

            raf.write(data);
        } catch (Throwable e) {
            return false;
        }

        return true;
    }

    public static boolean writeBytes(String path, byte mode, int specifiedPos, byte[] data) {
        RandomAccessFile raf = openFile(path, false);
        if (null == raf) {
            return false;
        }

        boolean ret = writeBytes(raf, mode, specifiedPos, data);
        IOUtil.safeClose(raf);

        return ret;
    }

    public static boolean writeBytes(String filePath, String fileName, byte[] data, int offset, int len) {
        try {
            return writeBytes(filePath, fileName, null, data, offset, len, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 请尽量将这个函数放入ThreadManager的后台线程执行，防止引入严重卡顿
     * @param headData
     * @param bodyData
     * @param bodyOffset
     * @param bodyLen
     * @param forceFlush 请慎重使用这个参数，如果设置为true可能导致严重卡顿，甚至ANR，如果不是极为重要的数据，请设置为false。
     * @return
     */
    public static boolean writeBytesBase(File file, byte[] headData, byte[] bodyData, int bodyOffset, int bodyLen,
            boolean forceFlush) throws FileNotFoundException, IOException {
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(file);
            if (headData != null) {
                fileOutput.write(headData);
            }
            fileOutput.write(bodyData, bodyOffset, bodyLen);
            fileOutput.flush();
            if (forceFlush) {
                FileDescriptor fd = fileOutput.getFD();
                if (fd != null) {
                    fd.sync(); // 立刻刷新，保证文件可以正常写入;
                }
            }
            return true;
        } finally {
            IOUtil.safeClose(fileOutput);
        }
    }
    
    /**
     * 请尽量将这个函数放入ThreadManager的后台线程执行，防止引入严重卡顿
     * @param headData
     * @param bodyData
     * @param bodyOffset
     * @param bodyLen
     * @param forceFlush 请慎重使用这个参数，如果设置为true可能导致严重卡顿，甚至ANR，如果不是极为重要的数据，请设置为false。
     * @return
     */
    public static boolean writeBytes(File file, byte[] headData, byte[] bodyData, int bodyOffset, int bodyLen, boolean forceFlush){
        try {
            return writeBytesBase(file, headData, bodyData, bodyOffset, bodyLen, forceFlush);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeBytes(File file, byte[] data, int offset, int len) {
        return writeBytes(file, null, data, offset, len, false);
    }

    public static File createNewFile(String path) {
        return createNewFile(path, false);
    }

    public static RandomAccessFile openFile(String path, boolean append) {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(path, "rw");
            if (append) {
                long len = file.length();
                if (len > 0) {
                    file.seek(len);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return file;
    }

    public static boolean rename(File file, String newName) {
        return file.renameTo(new File(newName));
    }

    public static boolean delete(String path) {
        return delete(new File(path));
    }

    /**
     * 
     * @param path
     *            ：文件路径
     * @param append
     *            ：若存在是否插入原文件
     * @return
     */
    public static File createNewFile(String path, boolean append) {
        File newFile = new File(path);
        if (!append) {
            if (newFile.exists()) {
                newFile.delete();
            }
        }
        if (!newFile.exists()) {
            try {
                File parent = newFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                newFile.createNewFile();
            } catch (Exception e) {
                // #if (debug == true)
                e.printStackTrace();
                // #endif
            }
        }
        return newFile;
    }

    public static byte[] readBytes(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        return readBytes(new File(filePath));
    }

    public static byte[] readBytes(File file) {
        FileInputStream fileInput = null;
        try {
            if (file.exists()) {
                fileInput = new FileInputStream(file);
                return IOUtil.readFullBytes(fileInput);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.safeClose(fileInput);
            fileInput = null;
        }
        return null;
    }


}
