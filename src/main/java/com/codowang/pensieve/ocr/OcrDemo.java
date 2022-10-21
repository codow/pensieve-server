package com.codowang.pensieve.ocr;

import com.codowang.pensieve.core.utils.ImageUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.URLEncoder;

/**
 * ocr学习示例
 *
 * @author wangyb
 */
public class OcrDemo {

    public static void main(String[] args) {
        // File imageFile = new File("D:\\Users\\codowang\\Desktop\\ocr\\src\\签到表-正.jpg");
        File imageFile = new File("D:\\Users\\codowang\\Desktop\\ocr\\src\\签到表-歪.jpg");

        // File outFile = new File("D:\\Users\\codowang\\Desktop\\ocr\\target\\签到表-正-025.jpg");

        File outFile = new File("D:\\Users\\codowang\\Desktop\\ocr\\target\\签到表-歪-025.jpg");

        double scale = 0.25d;

        ImageUtils.thumbnail(imageFile, outFile, scale);

        String imageData = ImageUtils.getImgBase(outFile);
        try {
            imageData = URLEncoder.encode(imageData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 输出到剪切板
        StringSelection stringSelection = new StringSelection(imageData);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
