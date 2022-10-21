package com.codowang.pensieve.core.utils;

import java.io.File;

/**
 * 文件操作
 *
 * @author wangyb
 */
public class FileUtils {

    private FileUtils () {
    }

    /**
     * 删除文件夹（强制删除）
     * @param file 需要删除的文件夹
     */
    public static void deleteAllFilesOfDir(File file) {
        if (null != file) {
            if (!file.exists())
                return;
            if (file.isFile()) {
                boolean result = file.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = file.delete();
                }
            }
            File[] files = file.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteAllFilesOfDir(files[i]);
                }
            }
            file.delete();
        }
    }


    /**
     * 删除文件
     * @param pathname
     * @return
     */
    public static boolean deleteFile(String pathname) {
        boolean result = false;
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
            result = true;
            System.out.println("文件已经被成功删除");
        }
        return result;
    }
}
