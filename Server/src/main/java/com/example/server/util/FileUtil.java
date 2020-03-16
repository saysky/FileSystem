package com.example.server.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author saysky
 * @date 2020/2/29 3:59 下午
 */

public class FileUtil {


    /**
     * 从输入流中读取文件内容
     *
     * @param inputStream
     * @return
     */
    public static String read(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[1024];
        try {
            while (inputStream.read(bytes) != -1) {
                sb.append(new String(bytes).trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 转换文件大小
     *
     * @param size size
     * @return String
     */
    public static String parseSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return size + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            return (size / 100) + "." + String.valueOf(size % 100) + "MB";
        } else {
            size = size * 100 / 1024;
            return (size / 100) + "." + (size % 100) + "GB";
        }
    }
}
