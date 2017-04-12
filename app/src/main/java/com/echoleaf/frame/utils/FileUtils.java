/**
 *
 */
package com.echoleaf.frame.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 文件工具类
 *
 * @author John zhang
 * @version 0.1
 */
public class FileUtils {


    /**
     * 根据文件绝对路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

//	/**
//	 * 获取文件大小
//	 *
//	 * @param size 字节
//	 * @return
//	 */
//	public static String getFileSize(long size ) {
//		if ( size <= 0 )
//			return "0";
//		DecimalFormat df = new DecimalFormat( "##.##" );
//		float temp = (float) size / 1024;
//		if ( temp >= 1024 ) {
//			return df.format ( temp / 1024 ) + "M";
//		} else {
//			return df.format ( temp ) + "K";
//		}
//	}

    /**
     * 转换文件大小
     *
     * @param size
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static String getFileExt(String filename) {
        if (StringUtils.isEmpty(filename))
            return "";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    public static String generateDateFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd");
        return sdf.format(System.currentTimeMillis());
    }


}
