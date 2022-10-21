package com.codowang.pensieve.pdf;


import com.codowang.pensieve.core.utils.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfboxDemo {

    public static final String PDF_SOURCE_DIR = "D:\\Users\\codowang\\Desktop\\ocr\\src";
    public static final String PDF_TARGET_DIR = "D:\\Users\\codowang\\Desktop\\ocr\\target";

    @Test
    public void pdf2Jpeg () throws IOException {
        String sourceFileName = "copybook.pdf";
        String targetDirName = "copybook";

        File sourceFile = new File(PDF_SOURCE_DIR, sourceFileName);

        PDDocument doc = PDDocument.load(sourceFile);

        PDFRenderer renderer = new PDFRenderer(doc);

        int pageCount = doc.getNumberOfPages();

        System.out.println(pageCount);

        File targetDir = new File(PDF_TARGET_DIR, targetDirName);

        if (targetDir.exists()) {
            FileUtils.deleteAllFilesOfDir(targetDir);
        }

        if (targetDir.mkdirs()) {
            // 循环pdf每个页码,1页pdf转成1张图片，多页pdf会转成多张图片
            for (int i = 0; i < pageCount; i++) {
                // dpi表示图片清晰度 dpi越大转换后越清晰，相对转换速度越慢
                BufferedImage image = renderer.renderImageWithDPI(i, 144);
                //亲测 图片格式支持：jpg，png，gif，bmp，jpeg
                //写入图片
                ImageIO.write(image, "png", new File(PDF_TARGET_DIR + File.separator + targetDirName, i + ".png"));
            }
        }
    }
}
