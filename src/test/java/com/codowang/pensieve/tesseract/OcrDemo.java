package com.codowang.pensieve.tesseract;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * 使用Tesseract做OCR
 *
 * @author wangyb
 */
public class OcrDemo {

    /**-- unicode汉字编码 \u4e00-\u9fa5 --**/

    @Test
    public void test() throws TesseractException {
        String filePath = "D:\\Workspace\\python\\opencv\\assets\\target\\copybook_split\\52.png";

        ITesseract tesseract = new Tesseract();

        tesseract.setLanguage("chi_sim");

        String result = tesseract.doOCR(new File(filePath));

        System.out.println("ocr result = " + result);
    }
}
