package com.chedifier.baselibrary.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean copyFileToDir(String sourceFilePath, String destDirPath) {
    	if(StringUtils.isEmpty(sourceFilePath) || StringUtils.isEmpty(destDirPath)){
    		return false;
    	}
    	
    	File sourceFile = new File(sourceFilePath);
    	File destDir = new File(destDirPath);
    	
    	if(!destDir.exists()){
    		destDir.mkdirs();
    	}
    	
    	if(destDir.isFile()){
    		return false;
    	}
    	
    	if(!destDir.exists()){
    		return false;
    	}
    	
    	if(sourceFile.isFile()){
			String fileName = sourceFile.getName();
			String newFilePath = destDirPath + (destDirPath.endsWith("\\")?"":"\\") + fileName;
			
			File newFile = new File(newFilePath);
			copyToFile(sourceFile, newFile);
		}else if(sourceFile.isDirectory()){
			File[] childFile = sourceFile.listFiles();
			if(childFile !=null){
				for(int i=0;i<childFile.length;i++){
					File cf = childFile[i];
					
					String newSourcePath = cf.getAbsolutePath();
					String newDestPath = destDirPath + cf.getName();
					copyFileToDir(newSourcePath, newDestPath);
				}
			}
		}
    	
    	return false;
    }
    
    public static String getParentPath(String path){
    	if(StringUtils.isEmpty(path)){
    		return "";
    	}
    	
    	int idx = path.lastIndexOf(File.separator);
    	if(idx > 0){
    		return path.substring(0, idx);
    	}
    	
    	return "";
    }
    
    /**
     * Copy data from a source to destFile. Return true if succeed, return false if failed.
     * 
     * @param sourceFile source file
     * @param destFile destFile
     * 
     * @return success return true
     */
    public static boolean copyToFile(File sourceFile, File destFile) {
        DebugLog.d(TAG, "CopyToFile from " + sourceFile + " to " + destFile);
        if (sourceFile == null || destFile == null || !sourceFile.exists()) {
            return false;
        }

        InputStream inputStream = null;
        FileOutputStream out = null;
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            inputStream = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096]; // SUPPRESS CHECKSTYLE
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) >= 0) {
                out.write(buffer, 0, bytesRead);
            }
            DebugLog.d(TAG, "Copy " + sourceFile + " success!");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            DebugLog.d(TAG, "Copy " + sourceFile + " failed!");
            return false;
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.getFD().sync();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
    
    public static void makeDirs(String dirName){
		DebugLog.d(TAG, "³õÊ¼»¯Ä¿Â¼£ºdirName £½ " + dirName);
		
		File tmpF = new File(dirName);
		if(!tmpF.exists()){
			tmpF.mkdirs();
		}
	}
}
