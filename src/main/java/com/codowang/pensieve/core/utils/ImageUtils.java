package com.codowang.pensieve.core.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.codec.binary.Base64;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类
 *
 * @author wangyb
 */
public class ImageUtils {

    private ImageUtils () {

    }

    /**
     * 制作缩略图
     *
     * @param file 文件
     * @param scale 缩率比例
     * @return 图片文件
     */
    public static BufferedImage thumbnail (File file, double scale) {
        try {
            return Thumbnails.of(file)
                    .scale(scale)
                    .asBufferedImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 制作缩略图
     *
     * @param file 文件
     * @param outFile 输出文件
     * @param scale 缩率比例
     */
    public static void thumbnail (File file, File outFile, double scale) {
        try {
            Thumbnails.of(file)
                    .scale(scale)
                    .toFile(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片地址
     * @return base64图片
     */
    public static String getImgBase(String imgFile) {
        return getImgBase(new File(imgFile));
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片文件
     * @return base64图片
     */
    public static String getImgBase(File imgFile) {
        // 将图片文件转化为二进制流
        InputStream in;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64String(data);
    }
}
