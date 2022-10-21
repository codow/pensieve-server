package com.codowang.pensieve.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageView {

    public static final String PDF_SOURCE_DIR = "D:\\Users\\codowang\\Desktop\\ocr\\src";
    public static final String PDF_TARGET_DIR = "D:\\Users\\codowang\\Desktop\\ocr\\target";

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        new ImageViewFrame().open();
    }
}

class ImageViewFrame extends JFrame {

    private String imgPath = "D:\\Users\\codowang\\Desktop\\ocr\\target\\copybook\\0.png";

    private Mat src;

    private BufferedImage srcImage;

    private JPanel imagePanel;

    private int width = 1024;

    private int height = 768;

    public void open() throws IOException {
        // 初始化框架
        initFrame();
        // 初始化图片显示
        initImageLabel();
        // 显示
        this.setVisible(true);
    }

    private void initFrame() {
        setTitle("图形处理");
        // 设置关闭frame后程序结束
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置初始大小和位置
        this.setLocation(100, 100);
        this.setSize(width, height);
        // 设置布局
        this.setLayout(new FlowLayout());
    }

    private void initImageLabel() throws IOException {
        imagePanel = new JPanel();
        imagePanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        imagePanel.setLayout(new BorderLayout(0, 0));
        this.setContentPane(imagePanel);
        JScrollPane imageScrollPane = new JScrollPane();
        imageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        imagePanel.add(imageScrollPane, BorderLayout.CENTER);
        JLabel jLabel = new JLabel();
        src = Imgcodecs.imread(imgPath);
        srcImage = (BufferedImage) HighGui.toBufferedImage(src);
        jLabel.setIcon(new ImageIcon(srcImage));
        imageScrollPane.setViewportView(jLabel);
    }
}
